package fr.ensea.rts.luis.classes;
import java.net.*;
import java.io.Console;

public class UDPClient {
    public static void main(String[] args) {

        String serverName = "localhost";
        int port = 8080;

        try {
            InetAddress serverAddress = InetAddress.getByName(serverName);
            DatagramSocket socket = new DatagramSocket();

            // Use the Console class to read user input
            Console console = System.console();
            if (console == null) {
                System.out.println("No console available");
                return;
            }
            System.out.println("Type your message (type 'exit' to quit):");


            while (true) {
                String userInput = console.readLine();

                // Exit if the user types "exit"
                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Client exiting.");
                    break;
                }

                byte[] data = userInput.getBytes("UTF-8");

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