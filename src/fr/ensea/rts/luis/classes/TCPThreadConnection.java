package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static fr.ensea.rts.luis.classes.ServerUtilities.maximumReceivedMessageLength;
import static fr.ensea.rts.luis.classes.ServerUtilities.processInput;

public class TCPThreadConnection extends Thread {
    private final InputStream input;
    private final OutputStream output;
    private final MultiOutputStream outs;
    private final Socket socket;

    public TCPThreadConnection(Socket socket, MultiOutputStream outs) throws IOException {
        this.input = socket.getInputStream();
        this.outs = outs;
        this.output = socket.getOutputStream();
        this.socket = socket;
        this.setName(socket.getRemoteSocketAddress().toString());
    }

    private void writeToOuts(String message) throws IOException {
        if (!message.endsWith("\n")){
            message = message.concat("\n");
        }
        outs.write_except(message.getBytes(StandardCharsets.UTF_8), output);
    }
    public void run() {
        System.out.println("TCP connection started");
        try {
            byte[] buffer = new byte[maximumReceivedMessageLength];
            boolean reading = true;
            while(reading) {
                String received = processInput(buffer,input);
                String message = Thread.currentThread().getName() + ": " + received;
                if (received.isEmpty()) {
                    System.out.println("Message is empty");
                    reading = false;
                    String goodbye = Thread.currentThread().getName() + " has left\n";
                    outs.write(goodbye.getBytes(StandardCharsets.UTF_8));
                }
                else {
                    System.out.println(message);
                    writeToOuts(message);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            outs.remove(output);
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



