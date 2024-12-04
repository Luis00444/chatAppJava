package fr.ensea.rts.luis.classes;

import fr.ensea.rts.luis.exceptions.QuitConnectionException;

import java.io.IOException;

public interface MessageManager {
    /**
     * Process and prints the message as needed
     * @param message The message to print
     * @throws IOException if an I/O error occurs
     * @throws QuitConnectionException if the client send a command to exit
     */
    void processMessage(String message) throws IOException, QuitConnectionException;

    /**
     * Close all the input and output used
     * @throws IOException if an I/O error occurs
     */
    void close() throws IOException;

    /**
     * Print all the commands the manager has to the respective output
     * @throws IOException if an I/O error occurs
     */
    void printHelp() throws IOException;

    /**
     * Print to the respective output that an invalid command was passed or
     * an error happened
     * @param badString The string that was used or the description of the error
     * @throws IOException if an I/O error occurs
     */
    void printErrorMessage(String badString) throws IOException;

    /**
     * Prints a goodbye message to the respective outputs and throws a QuitConnectionException
     * @throws IOException if an I/O error occurs
     * @throws QuitConnectionException Always, to signal an end of a connection
     */
    void printGoodbye() throws IOException, QuitConnectionException;
}
