package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static fr.ensea.rts.luis.classes.ServerUtilities.*;

/**
 * Creates a TCP echo server that receive a connection and send what it receives
 * to the client
 */
public class TCPServer {
    private static final int maximumQueuedConnections = 10;
    private final InetSocketAddress address;
    private boolean isListening;

    /**
     * Creates a TCP Server in the specified port
     *
     * @param portToListen the port the server will listen
     * @throws IllegalArgumentException if it receives an invalid port number
     */
    public TCPServer(int portToListen) throws IllegalArgumentException {
        testPortNumber(portToListen);
        address = new InetSocketAddress(defaultHostAddress, portToListen);
        isListening = false;
    }

    /**
     * creates a TCP server in the default port
     */
    public TCPServer() {
        this(defaultPort);
    }

    public static void main(String[] args) throws IOException {
        TCPServer server;
        int port = getPortNumberFromArgs(args);
        server = new TCPServer(port);
        server.launch();
    }

    /**
     * Launch the server, so it start listening
     *
     * @throws IOException If an I/O error occurs
     */
    public void launch() throws IOException {
        ServerSocket serverSocket = new ServerSocket(address.getPort(), maximumQueuedConnections, address.getAddress());
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        System.out.println(this);

        Socket receiveSocket = serverSocket.accept();
        InputStream input = receiveSocket.getInputStream();
        OutputStream output = receiveSocket.getOutputStream();

        while (!receiveSocket.isClosed()) {

            String message = processInput(buffer, input);

            if (message.isEmpty()) {
                receiveSocket.close();
                break;
            }

            printAndEcho(message, output);
        }
        input.close();
        serverSocket.close();
    }

    /**
     * Prints a message and send to the output stream the same message
     *
     * @param message the message to print
     * @param output  where to send the message after printing (echo)
     * @throws IOException if an I/O error occurs
     */
    private void printAndEcho(String message, OutputStream output) throws IOException {
        System.out.println("<<< " + message);
        String echo = message + "\n";
        System.out.println(">>> " + message);
        output.write(echo.getBytes(StandardCharsets.UTF_8));
    }

    public int getPort() {
        return address.getPort();
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
