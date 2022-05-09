import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
// import java.util.jar.Attributes.Name;
// javaFX untuk GUI
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Program multiClient Chat TCP server
// by Jason Alhilal & Nadiatus Salam
// IF UNDIP - JARKOM A

public class TCPClient {

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private JFrame jFrame = new JFrame("Aplikasi Chat Jarkom IF A"); // instance (new)
    private JTextField jTextField = new JTextField(40);
    private JTextArea jTextArea = new JTextArea(8, 40);

    public TCPClient() { // GUI menu utama
        jTextField.setEditable(Boolean.FALSE);
        jTextArea.setEditable(Boolean.FALSE);
        jFrame.setSize(500, 500);
        jFrame.getContentPane().add(jTextField, "North");
        jFrame.getContentPane().add(new JScrollPane(jTextArea), "Center");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation((dimension.width / 2) - (jFrame.getSize().width / 2),
                (dimension.height / 2) - (jFrame.getSize().height / 2));

        jTextField.addActionListener((ActionEvent e) -> {
            printWriter.println(jTextField.getText());
            jTextField.setText("");
        });
    }

    public String getServerAddress() { // method get Server address GUI
        return JOptionPane.showInputDialog(
                jFrame,
                "masukan ip address",
                "selamat datang di aplikasi chats",
                JOptionPane.QUESTION_MESSAGE);
    }

    public String getName() { // method get name user GUI
        return JOptionPane.showInputDialog(
                jFrame,
                "Masukkan nama anda",
                "selamat datang di aplikasi chat",
                JOptionPane.QUESTION_MESSAGE);
    }

    private void run() throws IOException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 1200); // boleh localhost
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            while (true) { // print ke console/window
                String line = bufferedReader.readLine();
                if (line.startsWith("submitname")) {
                    printWriter.println(getName());
                } else if (line.startsWith("nameaccepted")) {
                    jTextField.setEditable(Boolean.TRUE);
                } else if (line.startsWith("message")) {
                    jTextArea.append(line.substring(8) + "\n");
                }
            }
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        TCPClient TCPClient = new TCPClient();
        TCPClient.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TCPClient.jFrame.setVisible(Boolean.TRUE);
        TCPClient.run();

    }

}