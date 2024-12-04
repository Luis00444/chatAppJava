package fr.ensea.rts.luis.classes;
import javax.net.ssl.SNIServerName;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.io.Console;
/**
 * **Creates a TCP client to send and receive messages
 * Usage java TCPClient.java "server address" "port"
 */
public class TCPClient {
private static Socket tcp_socket;

    public void Tcp_IO_manager(Socket tcp_socket) throws IOException {
        Console console = System.console();
        //BufferedReader userInputReader = new BufferedReader(new InputStreamReader(console));
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(tcp_socket.getInputStream(), StandardCharsets.UTF_8)); // Server response
        PrintWriter serverWriter = new PrintWriter(new OutputStreamWriter(tcp_socket.getOutputStream(), StandardCharsets.UTF_8), true); // Send data to server;
        System.out.println("Type your message and press Enter. Press <CTRL>+D(Z for windows) to exit.");
        while (true) {
            try{
                String userInput = console.readLine();
                if (userInput == null) break; // Exit if ctrl+c
                serverWriter.println(userInput);
                String serverMessage = serverReader.readLine();
                if (serverMessage != null) {
                    System.out.println("Server: " + serverMessage);
                }
            } catch (InterruptedIOException e) {
                System.out.println(e + "Server interrupted.");
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
    public void closeConnection(Socket tcpSocket) throws IOException {
        tcpSocket.close();
        System.out.println("Connection closed.");
    }

    public static void main(String[] args) throws IOException {
        String Host = args[0];
        int port = Integer.parseInt(args[1]);
        //Create tcp socket
        TCPClient tcpClient = new TCPClient(Host, port);

        try {
            tcpClient.Tcp_IO_manager(tcp_socket);
        } finally {
            tcpClient.closeConnection(tcp_socket);
        }
    }
}