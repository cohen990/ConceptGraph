package ConceptGraph.Output.Storage;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.Output.FileOutputAssistant;
import ConceptGraph.Output.Logging.Logger;
import ConceptGraph.DataStructures.Storage;

import java.io.IOException;
import java.io.Writer;

public class IndividualFilesGraphStore extends GraphStore {
    private final FileOutputAssistant fileOutputAssistant;

    public IndividualFilesGraphStore(FileOutputAssistant fileOutputAssistant, Logger logger) {
        super(logger);
        this.fileOutputAssistant = fileOutputAssistant;
    }

    private Writer getWriter(String title) {
        Writer writer = null;
        try {
            if (!Storage.SeenLinks.add(title)) {
                writer = fileOutputAssistant.getWriterForGraph(title, true);
            } else {
                writer = fileOutputAssistant.getWriterForGraph(title, false);
            }

            String fileName = fileOutputAssistant.clipFileName(fileOutputAssistant.escape(title), ".grp");
            logger.log("Writing to output/graph/" + fileName);
        } catch (IOException e) {
            logger.logException(e);
        } finally {
            return writer;
        }
    }

    @Override
    public void write(Node node) {
        Writer file = getWriter(node.name);
        if(file == null){
            logger.logWarning("Unable to get writer for " + node.toStringWithoutChildren());
            return;
        }
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

    @Override
    public void finalize() {
        // no op
    }

    private void tryToClose(Writer nodes) {
        try {
            nodes.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

