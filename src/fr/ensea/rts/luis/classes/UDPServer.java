package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static fr.ensea.rts.luis.classes.ServerUtilities.*;

/**
 * Class UDPServer
 * This class implements a simple server that listens for incoming datagrams.
 * Then, it prints the received message if it is smaller than 1024 bytes.
 * <p> </p>
 * Usage example:
 * <p></p>
 * <code>
 * UDPServer server = new UDPServer(port);
 * server.launch();
 * </code>
 */
public class UDPServer {
    private final DatagramSocket socket;
    private boolean isListening;


    /**
     * Constructs an UDPServer object with a specific port to listen.
     * This port should be a positive integer in the range of well know
     * and registered ports
     *
     * @param portToListen port the server will listen incoming messages
     * @throws IllegalArgumentException if the port number is not in the range
     * @throws SocketException          if the socket could not be opened
     */
    public UDPServer(int portToListen) throws IllegalArgumentException, SocketException {
        testPortNumber(portToListen);
        InetSocketAddress address = new InetSocketAddress(defaultHostAddress, portToListen);
        socket = new DatagramSocket(address);
        isListening = false;
    }

    /**
     * Constructs an UDPServer object with a default port
     *
     * @throws SocketException if the socket could not be opened
     */
    public UDPServer() throws SocketException {
        this(defaultPort);
    }

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        UDPServer server;
        server = new UDPServer(getPortNumberFromArgs(args));
        server.launch();
    }

    /**
     * Starts to listen the default address and the selected port.
     * Incoming datagrams are shown preceded by "<<< "
     * If it receives an empty message, it finishes the while loop
     */
    public void launch() {
        if (!socket.isBound()) {
            throw new IllegalStateException("Server is not bound");
        }
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        DatagramPacket packet = new DatagramPacket(buffer, maximumReceivedMessageLength);
        packet.setPort(socket.getLocalPort());
        packet.setAddress(socket.getLocalAddress());
        System.out.println(this);
        try {
            while (!socket.isClosed()) {
                socket.receive(packet);
                byte[] received = packet.getData();

                String message = getStringFromBuffer(received, packet.getLength());

                if (message.isEmpty()) {
                    socket.close();
                } else {
                    System.out.println("<<< " + message);
                }
            }
        } catch (IOException e) {
            System.err.println("Listening finalized with an IOException");
            System.err.println(e.getMessage());
        } finally {
            socket.close();
        }

    }

    @Override
    public String toString() {
        if (isListening) {
            return "Server is listening in port " + socket.getLocalPort() + " at " + socket.getLocalAddress();
        } else {
            return "Server is not listening, with configured port " + socket.getLocalPort() + " at " + socket.getLocalAddress();
        }
    }
}
