import java.net.*;


public class Receiver {

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(666);

        byte[] lenbyte = new byte[1000];
        DatagramPacket lendata = new DatagramPacket(lenbyte, lenbyte.length);
        socket.receive(lendata);
        System.out.println(lendata.getOffset());
        String st = new String(lendata.getData(), lendata.getOffset(), lendata.getLength());
        int len = Integer.parseInt(st.trim());
        System.out.println("Size of the incoming file:  " + len + " bytes");

        int count = 0;
        while (count < ((len / 1000) + 1) && len > 0) {
            try {

                byte[] bayt = new byte[1000];
                DatagramPacket in = new DatagramPacket(bayt, bayt.length);
                System.out.println("listening...");
                socket.receive(in);

                if (in.getLength() <= 0) {
                    //paket gelmedi
                    System.out.println("paket gelmedi");
                }

                socket.setSoTimeout(5000);
                //if paket gelirse count++ mesajı bas
                String message = Integer.toString(count);
                System.out.println("ACK " + message + " has been received.");
                //timeout 5 saniye


                if (in.getLength() > 0) {
                    byte[] ack = message.getBytes();
                    DatagramPacket ackdata = new DatagramPacket(ack, ack.length, in.getAddress(), in.getPort());
                    socket.send(ackdata);
                }

                count++;
            } catch (SocketTimeoutException ste) {
                System.out.println("Error when receiving file.");
            }
            if(count == (len/1000)+1){
                break;
            }
        }
        System.out.println("File has been received.");
    }
}