package fr.ensea.rts.luis.classes;
import javax.net.ssl.SNIServerName;
import java.io.IOException;
import java.net.*;
import java.io.Console;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        String Host = args[0];
        int port = Integer.parseInt(args[1]);

        try{
            InetAddress serverAddr = InetAddress.getByName(Host);
            Socket tcp_socket = new Socket(serverAddr, port);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
