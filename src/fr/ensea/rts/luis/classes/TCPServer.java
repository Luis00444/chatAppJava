package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static fr.ensea.rts.luis.classes.ServerUtilities.*;

public class TCPServer {
    private boolean isListening;
    private static final int maximumQueuedConnections = 10;
    private final InetSocketAddress address;

    public TCPServer(int portToListen) throws IllegalArgumentException {
        testPortNumber(portToListen);
        address = new InetSocketAddress(defaultHostAddress,portToListen);
        isListening = false;
    }

    public TCPServer() {
        this(defaultPort);
    }

    public void launch() throws IOException {
        ServerSocket serverSocket = new ServerSocket(address.getPort(), maximumQueuedConnections, address.getAddress());
        isListening = true;
        byte[] buffer = new byte[maximumReceivedMessageLength];
        System.out.println(this);

        Socket receiveSocket = serverSocket.accept();
        InputStream input = receiveSocket.getInputStream();
        OutputStream output = receiveSocket.getOutputStream();

        while (!receiveSocket.isClosed()) {

            String message = processInput(buffer,input);

            if (message.isEmpty()) {
                receiveSocket.close();
                break;
            }

            printAndEcho(message,output);
        }
        input.close();
        serverSocket.close();
    }

    private void printAndEcho(String message, OutputStream output) throws IOException {
        System.out.println("<<< " + message);
        String echo = message + "\n";
        output.write(echo.getBytes(StandardCharsets.UTF_8));
    }


    public static void main(String[] args) throws IOException {
        TCPServer server;
        int port = getPortNumberFromArgs(args);
        server = new TCPServer(port);
        server.launch();
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
