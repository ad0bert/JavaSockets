package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import messenger.server.SocketServer;

public class RequestHandler extends Thread {
    private final Socket socket;
    private final SocketServer server;
    private PrintWriter out;

    private final String secret = "KADSE";

    public RequestHandler(Socket socket, SocketServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            System.out.println("Received a connection");
            // Get input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream());
            // Write out our header to the client

            this.out.println("Echo Server 1.0");
            this.out.flush();
            // Echo lines back to the client until the client closes the
            // connection or we receive an empty line
            String line = in.readLine();
            while ((line != null) && (line.length() > 0)) {
                if (line.startsWith(this.secret)) {
                    String message = line.substring(this.secret.length(), line.length()).trim();
                    System.out.println("got message: " + message);
                    this.server.distributeMessage(message + "\n");
                }
                line = in.readLine();
            }
            // Close our connection
            in.close();
            this.out.close();
            this.socket.close();
            this.server.remove(this);
            System.out.println("Connection closed");
        } catch (Exception e) {
            if (!e.getMessage().equals("Socket closed")) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if (!this.socket.isClosed() && (this.out != null)) {
            this.out.write(message);
            this.out.flush();
        }
    }

    public void shutDown() {
        try {
            this.socket.shutdownInput();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
