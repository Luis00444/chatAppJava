package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
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
    private static final int defaultPort = 1234;
    private static final String hostAddress = "0.0.0.0";
    private static final int minimumPortNumber = 0;
    private static final int maximumPortNumber = 32767;
    public static final int maximumReceivedMessageLength = 1024;


    /**
     * Constructs an UDPServer object with a specific port to listen.
     * This port should be a positive integer in the range of well know
     * and registered ports
     * @param portToListen port the server will listen incoming messages
     * @throws IllegalArgumentException if the port number is not in the range
     * @throws SocketException if the socket could not be opened
     */
    public UDPServer(int portToListen) throws IllegalArgumentException, SocketException {
        if (!validatePortNumber(portToListen)){
            throw new IllegalArgumentException(
                    "Port value should be in the range [" + minimumPortNumber + "," + maximumPortNumber + "]"
            );
        }
        InetSocketAddress address = new InetSocketAddress(hostAddress,portToListen);
        socket = new DatagramSocket(address);
        isListening = false;
    }

    /**
     * Check if the port number is in the correct range
     * @param portNumber the port number to check
     * @return true if the port number is in range, else false
     *
     */
    private static boolean validatePortNumber(int portNumber){
        boolean compliesWithUpperBound = portNumber <= maximumPortNumber;
        boolean compliesWithLowerBound = portNumber >= minimumPortNumber;
        return compliesWithLowerBound && compliesWithUpperBound;
    }

      /**
      * Constructs an UDPServer object with a default port
      * @throws SocketException if the socket could not be opened
      */
    public UDPServer() throws SocketException {this(defaultPort);}

    /**
     * Starts to listen the default address and the selected port.
     * Incoming datagrams are shown preceded by <<<
     */
    public void launch(){
        if(!socket.isBound()){
            throw new IllegalStateException("Server is not bound");
        }
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        DatagramPacket packet = new DatagramPacket(buffer, maximumReceivedMessageLength);
        System.out.println(this);
        try {
            while (!socket.isClosed()) {
                socket.receive(packet);
                byte[] received = packet.getData();
                String message = new String(Arrays.copyOfRange(received,0,packet.getLength()), StandardCharsets.UTF_8);
                System.out.println("<<< " + message);
            }
        }
        catch (IOException e){
            System.err.println("Listening finalized with an IOException");
            System.err.println(e.getMessage());
        }
        finally {
            socket.close();
        }

    }

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        //TODO: create a function or object that has the task of extracting the arguments
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
