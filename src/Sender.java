import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

//controls whether homework.docx file sent or not
public class Sender {

    public static void main(String[] args) throws Exception {
        InetAddress ipAddress = InetAddress.getByName("localhost");
        DatagramSocket socket = new DatagramSocket();

        System.out.print("Give the path and filename: ");
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();
        File file = new File(path);


        int len = (int) file.length(); //figure out how big the file is

        //System.out.println(len);

        byte[] lenbyte = Integer.toString(len).getBytes();
        DatagramPacket lendata = new DatagramPacket(lenbyte, lenbyte.length, ipAddress, 666);
        socket.send(lendata);

        byte[] bayt = new byte[len];


        int count = 0;
        while (count < ((len / 1000) + 1) && len > 0)
        //for(int i = 1; i < (len/1000)+1 ;i++ )
        {
            int k = count * 1000;

            byte[] arr = Arrays.copyOfRange(bayt, k, k + 1000);
            //System.out.print((Arrays.toString(arr))+ " ");

            DatagramPacket dp = new DatagramPacket(arr, arr.length, ipAddress, 666);
            socket.send(dp);

            byte[] ack = new byte[1000];
            DatagramPacket ackdata = new DatagramPacket(ack, ack.length);
            socket.receive(ackdata);
            String ackmessage = new String(ackdata.getData(), ackdata.getOffset(), ackdata.getLength());
            System.out.println("ACK " + ackmessage + " has been sent");

            if (Integer.parseInt(ackmessage) == count) {
                count++;
            }
            if(count == (len/1000)+1){
                break;
            }
         }
         System.out.println("File has been sent.");
        }
    }

