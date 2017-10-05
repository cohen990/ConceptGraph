package ConceptGraph.Output.Storage;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.Output.FileOutput;
import ConceptGraph.Output.Logging.Logger;

import java.io.*;

public class SingleFileGraphStore extends GraphStore {
    private final FileOutputStream graph;
    private final FileWriter index;
    private long head = 0;
    private String NEW_LINE = System.getProperty("line.separator");

    public SingleFileGraphStore(FileOutput fileOutputAssistant, Logger logger) throws IOException {
        super(logger);
        this.graph = fileOutputAssistant.getFileOutputStream("graph.grp");
        this.index = fileOutputAssistant.getWriter("index.idx");
    }

    @Override
    public void write(Node node) {
        String stringToWrite = node.toString() + NEW_LINE + NEW_LINE;
        byte[] bytesToWrite = stringToWrite.getBytes();
        try {
            index.write(node.name + ":" + head + NEW_LINE);
            graph.write(bytesToWrite);
            head += bytesToWrite.length;
        } catch (IOException e) {
            logger.logException(e);
        } finally {
            tryToFlush(graph);
            tryToFlush(index);
        }
        node.written = true;
        node.dropConnections();
    }

    @Override
    public void finalize() {
        try {
            graph.close();
        } catch (IOException e) {
            logger.logException(e);
        }
    }

    private void tryToFlush(Flushable target) {
        try {
            target.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

