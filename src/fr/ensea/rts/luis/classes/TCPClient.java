package fr.ensea.rts.luis.classes;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
/**
 * **Creates a TCP client to send and receive messages
 * Usage java TCPClient.java "server address" "port"
 */
public class TCPClient implements Launchable {
    private final Socket tcp_socket;

    public void launch() throws IOException {
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(tcp_socket.getInputStream(), StandardCharsets.UTF_8)); // Server response
        PrintWriter serverWriter = new PrintWriter(new OutputStreamWriter(tcp_socket.getOutputStream(), StandardCharsets.UTF_8), true); // Send data to server;
        System.out.println("Type your message and press Enter. Press <CTRL>+D(Z for windows) to exit.");
        Console console = System.console();
        while (true) {
            // Check if there's user input
            if (System.in.available() > 0) {
                String userInput = console.readLine();
                if (userInput == null) break; // Exit if ctrl+c
                serverWriter.println(userInput);
            }
            // Check if there's a message from the server
            try {
                if (serverReader.ready()) {
                    String serverMessage = serverReader.readLine();
                    if (serverMessage != null) {
                        System.out.println("Server: " + serverMessage);
                    }
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Socket timed out");
            }
            }
        }

    public TCPClient(String host, int port ) throws IOException {
        try {
            InetAddress serverAddr = InetAddress.getByName(host);
            tcp_socket = new Socket(serverAddr, port);
            System.out.println("Connected to TCP server.");
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Host not found." + e.getMessage());
        }
    }
    public void closeConnection() throws IOException {
        tcp_socket.close();
        System.out.println("Connection closed.");
    }

    public static void main(String[] args) throws IOException {
        String Host = args[0];
        int port = Integer.parseInt(args[1]);
        //Create tcp socket
        TCPClient tcpClient = new TCPClient(Host, port);

        try {
            tcpClient.launch();
        } finally {
            tcpClient.closeConnection();
        }
    }
}