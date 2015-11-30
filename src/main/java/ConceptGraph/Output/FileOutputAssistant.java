package ConceptGraph.Output;

import ConceptGraph.Output.Logging.DefaultLogger;
import ConceptGraph.Output.Logging.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by danco on 29/11/2015.
 */
public class FileOutputAssistant {
    public static final String OUTPUT_PATH = "output";
    private static final String GRAPH_OUTPUT_SUBPATH = "graph";
    private Logger logger;

    public FileOutputAssistant() {
        logger = new DefaultLogger();
    }

    public FileWriter getWriter(String fileName) throws IOException {
        makeDirectoryIfNotExists(OUTPUT_PATH);
        return new FileWriter(OUTPUT_PATH + "/" + escape(fileName));
    }

    public FileWriter getWriterForGraph(String fileName, boolean append) throws IOException {
        makeDirectoryIfNotExists(OUTPUT_PATH);
        makeDirectoryIfNotExists(OUTPUT_PATH + "/" + GRAPH_OUTPUT_SUBPATH);
        return new FileWriter(OUTPUT_PATH + "/" + GRAPH_OUTPUT_SUBPATH + "/" + escape(fileName) + ".grp", append);
    }

    private void makeDirectoryIfNotExists(String directory) {
        File file = new File(directory);
        if (!file.exists()) {
            if (file.mkdir()) {
                logger.log("The directory \"" + directory + "\" has been created!");
            } else {
                logger.logWarning("Failed to create the directory \"" + directory + "\"");
            }
        }
    }

    public String escape(String input) {
        return input.replace(" ", "_");
    }
}
