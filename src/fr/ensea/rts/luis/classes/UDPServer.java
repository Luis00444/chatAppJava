package fr.ensea.rts.luis.classes;

import javax.print.AttributeException;
import java.io.IOException;
import java.net.ServerSocket;

public class UDPServer {
    private ServerSocket socket;
    private boolean isListening;
    private final int maxLengthOfString = 1024;

    public UDPServer(int portToListen) throws IllegalArgumentException, IOException {
        //todo: do the constructor
        if (portToListen < 0 || portToListen > 32767){
            throw new IllegalArgumentException("Port value should be in the range [0,16]");
        }
        socket = new ServerSocket(portToListen);
        isListening = false;
    }
    public UDPServer() throws IOException {
        this(1234);
    }

    public void launch() throws IOException {
        isListening = true;
        socket.accept();
        
    }

    public static void main(String[] args) throws IllegalArgumentException{
        //todo: create the main that accept only one argument: the port
        if (args.length > 1) {
            throw new IllegalArgumentException("Only accept one or zero arguments");
        }
    }

    @Override
    public String toString() {
        if (isListening) {
            return "Server is listening in port " + socket.getLocalPort();
        }
        else{
            return "Server is not listening, with configured port " + socket.getLocalPort();
        }
    }
}
