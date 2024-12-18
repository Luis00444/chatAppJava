package fr.ensea.rts.luis.classes;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;

/**
 * Creates a TCP client to send and receive messages
 * Usage java TCPClient.java "server address" "port"
 */
public class TCPClient implements Launchable{
    private final Socket tcpSocket;

    /**TCP INput/OUTput Manager - provides message exchange between Client and Server
     * @throws IOException - throws exception if connection was interrupted
     */
    public void launch() throws IOException {
        Console console = System.console();
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream(), StandardCharsets.UTF_8)); // Server response
        PrintWriter serverWriter = new PrintWriter(new OutputStreamWriter(tcpSocket.getOutputStream(), StandardCharsets.UTF_8), true); // Send data to server;
        System.out.println("Type your message and press Enter. Press <CTRL>+D(Z for windows) to exit.");
        while (true) {
            try{
                String userInput = console.readLine(); // Getting user input
                if (userInput == null) break; // Exit Connection if User press ctrl+c
                serverWriter.println(userInput);
                String serverMessage = serverReader.readLine();
                if (serverMessage != null) {
                    System.out.println("Server: " + serverMessage); //Print out Server response
                }
            } catch (InterruptedIOException e) {
                System.out.println(e + "Service interrupted.");
            }
            }
        }

    /**
     * TCPClient - Connects to a TCP server
     * @param host server Address
     * @param port port number
     * @throws IOException - if host wasn't found
     */
    public TCPClient(String host, int port ) throws IOException {
        try {
            InetAddress serverAddr = InetAddress.getByName(host);
            tcpSocket = new Socket(serverAddr, port);
            System.out.println("Connected to TCP server.");
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Host not found." + e.getMessage());
        }
    }
    // Closes TCP socket after the end of connection
    public void closeConnection() throws IOException {
        tcpSocket.close();
        System.out.println("Connection closed.");
    }

    public static void main(String[] args) throws IOException {
        String Host = args[0]; //Server address
        int port = Integer.parseInt(args[1]); // POrt number
        TCPClient tcpClient = new TCPClient(Host, port); //Create tcp socket

        try {
            tcpClient.launch();
        } finally {
            tcpClient.closeConnection();
        }
    }
}