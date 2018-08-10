package messenger.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import messenger.RequestHandler;

public class SocketServer extends Thread {
    private ServerSocket serverSocket;
    private final int port;
    private boolean running = false;
    private final Queue<RequestHandler> requestHandlerList = new ConcurrentLinkedQueue<RequestHandler>();

    public SocketServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        System.out.println("Shutting down");
        this.running = false;
        try {
            shutDownRequestHanlder();
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            try {
                System.out.println("Listening for a connection");
                // Call accept() to receive the next connection
                Socket socket = this.serverSocket.accept();
                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler(socket, this);
                requestHandler.start();
                this.requestHandlerList.offer(requestHandler);
            } catch (IOException e) {
                if (!e.getMessage().equals("Socket closed")) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void distributeMessage(String message) {
        this.requestHandlerList.parallelStream().forEach(new Consumer<RequestHandler>() {
            @Override
            public void accept(RequestHandler rH) {
                rH.sendMessage(message);
            }
        });
    }

    public void shutDownRequestHanlder() {
        this.requestHandlerList.parallelStream().forEach(new Consumer<RequestHandler>() {
            @Override
            public void accept(RequestHandler rH) {
                rH.shutDown();
            }
        });
    }

    public void remove(RequestHandler requestHandler) {
        this.requestHandlerList.remove(requestHandler);
    }
}