package ConceptGraph.Output;

import ConceptGraph.Output.Logging.DefaultLogger;
import ConceptGraph.Output.Logging.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by danco on 29/11/2015.
 */
public class FileOutputAssistant {
    public static final String OUTPUT_PATH = "output";
    private static final String GRAPH_OUTPUT_SUBPATH = "graph";
    private Logger logger;
    private HashMap<String, String> asciiMap;

    public FileOutputAssistant() {
        logger = new DefaultLogger();
        asciiMap = new HashMap<>();
        asciiMap.put(" ", "#0x20");
        asciiMap.put("#0x20", " ");
        asciiMap.put("\"", "#0x22");
        asciiMap.put("#0x22", "\"");
        asciiMap.put("*", "#0x2A");
        asciiMap.put("#0x2A", "*");
        asciiMap.put("/", "#0x2F");
        asciiMap.put("#0x2F", "/");
        asciiMap.put(":", "#0x3A");
        asciiMap.put("#0x3A", ":");
        asciiMap.put("<", "#0x3C");
        asciiMap.put("#0x3C", "<");
        asciiMap.put(">", "#0x3E");
        asciiMap.put("#0x3E", ">");
        asciiMap.put("?", "#0x3F");
        asciiMap.put("#0x3F", "?");
        asciiMap.put("\\", "#0x5C");
        asciiMap.put("#0x5C", "\\");
        asciiMap.put("|", "#0x7C");
        asciiMap.put("#0x7C", "|");
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
        input = input.replace(" ", asciiMap.get(" "));
        input = input.replace("?", asciiMap.get("?"));
        input = input.replace("*", asciiMap.get("*"));
        input = input.replace("\"",asciiMap.get("\""));
        input = input.replace("\\",asciiMap.get("\\"));
        input = input.replace(":", asciiMap.get(":"));
        input = input.replace("|", asciiMap.get("|"));
        input = input.replace(">", asciiMap.get(">"));
        input = input.replace("<", asciiMap.get("<"));
        return input.replace("/", asciiMap.get("/"));
    }

}
