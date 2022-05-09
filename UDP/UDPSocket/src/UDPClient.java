import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Program multiClient UDP server
// by Jason Alhilal & Nadiatus Salam
// IF UNDIP - JARKOM A

public class UDPClient {

    public static void main(String args[]) {
        DatagramSocket datagramSocket = null;
        String s; // pesan
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            datagramSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1"); // set adress client/server

            while (true) { // ketika masih terhubung ke server
                System.out.print("Masukkan pesan anda : ");
                s = bufferedReader.readLine();
                byte[] b = s.getBytes(); // mengirimkan ke server harus berupa bytes

                DatagramPacket datagramPacket = new DatagramPacket(b, b.length, inetAddress, 8888);
                datagramSocket.send(datagramPacket); // kirim ke server

                byte[] buffer = new byte[90009];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(packet);

                byte[] data = packet.getData();
                s = new String(data, 0, packet.getLength());

                System.out.println(packet.getAddress().getHostName() + " : " + packet.getPort() + " : " + s);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}