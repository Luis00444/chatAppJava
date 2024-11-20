package fr.ensea.rts.luis.classes;
import javax.net.ssl.SNIServerName;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;

/****************************************
*TCP client to read and send message
 * Usage java TCPClient.java "server address" "port"
 ***************************************/
public class TCPClient {
    public static void main(String[] args) throws IOException {
        String Host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket tcp_socket;
        try {
            InetAddress serverAddr = InetAddress.getByName(Host);
            tcp_socket = new Socket(serverAddr, port);
            System.out.println("Connected to TCP server.");
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Host not found." + e.getMessage());
        }

        //Configuring Input/Output streams
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(tcp_socket.getInputStream(), "UTF-8")); // Server response
        PrintWriter serverWriter = new PrintWriter(new OutputStreamWriter(tcp_socket.getOutputStream(), "UTF-8"), true); // Send data to server;
        System.out.println("Type your message and press Enter. Press <CTRL>+D(Z for windows) to exit.");

        String userInput;
        while ((userInput = userInputReader.readLine()) != null) {
            serverWriter.println("Client:" + userInput);
            String serverOutput = serverReader.readLine();
            serverWriter.println(serverOutput);
        }
        System.out.println("Connection closed.");

        // Close the socket when done
        tcp_socket.close();
    }
}