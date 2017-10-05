package ConceptGraph.Output;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public interface FileOutput {
    FileWriter getWriter(String fileName) throws IOException;

    FileOutputStream getFileOutputStream(String fileName) throws IOException;
}
