package ConceptGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SingleFileGraphStore extends GraphStore {
    private final FileOutputAssistant fileOutputAssistant;
    private Logger logger;

    public SingleFileGraphStore() {
        logger = FileLogger.Create();
        fileOutputAssistant = new FileOutputAssistant();
    }

    private Writer getWriter(String title) {
        Writer writer = null;
        try {
            if (!Storage.SeenLinks.add(title)) {
                writer = fileOutputAssistant.getWriterForGraph(title, true);
            } else {
                writer = fileOutputAssistant.getWriterForGraph(title, false);
            }

            logger.log("Writing to output/graph/" + fileOutputAssistant.escape(title) + ".grp");
        } catch (IOException e) {
            logger.logException(e);
        } finally {
            return writer;
        }

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

