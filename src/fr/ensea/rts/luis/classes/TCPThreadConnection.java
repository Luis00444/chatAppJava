package fr.ensea.rts.luis.classes;

import fr.ensea.rts.luis.exceptions.QuitConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static fr.ensea.rts.luis.classes.ServerUtilities.*;

public class TCPThreadConnection extends Thread {
    private final InputStream input;
    private final MessageManager manager;
    private final Socket socket;

    public TCPThreadConnection(Socket socket, MessageManager manager) throws IOException {
        this.input = socket.getInputStream();
        this.manager = manager;
        this.socket = socket;
        this.setName(socket.getRemoteSocketAddress().toString());
    }
    public void run() {
        System.out.println("TCP connection started");
        try {
            byte[] buffer = new byte[maximumReceivedMessageLength];
            boolean reading = true;
            while(reading) {
                String received = processInput(buffer,input);
                try{
                    manager.processMessage(received);
                }
                catch(QuitConnectionException e){
                    reading = false;
                    socket.close();
                    manager.close();
                    input.close();
                }
            }
        }
        catch (IOException e) {
            System.out.println("TCP connection failed for "+ socket.getRemoteSocketAddress());
        }
        finally {
            try {
                input.close();
                socket.close();
                manager.close();
            } catch (IOException e) {
                System.out.println("TCP connection failed for "+ socket.getRemoteSocketAddress());
            }
        }
    }
}



