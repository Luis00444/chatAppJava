package fr.ensea.rts.luis.classes;

import java.io.Console;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**************************************************
 // UDP client to send messages
 // Usage UDPClient.java "server" "port"
 ***************************************************/
public class UDPClient {
    private final InetSocketAddress address;
    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private static final int defaultPort = 1234;

    public UDPClient(String serverName, int port) throws SocketException {
        this(new InetSocketAddress(serverName,port));
    }
    public UDPClient(InetSocketAddress address) throws SocketException {
        this.address = address;
        socket = new DatagramSocket();
        packet = new DatagramPacket(new byte[1024], 1024);
        packet.setSocketAddress(address);
    }
    public UDPClient() throws SocketException {
        this("localhost", defaultPort);
    }

    private static InetSocketAddress processArgs (String[] args) {
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
    public void receive () throws IOException {
        socket.receive(packet);
    }
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