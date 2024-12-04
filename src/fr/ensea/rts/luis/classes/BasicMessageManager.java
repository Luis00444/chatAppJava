package fr.ensea.rts.luis.classes;

import fr.ensea.rts.luis.exceptions.QuitConnectionException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static fr.ensea.rts.luis.classes.ServerUtilities.noTrailingWhitespace;
import static fr.ensea.rts.luis.classes.ServerUtilities.tagMessage;

public class BasicMessageManager implements MessageManager {
    private final MultiOutputStream multiOutputStream;
    private final OutputStream ThreadOutputStream;

    public BasicMessageManager(MultiOutputStream multiOutputStream, OutputStream ThreadOutputStream) {
        this.multiOutputStream = multiOutputStream;
        this.ThreadOutputStream = ThreadOutputStream;
    }

    @Override
    public void processMessage(String message) throws IOException {
        message = noTrailingWhitespace(message);
        if (message.startsWith("#")){
            if (message.equals("#quit")){
                printGoodbye();
                throw new QuitConnectionException();
            }
            else if (message.equals("#help")){
                printHelp();
            }
            else{
                printErrorMessage(message);
            }
        }
        else{
            writeToOuts(tagMessage(message));
            System.out.println(tagMessage(message));
        }
    }

    private void writeToOuts(String message) throws IOException {
        if (!message.endsWith("\n")){
            message = message.concat("\n");
        }
        multiOutputStream.write_except(message.getBytes(StandardCharsets.UTF_8), ThreadOutputStream);
    }

    @Override
    public void close() throws IOException {
        ThreadOutputStream.close();
        multiOutputStream.remove(ThreadOutputStream);
    }
    public void printHelp() throws IOException {
        String help = "#quit: quit the session\n#help: show this message\n";
        ThreadOutputStream.write(help.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void printErrorMessage(String badString) throws IOException {
        String errorString = "error: incorrect command " +badString + "\n";
        ThreadOutputStream.write(errorString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void printGoodbye() throws IOException {
        ThreadOutputStream.write(("Goodbye!\n").getBytes(StandardCharsets.UTF_8));
        writeToOuts(Thread.currentThread().getName() + " has left the chat\n");
    }
}
