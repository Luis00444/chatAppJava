package fr.ensea.rts.luis.classes;

import java.io.Console;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**************************************************
 // UDP client to send messages
 // Usage UDPClient.java "server" "port"
 ***************************************************/
public class UDPClient {
    private static String serverName;
    private static int port;

    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            InetAddress serverAddress = InetAddress.getByName(serverName);
            DatagramSocket socket = new DatagramSocket();

            // Use console to read user input
            Console console = System.console();
            if (console == null) {
                System.out.println("No console available");
                return;
            }
            System.out.println("Type your message (type 'exit' to quit):");

            while (true) {
                String userInput = console.readLine();
                // Exit if the user types "exit"
                if (userInput.equalsIgnoreCase("exit") || (userInput.length() > 1024)) {
                    System.out.println("Client exiting.");
                    break;
                }

                byte[] data = userInput.getBytes(StandardCharsets.UTF_8);

                // Create a DatagramPacket to send the data
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);

                // Send the UDP packet
                socket.send(packet);
                System.out.println("Message sent: " + userInput);
            }

            // Close the socket when done
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}