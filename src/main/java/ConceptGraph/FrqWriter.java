package ConceptGraph;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FrqWriter {

    private OutputStream outputStream;
    private String fileName;

    public FrqWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeStream(byte[] stream) throws IOException {
        outputStream.write(stream);
    }
}