package fr.ensea.rts.luis.classes;

import java.io.Console;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;


/**
 * Class that implements a UDP client. the run method creates an interactive session,
 * and the send and receive methods could be used if needed to integrate it to another
 * program
 */
public class UDPClient {
    private final InetSocketAddress address;
    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private static final int defaultPort = 1234;

    /**
     * Creates a UDPClient object with an address port pair
     * @param serverName address of the server
     * @param port port to send the data
     * @throws SocketException if the socket creation fails
     */
    public UDPClient(String serverName, int port) throws SocketException {
        this(new InetSocketAddress(serverName,port));
    }

    /**
     * Creates an UDPClient object with an InetSocketAddress
     * @param address the address to send the data
     * @throws SocketException if the socket creation fails
     */
    public UDPClient(InetSocketAddress address) throws SocketException {
        this.address = address;
        socket = new DatagramSocket();
        packet = new DatagramPacket(new byte[1024], 1024);
        packet.setSocketAddress(address);
    }

    /**
     * Creates a UDPClient that sends data to localhost at the default port
     * @throws SocketException if the socket creation fails
     */
    public UDPClient() throws SocketException {
        this("localhost", defaultPort);
    }


    /**
     * Process the input args to an InetSocketAddress used to instance the class
     * @param args the args passed to the program
     * @return an InetSocketAddress using the args and the default
     * @throws IllegalArgumentException if there was more than 2 args
     */
    private static InetSocketAddress processArgs (String[] args) throws IllegalArgumentException {
        String serverName;
        int port;

        if (args.length == 0){
            serverName = "localhost";
            port = defaultPort;
        }
        else if (args.length == 1){
            serverName = args[0];
            port = defaultPort;
        }
        else if (args.length == 2){
            serverName = args[0];
            port = Integer.parseInt(args[1]);
        }
        else{
            throw new IllegalArgumentException("Error: invalid number of arguments, this accepts maximum 2 arguments, but " + args.length + " were given.");
        }
        return new InetSocketAddress(serverName, port);
    }

    /**
     * Run an interactive session to send and receive data from a server
     */
    public void run () {
        Thread receive_thread;
        receive_thread = new Thread(()->{
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            while (true){
                try {
                    receive();
                } catch (IOException e) {
                    break;
                }
                System.out.println(new String(packet.getData(), StandardCharsets.UTF_8));
            }
            Thread.currentThread().interrupt();
        });
        receive_thread.start();
        try {
            Console console = System.console();
            if (console == null) {
                System.out.println("No console available");
                return;
            }
            System.out.println("Type your message (type 'exit' to quit):");
            while (true) {
                String userInput = console.readLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Client exiting.");
                    break;
                }
                if (userInput.length() > 1024){
                    System.out.println("Message too large.");
                }
                send(userInput);
            }

            // Close the socket when done

            //receive_thread.interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            socket.close();
        }
    }

    /**
     * Receive a packet from the socket
     * @throws IOException if an I/O error occurs
     */
    public void receive () throws IOException {
        socket.receive(packet);
    }

    /**
     * Send a message to the server
     * @param message the message to send
     * @throws IOException if an I/O error occurs
     */
    public void send (String message) throws IOException {
        byte[] buf = message.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address);
        if (socket.isClosed()){
            return;
        }
        socket.send(packet);
    }

    public static void main(String[] args) throws SocketException {
        InetSocketAddress serverAddress = UDPClient.processArgs(args);
        UDPClient client = new UDPClient(serverAddress);
        client.run();
    }
}