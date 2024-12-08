import fr.ensea.rts.luis.classes.*;

import java.io.IOException;
import java.net.SocketException;

public class Main {
    public static final String helpString = """
            Help:
            Starts a chat server with the described parameters. By default, the
            port is 1234, the type is tcp, and the address is localhost for clients.
           
            Usage:
            [{-u, -t, -m}] [{-s, -c}][-h] [-address=<ip>] [port]
           
            Options:
            -u                      UDP connection
            -t                      TCP connection
            -m                      TCP server that accept multiple connections. if client, is equal to TCP
            -s                      Server mode
            -c                      Client mode
            -h                      Print this message and exit
            -address=<ip address>   set the ip address to connect to (only for clients, ignored otherwise).
           """;
    public static void checkIfAssigned(String type) throws IllegalArgumentException {
        if (!type.isEmpty()) {
            throw new IllegalArgumentException("flag collision: " + type + " was already assigned");
        }
    }

    public static void checkIfAssigned(int port) throws IllegalArgumentException {
        if (port != 0) {
            throw new IllegalArgumentException("double port assignment");
        }
    }

    private static String[] getServerFromArgs(String[] args) {
        int port = 0;
        String protocol = "";
        String type = "";
        String address = "";
        boolean printHelp = false;
        if (args.length > 3){
            throw new IllegalArgumentException("Too many arguments. At most 1 arguments and 2 flag are required.");
        }
        if (args.length == 0){
            printHelp = true;
        }
        for (String arg : args) {
            if (arg.startsWith("-")) {
                if(arg.startsWith("-address=")){
                    address = arg.substring("-address=".length());
                    continue;
                }
                switch (arg) {
                    case "-u", "-t", "-m" -> {
                        checkIfAssigned(protocol);
                        protocol = arg;
                    }
                    case "-s", "-c" -> {
                        checkIfAssigned(type);
                        type = arg;
                    }
                    case "-h" -> printHelp = true;
                    default -> throw new IllegalArgumentException("Unrecognized option: " + arg);
                }
            }
            else{
                checkIfAssigned(port);
                port = Integer.parseInt(arg);
            }
        }
        if (protocol.isEmpty()){
            protocol = "-t";
        }
        if (type.isEmpty()){
            type = "-s";
        }
        if (address.isEmpty()){
            address = "127.0.0.1";
        }
        if (printHelp){
            protocol = "help";
        }
        if (port == 0){
            port = ServerUtilities.defaultPort;
        }
        return new String[]{protocol, String.valueOf(port), type, address};
    }
    public static Launchable getServerFromProtocol(String protocol, int port) throws IllegalArgumentException, SocketException {
        if (protocol.equals("-t")){
            return new TCPServer(port);
        }
        if (protocol.equals("-u")){
            return new UDPServer(port);
        }
        return new TCPMultiServer(port);
    }
    public static Launchable getClientFromProtocol(String protocol,String address, int port) throws IllegalArgumentException, IOException {
        if (protocol.equals("-t") || protocol.equals("-m")){
            return new TCPClient(address,port);
        }
        return new UDPClient(address,port);
    }
    public static void main(String[] args) throws IOException {
        String[] processed = getServerFromArgs(args);

        String protocol = processed[0];
        int port = Integer.parseInt(processed[1]);
        String type = processed[2];
        String address = processed[3];

        if (protocol.equals("help")){
            System.out.println(helpString);
            return;
        }

        Launchable toLaunch;
        if (type.equals("-s")){
            toLaunch = getServerFromProtocol(protocol,port);
        }
        else{
            toLaunch = getClientFromProtocol(protocol,address,port);
        }
        toLaunch.launch();
    }
}