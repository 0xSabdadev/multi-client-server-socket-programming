import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// Program multiClient UDP server
// by Jason Alhilal & Nadiatus Salam
// IF UNDIP - JARKOM A

public class UDPServer {

    public static void main(String args[]) {
        DatagramSocket datagramSocket = null;

        try {
            datagramSocket = new DatagramSocket(8888); // packet dikirim dalam bentuk datagram ke port UDP

            byte[] buffer = new byte[90009]; // panjang buffer data yg dikirim [datagram]
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

            System.out.println("socket server jalan, menunggu data yang dikirim");

            while (true) {
                datagramSocket.receive(datagramPacket); // receive datagram packet
                byte[] data = datagramPacket.getData();
                String s = new String(data, 0, datagramPacket.getLength()); // packet ke string

                System.out.println(
                        datagramPacket.getAddress().getHostAddress() + " : " + datagramPacket.getPort() + " : " + s);

                s = "data yang terkirim : " + s;
                DatagramPacket packet = new DatagramPacket(s.getBytes(), s.getBytes().length,
                        datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(packet);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}