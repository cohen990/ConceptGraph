package ConceptGraph.Output;

import ConceptGraph.Output.Logging.DefaultLogger;
import ConceptGraph.Output.Logging.Logger;
import ConceptGraph.Utilities.StringHasher;

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
    private int WINDOWS_MAX_FILENAME_LENGTH = 255;

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
        return new FileWriter(OUTPUT_PATH + "/" + GRAPH_OUTPUT_SUBPATH + "/" + clipFileName(escape(fileName), ".grp"), append);
    }

    public String clipFileName(String fileName, String extension) {
        if(isLongerThanMaxFileNameLength(fileName + extension)){
            fileName = replaceEndOfFileNameWithHash(fileName, extension);
        }

        return fileName + extension;
    }

    private boolean isLongerThanMaxFileNameLength(String fileName){
         return (fileName.length() - WINDOWS_MAX_FILENAME_LENGTH > 0);
    }

    private String replaceEndOfFileNameWithHash(String fileName, String extension) {
        String hash = Integer.toString(StringHasher.simpleHash(fileName + extension));
        int positionToCutTo = WINDOWS_MAX_FILENAME_LENGTH - extension.length() - hash.length();
        fileName = fileName.substring(0, positionToCutTo);
        fileName += hash;
        return fileName;
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
        input = input.replace("/", asciiMap.get("/"));
        return input;
    }

}
