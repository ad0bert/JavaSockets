package messenger;

import messenger.server.SocketServer;

public class TestMainApp {
    public static void main(String[] args) {
        int port = 9090;
        if (args.length == 0) {
            System.out.println("Usage: SimpleSocketServer <port>");
        } else {
            port = Integer.parseInt(args[0]);
        }
        System.out.println("Start server on port: " + port);
        SocketServer server = new SocketServer(port);
        server.startServer();
        // Automatically shutdown in 1 minute
        try {
            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.stopServer();
    }
}
