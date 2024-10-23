package fr.ensea.rts.luis.classes;

import javax.print.AttributeException;

public class UDPServer {
    public UDPServer(int portToListen){
        //todo: do the constructor
    }
    public UDPServer(){
        this(1234);
    }

    public void launch(){
        //todo: create this method
    }

    public static void main(String[] args) throws IllegalArgumentException{
        //todo: create the main that accept only one argument: the port
        if (args.length > 1) {
            throw new IllegalArgumentException("Only accept one or zero arguments");
        }
    }

    @Override
    public String toString() {
        return "Server";
    }
}
