package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static fr.ensea.rts.luis.classes.ServerUtilities.*;

public class TCPMultiServer extends TCPServer {
    private boolean isListening;
    private final InetSocketAddress address;
    private static final int maximumQueuedConnections = 10;

    public TCPMultiServer(int portToListen) {
        testPortNumber(portToListen);
        address = new InetSocketAddress(defaultHostAddress, portToListen);
        this.isListening = false;
    }

    public TCPMultiServer() {
        this(defaultPort);
    }


    @Override
    public void launch() throws IOException {
        isListening = true;
        try (ServerSocket serverSocket = new ServerSocket(address.getPort(), maximumQueuedConnections, address.getAddress())) {
            serverSocket.setSoTimeout(timeout_milliseconds);
            System.out.println(this);
            MultiOutputStream outs = new MultiOutputStream();
            while (isListening) {
                try {
                    Socket receiveSocket = serverSocket.accept();
                    outs.add(receiveSocket.getOutputStream());
                    new TCPThreadConnection(receiveSocket,outs).start();
                } catch (SocketTimeoutException e) {
                    isListening = false;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TCPMultiServer server;
        int port = getPortNumberFromArgs(args);
        server = new TCPMultiServer(port);
        server.launch();
    }


    @Override
    public String toString() {
        if (isListening) {
            return "Server is listening in port " + getPort() + " at " + address.getAddress();
        } else {
            return "Server is not listening, with configured port " + getPort() + " at " + address.getAddress();
        }
    }
}
