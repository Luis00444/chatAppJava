package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static fr.ensea.rts.luis.classes.ServerUtilities.*;

/**
 * TCP server that use threading to serve at the same time multiple clients
 * The clients can send messages and the other clients could see them. There
 * are different commands that could be used (write #help with a client to see them)
 */
public class TCPMultiServer extends TCPServer {
    private static final int maximumQueuedConnections = 10;
    private final InetSocketAddress address;
    private boolean isListening;

    /**
     * Creates a TCP server that could manage multi-client communication
     * @param portToListen Port the server listen to
     */
    public TCPMultiServer(int portToListen) throws IllegalArgumentException{
        testPortNumber(portToListen);
        address = new InetSocketAddress(defaultHostAddress, portToListen);
        this.isListening = false;
    }

    /**
     * Creates a TCP server that could manage multi-client communication at the default port.
     * See ServerUtilities.DefaultPort
     */
    public TCPMultiServer() {
        this(defaultPort);
    }

    /**
     * Execute a TCP multi-client server at the specified port in args, or the
     * default port if no args were given
     * @param args Only accepts one argument, the port
     * @throws IOException if an IO error occurs
     * @throws IllegalArgumentException if more than 1 argument was given, or if it was
     * not a valid port number
     */
    public static void main(String[] args) throws IOException, IllegalArgumentException {
        TCPMultiServer server;
        int port = getPortNumberFromArgs(args);
        server = new TCPMultiServer(port);
        server.launch();
    }

    /**
     * Launches the server. It starts to listen to connections at the given port at creation
     * @throws IOException if an I/O error occurs
     */
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
                    MessageManager messageManager = new CommandMessageManager(outs, receiveSocket.getOutputStream());
                    new TCPThreadConnection(receiveSocket, messageManager).start();
                } catch (SocketTimeoutException e) {
                    isListening = false;
                }
            }
        }
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
