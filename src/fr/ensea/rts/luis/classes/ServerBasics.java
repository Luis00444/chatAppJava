package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ServerBasics {
    public static final int EndOfFile = -1;
    public static final int defaultPort = 1234;
    public static final String defaultHostAddress = "0.0.0.0";
    public static final int minimumPortNumber = 0;
    public static final int maximumPortNumber = 32767;
    public static final int maximumReceivedMessageLength = 1024;

    static boolean validatePortNumber(int portNumber){
        boolean compliesWithUpperBound = portNumber <= maximumPortNumber;
        boolean compliesWithLowerBound = portNumber >= minimumPortNumber;
        return compliesWithLowerBound && compliesWithUpperBound;
    }

    static void testPortNumber(int portNumber) throws IllegalArgumentException {
        if (!ServerBasics.validatePortNumber(portNumber)) {
            throw new IllegalArgumentException(
                    "Port value should be in the range [" + minimumPortNumber + "," + maximumPortNumber + "]"
            );
        }
    }

    public static String getStringFromBuffer(byte[] buffer, int totalRead) {
        byte[] clippedBuffer = Arrays.copyOfRange(buffer, 0, totalRead);
        String bruteClippedString = new String(clippedBuffer, StandardCharsets.UTF_8);
        return bruteClippedString.replaceAll("\n", "");
    }

    static int getPortNumberFromArgs(String[] args) {
        if (args.length == 0) {
            return defaultPort;
        }
        if (args.length == 1) {
            return Integer.parseInt(args[0]);
        }
        throw new IllegalArgumentException("Invalid number of arguments: This function accepts only 0 or one arguments, not " + args.length);
    }

    static String processInput(byte[] buffer,InputStream input) throws IOException {
        int totalRead = input.read(buffer, 0, maximumReceivedMessageLength);
        if (totalRead == EndOfFile) {
            return "";
        }
        return getStringFromBuffer(buffer, totalRead);
    }
}
