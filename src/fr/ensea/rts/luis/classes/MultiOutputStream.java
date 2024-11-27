package fr.ensea.rts.luis.classes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MultiOutputStream extends OutputStream {
    private List<OutputStream> streams;
    public MultiOutputStream(List<OutputStream> streams) {
        this.streams = streams;
    }
    public MultiOutputStream() {
        this.streams = new ArrayList<>();
    }
    public void add(OutputStream stream) {
        this.streams.add(stream);
    }
    public void remove(OutputStream stream) {
        this.streams.remove(stream);
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
}
