package fr.ensea.rts.luis.classes;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/****************************************
*TCP client to read and send message
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

        /* Sending text to Server */
        OutputStream outputStream = tcp_socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
        printWriter.println("Hello from Client");

        //Receive data from Server
        InputStream inputStream = tcp_socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String serverResponse = bufferedReader.readLine();
        System.out.println(serverResponse);

        // Use console to read user input
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }
        System.out.println("Type your message (type 'exit' to quit):");

        //Get text from User until types <ctrl>+D

        while (true) {
            String userInput = console.readLine();
            if (userInput == null || (userInput.length() > 1024)) {
                System.out.println("Client exiting.");
                break;
            }
        }
    }
}
