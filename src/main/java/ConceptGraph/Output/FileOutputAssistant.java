package ConceptGraph.Output;

import ConceptGraph.Output.Logging.DefaultLogger;
import ConceptGraph.Output.Logging.Logger;
import ConceptGraph.Utilities.StringHasher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by danco on 29/11/2015.
 */
public class FileOutputAssistant implements FileOutput {
    public static final String OUTPUT_PATH = "output";
    private static final String GRAPH_OUTPUT_SUB_PATH = "graph";
    private Logger logger;
    private HashMap<String, String> asciiMap;
    private int WINDOWS_MAX_FILENAME_LENGTH = 255;

    public FileOutputAssistant() {
        logger = new DefaultLogger();
        getAsciiMap();
    }

    private void getAsciiMap() {
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

    @Override
    public FileWriter getWriter(String fileName) throws IOException {
        makeDirectoryIfNotExists(OUTPUT_PATH);
        return new FileWriter(OUTPUT_PATH + "/" + escape(fileName));
    }


    public FileOutputStream getFileOutputStream(String fileName) throws IOException {
        makeDirectoryIfNotExists(OUTPUT_PATH);
        return new FileOutputStream(OUTPUT_PATH + "/" + escape(fileName));
    }

    public FileWriter getWriterForGraph(String fileName, boolean append) throws IOException {
        makeDirectoryIfNotExists(OUTPUT_PATH);
        makeDirectoryIfNotExists(OUTPUT_PATH + "/" + GRAPH_OUTPUT_SUB_PATH);
        return new FileWriter(OUTPUT_PATH + "/" + GRAPH_OUTPUT_SUB_PATH + "/" + clipFileName(escape(fileName), ".grp"), append);
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
        input = input.replace(" ", getAscii(" "));
        input = input.replace("?", getAscii("?"));
        input = input.replace("*", getAscii("*"));
        input = input.replace("\"", getAscii("\""));
        input = input.replace("\\", getAscii("\\"));
        input = input.replace(":", getAscii(":"));
        input = input.replace("|", getAscii("|"));
        input = input.replace(">", getAscii(">"));
        input = input.replace("<", getAscii("<"));
        input = input.replace("/", getAscii("/"));
        return input;
    }

    private String getAscii(String key) {
        return asciiMap.get(key);
    }

}
