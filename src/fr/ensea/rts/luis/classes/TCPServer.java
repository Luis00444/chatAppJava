package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPServer {
    private boolean isListening;
    private static final int defaultPort = 1234;
    private static final String hostAddress = "0.0.0.0";
    private static final int minimumPortNumber = 0;
    private static final int maximumPortNumber = 32767;
    private static final int maximumReceivedMessageLength = 1024;
    private final InetSocketAddress address;

    public TCPServer(int portToListen) {
        if (!validatePortNumber(portToListen)){
            throw new IllegalArgumentException(
                    "Port value should be in the range [" + minimumPortNumber + "," + maximumPortNumber + "]"
            );
        }
        address = new InetSocketAddress(hostAddress,portToListen);
        isListening = false;
    }

    public TCPServer() {
        this(defaultPort);
    }

    void launch() throws IOException {
        //TODO: finish this thing
        ServerSocket serverSocket = new ServerSocket(address.getPort(), 10, address.getAddress());
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        System.out.println(this);

        Socket receiveSocket = serverSocket.accept();
        InputStream input = receiveSocket.getInputStream();
        while (receiveSocket.isInputShutdown()) {
            int totalRead = input.read(buffer, 0, maximumReceivedMessageLength);
            String message = new String(Arrays.copyOfRange(buffer, 0, totalRead), StandardCharsets.UTF_8);
            System.out.println("<<< " + message);
        }
        input.close();
        serverSocket.close();



    }


    public static void main(String[] args) throws IOException {
        //TODO: create a function or object that has the task of extracting the arguments
        TCPServer server;
        if (args.length == 1){
            server = new TCPServer(Integer.parseInt(args[0]));
        }
        else if (args.length == 0){
            server = new TCPServer();
        }
        else{
            throw new IllegalArgumentException("Only accept one or zero arguments");
        }
        server.launch();

    }

    private static boolean validatePortNumber(int portNumber){
        boolean compliesWithUpperBound = portNumber <= maximumPortNumber;
        boolean compliesWithLowerBound = portNumber >= minimumPortNumber;
        return compliesWithLowerBound && compliesWithUpperBound;
    }

    public int getPort(){
        return address.getPort();
    }

    @Override
    public String toString() {
        if (isListening) {
            return "Server is listening in port " + getPort() + " at " + address.getAddress();
        }
        else{
            return "Server is not listening, with configured port " + getPort() + " at " + address.getAddress();
        }
    }
}
