package fr.ensea.rts.luis.classes;

import fr.ensea.rts.luis.exceptions.QuitConnectionException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CommandMessageManager implements MessageManager {
    public static final String welcomeString = "type a message or type #help to see a list of commands\n";
    public static final String helpString = """
            Commands:
            #quit
            #exit           Finish the communication
            #version        Prints the version of the message manager.
            #name           Prints the name of the thread
            #name <String>  Changes the name of the thread
            #sleep <long>   Makes the thread sleep <long> milliseconds
            """;
    private final MultiOutputStream multiOutputStream;
    private final OutputStream ThreadOutputStream;

    public CommandMessageManager(MultiOutputStream outs, OutputStream threadOutputStream) throws IOException {
        this.multiOutputStream = outs;
        this.ThreadOutputStream = threadOutputStream;
        this.ThreadOutputStream.write(welcomeString.getBytes(StandardCharsets.UTF_8));
    }

    public void processMessage(String message) throws IOException, QuitConnectionException {
        message = message.trim();
        if (message.startsWith("#")) {
            try {
                processCommands(message.substring(1));
            } catch (InterruptedException e) {
                printErrorMessage(e.getMessage());
            }
        }
        else {
            if (message.isEmpty()){
                return;
            }
            writeToOuts(this.tagMessage(message));
            System.out.println(this.tagMessage(message));
        }
    }

    private String tagMessage(String message) {
        return ServerUtilities.tagMessage(multiOutputStream.getName(ThreadOutputStream), message);
    }

    private void processCommands(String message) throws IOException, InterruptedException {
        message = message.trim();
        if (message.equals("quit")) {
            printGoodbye();
        } else if (message.equals("help")) {
            printHelp();
        } else if (message.equals("exit")) {
            printGoodbye();
        } else if (message.equals("name")) {
            printName();
        } else if (message.equals("version")) {
            printVersion();
        } else if (message.startsWith("name ")) {
            String newName = message.substring("name ".length());
            changeName(newName);
        } else if (message.startsWith("sleep ")) {
            int sleep = Integer.parseInt(message.substring("sleep ".length()));
            Thread.sleep(sleep);
        } else {
            printErrorMessage("Unknown command: " + message);
        }
    }

    private void writeToOuts(String message) throws IOException {
        if (!message.endsWith("\n")) {
            message = message.concat("\n");
        }
        multiOutputStream.write_except(message.getBytes(StandardCharsets.UTF_8), ThreadOutputStream);
    }

    public void printGoodbye() throws IOException, QuitConnectionException {
        ThreadOutputStream.write(("Goodbye!\n").getBytes(StandardCharsets.UTF_8));
        writeToOuts(multiOutputStream.getName(ThreadOutputStream) + " has left the chat\n");
        close();
        throw new QuitConnectionException();
    }

    public void printHelp() throws IOException {
        ThreadOutputStream.write(helpString.getBytes(StandardCharsets.UTF_8));
    }

    public void printName() throws IOException {
        ThreadOutputStream.write(("Name: " + multiOutputStream.getName(ThreadOutputStream) + "\n").getBytes(StandardCharsets.UTF_8));
    }

    public void changeName(String newName) {
        multiOutputStream.setName(ThreadOutputStream, newName.trim());
    }

    public void printVersion() throws IOException {
        ThreadOutputStream.write(("Version: CommandMessageManager-1").getBytes(StandardCharsets.UTF_8));
    }

    public void printErrorMessage(String message) throws IOException {
        ThreadOutputStream.write(("Error: " + message + "\n").getBytes(StandardCharsets.UTF_8));
    }

    public void close() throws IOException {
        multiOutputStream.remove(ThreadOutputStream);
        ThreadOutputStream.close();
    }
}
