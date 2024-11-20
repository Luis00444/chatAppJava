package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPServer {
    private static final int EndOfFile = -1;
    private boolean isListening;
    private static final int defaultPort = 1234;
    private static final String hostAddress = "0.0.0.0";
    private static final int minimumPortNumber = 0;
    private static final int maximumPortNumber = 32767;
    private static final int maximumReceivedMessageLength = 1024;
    private static final int maximumQueuedConnections = 10;
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
        ServerSocket serverSocket = new ServerSocket(address.getPort(), maximumQueuedConnections, address.getAddress());
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        System.out.println(this);

        Socket receiveSocket = serverSocket.accept();
        InputStream input = receiveSocket.getInputStream();
        OutputStream output = receiveSocket.getOutputStream();
        while (!receiveSocket.isClosed()) {
            int totalRead = input.read(buffer, 0, maximumReceivedMessageLength);

            if (totalRead == EndOfFile){
                receiveSocket.close();
                break;
            }

            String message = getStringFromBuffer(buffer, totalRead);
            if (message.isEmpty()) {
                receiveSocket.close();
            }
            else {
                System.out.println("<<< " + message);
                String echo = message + "\n";
                output.write(echo.getBytes(StandardCharsets.UTF_8));
            }
        }
        input.close();
        serverSocket.close();
    }

    private static String getStringFromBuffer(byte[] buffer, int totalRead) {
        byte[] clippedBuffer = Arrays.copyOfRange(buffer, 0, totalRead);
        String bruteClippedString = new String(clippedBuffer, StandardCharsets.UTF_8);
        return bruteClippedString.replaceAll("\n", "");
    }

    private static int getPortNumberFromArgs(String[] args) {
        if (args.length == 0) {
            return defaultPort;
        }
        if (args.length == 1) {
            return Integer.parseInt(args[0]);
        }
        throw new IllegalArgumentException("Invalid number of arguments: This function accepts only 0 or one arguments, not " + args.length);
    }

    public static void main(String[] args) throws IOException {
        TCPServer server;
        int port = getPortNumberFromArgs(args);
        server = new TCPServer(port);
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
