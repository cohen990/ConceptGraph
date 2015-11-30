package ConceptGraph.Output.Storage;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.DataStructures.Storage;
import ConceptGraph.Output.FileOutputAssistant;
import ConceptGraph.Output.Logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SingleFileGraphStore extends GraphStore {
    private final FileOutputAssistant fileOutputAssistant;
    private final FileWriter writer;

    public SingleFileGraphStore(FileOutputAssistant fileOutputAssistant, Logger logger) throws IOException {
        super(logger);
        this.fileOutputAssistant = fileOutputAssistant;
        this.writer = fileOutputAssistant.getWriter("graph.grp");
    }

    private Writer getWriter(String title) {
        return writer;
    }

    @Override
    public void writeNodeToFile(Node node) {
        Writer file = getWriter(node.name);
        try {
            file.write(node.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
        } catch (IOException e) {
            logger.logException(e);
        } finally {
            tryToClose(file);
        }
        node.written = true;
        node.dropConnections();
    }

    private void tryToClose(Writer nodes) {
        try {
            nodes.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

