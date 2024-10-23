package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.BufferOverflowException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPServer {
    private final DatagramSocket socket;
    private boolean isListening;

    public UDPServer(int portToListen) throws IllegalArgumentException, IOException {
        //todo: do the constructor
        if (portToListen < 0 || portToListen > 32767){
            throw new IllegalArgumentException("Port value should be in the range [0,16]");
        }
        InetSocketAddress address = new InetSocketAddress("0.0.0.0",portToListen);
        socket = new DatagramSocket(address);
        isListening = false;
    }
    public UDPServer() throws IOException {
        this(1234);
    }

    public void launch(){
        isListening = true;
        int maxLengthOfString = 1024;
        byte[] buffer = new byte[maxLengthOfString];
        DatagramPacket packet = new DatagramPacket(buffer, maxLengthOfString);
        try {
            while (true) {
                socket.receive(packet);
                if (packet.getLength() > maxLengthOfString) throw new BufferOverflowException();
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
        //todo: create the main that accept only one argument: the port
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
        System.out.println(server);
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
