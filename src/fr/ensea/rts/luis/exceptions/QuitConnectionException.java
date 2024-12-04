package fr.ensea.rts.luis.exceptions;

/**
 * Exception that marks the end of the communication for a client
 */
public final class QuitConnectionException extends BaseMessageManagerException {
    /**
     * Creates an exception that denotes a voluntary end of a connection
     */
    public QuitConnectionException() {

    }
}
