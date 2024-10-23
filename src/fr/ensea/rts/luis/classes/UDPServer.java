package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.BufferOverflowException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Class UDPServer
 * This class implements a simple server that listens for incoming datagrams.
 * Then, it prints the received message if it is smaller than 1024 bytes.
 * <p> </p>
 * Usage example:
 * <p></p>
 * <code>
 *    UDPServer server = new UDPServer(port);
 *    server.launch();
 *  </code>
 */
public class UDPServer {
    private final DatagramSocket socket;
    private boolean isListening;
    static final int defaultPort = 1234;
    private static final String hostAddress = "0.0.0.0";
    private static final int minimumPortNumber = 0;
    private static final int maximumPortNumber = 32767;
    private static final int maximumReceivedMessageLength = 1024;


    /**
     * Constructs an UDPServer object with a specific port to listen.
     * This port should be a positive integer in the range of well know
     * and registered ports
     * @param portToListen port the server will listen incoming messages
     * @throws IllegalArgumentException if the port number is not in the range
     * @throws SocketException if the socket could not be opened
     */
    public UDPServer(int portToListen) throws IllegalArgumentException, SocketException {

        if (portToListen < minimumPortNumber || portToListen > maximumPortNumber){
            throw new IllegalArgumentException(
                    "Port value should be in the range [" + minimumPortNumber + "," + maximumPortNumber + "]"
            );
        }
        InetSocketAddress address = new InetSocketAddress(hostAddress,portToListen);
        socket = new DatagramSocket(address);
        isListening = false;
    }


      /**
      * Constructs an UDPServer object with a default port
      * @throws SocketException if the socket could not be opened
      */
    public UDPServer() throws SocketException {
        this(defaultPort);
    }

    /**
     * Starts to listen the default address and the selected port.
     * Incoming datagrams are shown preceded by <<<
     */
    public void launch(){
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        DatagramPacket packet = new DatagramPacket(buffer, maximumReceivedMessageLength);
        System.out.println(this);
        try {
            while (!socket.isClosed()) {
                socket.receive(packet);
                if (packet.getLength() > maximumReceivedMessageLength) throw new BufferOverflowException();
                byte[] received = packet.getData();
                String message = new String(Arrays.copyOfRange(received,0,packet.getLength()), StandardCharsets.UTF_8);
                System.out.print("<<< " + message);
            }
        }
        catch (IOException e){
            System.err.println("Listening finalized with an IOException");
            System.err.println(e.getMessage());
        }
        catch (BufferOverflowException e){
            System.err.println("BufferOverflowException: Data received exceeded buffer size");
            System.err.println(e.getMessage());
        }
        finally {
            socket.close();
        }

    }

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        UDPServer server;
        if (args.length == 1){
            server = new UDPServer(Integer.parseInt(args[0]));
        }
        else if (args.length == 0){
            server = new UDPServer();
        }
        else{
            throw new IllegalArgumentException("Only accept one or zero arguments");
        }
        server.launch();
    }

    @Override
    public String toString() {
        if (isListening) {
            return "Server is listening in port " + socket.getLocalPort() + " at " + socket.getLocalAddress();
        }
        else{
            return "Server is not listening, with configured port " + socket.getLocalPort() + " at " + socket.getLocalAddress();
        }
    }
}
