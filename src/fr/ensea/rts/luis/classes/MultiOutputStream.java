package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Stream that saves a list of output streams and write to them sequentially
 */
public class MultiOutputStream extends OutputStream {
    private static int counter = 0;
    private final List<OutputStream> streams;
    private final Dictionary<OutputStream, String> outputNames;

    /**
     * creates a MultiOutput stream with initial output streams
     * @param streams the streams to put in the multiOutputStream from the begining
     */
    public MultiOutputStream(List<OutputStream> streams) {
        this.streams = streams;
        outputNames = new Hashtable<>();
    }

    /**
     * creates a MultiOutput stream that has no stream inside
     */
    public MultiOutputStream() {
        this(new ArrayList<>());
    }

    /**
     * adds an output stream to the MultiOutput stream
     * @param stream the output to add
     */
    public void add(OutputStream stream) {
        this.streams.add(stream);
        setName(stream, "Person #" + ++counter);
    }

    /**
     * removes a single output stream from the Stream
     * @param stream the stream to be removed
     */
    public void remove(OutputStream stream) {
        this.streams.remove(stream);
        this.outputNames.remove(stream);
    }

    /**
     * write to all output streams the respective byte
     * @param b the {@code byte}.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void write(int b) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(b);
        }
    }

    /**
     * write to all output streams the respective bytes
     * @param b the data.
     * @throws IOException if an I/O error occurs
     */
    public void write(byte[] b) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(b);
        }
    }

    /**
     * write to all output  {@code len} bytes with offset {@code off} from {@code b}
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs
     */
    public void write(byte[] b, int off, int len) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(b, off, len);
        }
    }

    /**
     * Writes {@code b} bytes to all output streams except {@code excludedOut}
     * @param b the data
     * @param excludedOut the stream to exclude
     * @throws IOException if an I/O error occurs
     */
    public void write_except(byte[] b, OutputStream excludedOut) throws IOException {
        for (OutputStream stream : streams) {
            if (stream != excludedOut) {
                stream.write(b);
            }
        }
    }

    /**
     * Flush all output streams
     * @throws IOException if an I/O error occurs
     */
    public void flush() throws IOException {
        for (OutputStream stream : streams) {
            stream.flush();
        }
    }

    /**
     * close all output streams
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        for (OutputStream stream : streams) {
            stream.close();
        }
    }

    /**
     * Get the name of the stream. By default, it is "person #" plus an internal
     * counter
     * @param stream the stream to be named
     * @return the name of the stream
     */
    public String getName(OutputStream stream) {
        return outputNames.get(stream);
    }

    /**
     * set the name of a specified stream
     * @param stream the stream to be renamed
     * @param name the new name of the stream
     * @throws IllegalArgumentException if the stream is not inside the internal list of streams
     */
    public void setName(OutputStream stream, String name) throws IllegalArgumentException {
        if (!streams.contains(stream)){
            throw new IllegalArgumentException("stream does not exist");
        }
        outputNames.put(stream, name);
    }
}
