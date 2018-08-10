package messenger.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket("127.0.0.1", 9090);
            // Create input and output streams to read from and write to the
            // server
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an
            // empty line
            out.println("KADSE HTTP/1.0");
            out.println();
            // Read data from the server until we finish reading the document
            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }
            // Close our streams
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
