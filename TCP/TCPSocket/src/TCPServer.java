import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

// Program multiClient Chat TCP server
// by Jason Alhilal & Nadiatus Salam
// IF UNDIP - JARKOM A

public class TCPServer {

    private static final int PORT = 1200; // inisialisasi port
    private static HashSet<String> names = new HashSet<>();
    private static HashSet<PrintWriter> printWriters = new HashSet<>();

    public static void main(String[] args) throws IOException {
        System.out.println("server jalan pada port : " + PORT);
        ServerSocket serverSocket = new ServerSocket(PORT); // inisialisasi port TCP
        try {
            while (true) {
                new Handler(serverSocket.accept()).start(); // acc server run menunggu req client
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            serverSocket.close();
        }
    }

    private static class Handler extends Thread { // inheritence ke Thread untuk server menunggu req client

        private String name;
        private Socket socket;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    printWriter.println("submitname");
                    name = bufferedReader.readLine(); // baca name dari client

                    if (name == null) { // jika kosong harus isi lagi
                        return;
                    }

                    synchronized (names) {

                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }

                    }
                }

                printWriter.println("nameaccepted");
                printWriters.add(printWriter);

                while (true) {
                    String input = bufferedReader.readLine(); // baca message dari client

                    if (input == null) { // jika kosong harus isi lagi
                        return;
                    }

                    printWriters.stream().forEach((pw) -> { // kirim message ke window
                        pw.println("message " + name + " : " + input);
                    });

                }

            } catch (IOException e) { // jika error print err
                System.out.println(e);
            } finally {

                if (name != null) {
                    names.remove(name);
                }

                if (printWriter != null) {
                    printWriters.remove(printWriter);
                }

                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}