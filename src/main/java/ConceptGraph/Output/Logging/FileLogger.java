package ConceptGraph.Output.Logging;


import ConceptGraph.Output.FileOutputAssistant;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger extends DefaultLogger {
    private final FileWriter file;
    private static String logFileName = "log.txt";
    private final FileOutputAssistant fileOutputAssistant;

    private FileLogger(FileOutputAssistant outputAssistant) throws IOException {
        this.fileOutputAssistant = outputAssistant;
        this.file = fileOutputAssistant.getWriter(logFileName);
    }

    public static Logger Create(FileOutputAssistant outputAssistant){
        try {
            return new FileLogger(outputAssistant);
        } catch (IOException e) {
            Logger logger = new DefaultLogger();

            logger.logException(e);

            logger.logWarning("Unable to create " + FileOutputAssistant.OUTPUT_PATH + logFileName + ". Defaulting to console logging.");

            return logger;
        }
    }

    @Override
    public void log(String message, boolean withNewLine) {
        try{
            file.write(message + System.getProperty("line.separator") + (withNewLine ? System.getProperty("line.separator") : ""));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
