package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Class that stores constants and utility methods for the servers.
 */
final class ServerUtilities {
    public static final int EndOfFile = -1;
    public static final int defaultPort = 1234;
    public static final String defaultHostAddress = "0.0.0.0";
    public static final int minimumPortNumber = 0;
    public static final int maximumPortNumber = 32767;
    public static final int maximumReceivedMessageLength = 1024;

    /**
     * Function that takes a port number and validates is in the correct range
     * @param portNumber The number we are testing
     * @return true if it is valid
     */
    static boolean validatePortNumber(int portNumber){
        boolean compliesWithUpperBound = portNumber <= maximumPortNumber;
        boolean compliesWithLowerBound = portNumber >= minimumPortNumber;
        return compliesWithLowerBound && compliesWithUpperBound;
    }

    /**
     * function that raises an exception when an invalid port number is given (see minimumPortNumber
     * and maximumPortNumber).
     * If the port is valid, it does nothing
     * @param portNumber the number to be tested
     * @throws IllegalArgumentException when the port is not in the correct range
     */
    static void testPortNumber(int portNumber) throws IllegalArgumentException {
        if (!ServerUtilities.validatePortNumber(portNumber)) {
            throw new IllegalArgumentException(
                    "Port value should be in the range [" + minimumPortNumber + "," + maximumPortNumber + "]"
            );
        }
    }

    /**
     * Function that transform values from a byte array buffer to a string
     * @param buffer the buffer to transform
     * @param totalRead the number of characters read from the buffer
     * @return a string of the encoded (UTF-8) values of the buffer (Without the last line break
     */
    public static String getStringFromBuffer(byte[] buffer, int totalRead) {
        byte[] clippedBuffer = Arrays.copyOfRange(buffer, 0, totalRead);
        String bruteClippedString = new String(clippedBuffer, StandardCharsets.UTF_8);
        return bruteClippedString.replaceAll("\n", "");
    }

    /**
     * Function that returns a port number based on the input argument array.
     * the args should be of length 0 or 1, or an exception would be raised.
     * @param args The argument array
     * @return the default port of no arguments are given, or the integer casting of the first argument
     * @throws IllegalArgumentException when the argument array has a length bigger than one
     */
    static int getPortNumberFromArgs(String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            return defaultPort;
        }
        if (args.length == 1) {
            return Integer.parseInt(args[0]);
        }
        throw new IllegalArgumentException("Invalid number of arguments: This function accepts only 0 or one arguments, not " + args.length);
    }

    /**
     * Function that process an input stream with a buffer to get the value the stream received
     * as a string
     * @param buffer the buffer that will be used to read the stream.
     * @param input the input stream to be read
     * @return the data received as an string
     * @throws IOException If the first byte cannot be read for any reason other than end of file,
     * or if the input stream has been closed, or if some other I/ O error occurs.
     */
    static String processInput(byte[] buffer,InputStream input) throws IOException {
        int totalRead = input.read(buffer, 0, maximumReceivedMessageLength);
        if (totalRead == EndOfFile) {
            return "";
        }
        return getStringFromBuffer(buffer, totalRead);
    }
}
