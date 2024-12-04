package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class MultiOutputStream extends OutputStream {
    private final List<OutputStream> streams;
    private final Dictionary<OutputStream, String> outputNames;
    private static int counter = 0;

    public MultiOutputStream(List<OutputStream> streams) {
        this.streams = streams;
        outputNames = new Hashtable<>();
    }
    public MultiOutputStream() {
        this(new ArrayList<>());
    }
    public void add(OutputStream stream) {
        this.streams.add(stream);
        setName(stream, "Person #" + ++counter);
    }
    public void remove(OutputStream stream) {
        this.streams.remove(stream);
        this.outputNames.remove(stream);
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(b);
        }
    }
    public void write(byte[] b) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(b);
        }
    }
    public void write(byte[] b, int off, int len) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(b, off, len);
        }
    }
    public void write_except(byte[] b,OutputStream excludedOut) throws IOException {
        for (OutputStream stream : streams) {
            if (stream != excludedOut) {
                stream.write(b);
            }
        }
    }
    public void flush() throws IOException {
        for (OutputStream stream : streams) {
            stream.flush();
        }
    }
    public void close() throws IOException {
        for (OutputStream stream : streams) {
            stream.close();
        }
    }

    public String getName(OutputStream stream) {
        return outputNames.get(stream);
    }
    public void setName(OutputStream stream, String name) {
        outputNames.put(stream, name);
    }
}
