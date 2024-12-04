package fr.ensea.rts.luis.classes;

import fr.ensea.rts.luis.exceptions.QuitConnectionException;

import java.io.IOException;

public interface MessageManager {
    void processMessage(String message) throws IOException, QuitConnectionException;
    void close() throws IOException;
    void printHelp() throws IOException;
    void printErrorMessage(String badString) throws IOException;
    void printGoodbye() throws IOException, QuitConnectionException;
}
