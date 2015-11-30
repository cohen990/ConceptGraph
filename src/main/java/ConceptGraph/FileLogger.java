package ConceptGraph;


import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLogger extends DefaultLogger {
    private final FileWriter file;
    private static String logFileName = "log.txt";
    private final FileOutputAssistant fileOutputAssistant;

    private FileLogger() throws IOException {
        this.fileOutputAssistant = new FileOutputAssistant();
        this.file = fileOutputAssistant.getWriter(logFileName);
    }

    public static Logger Create(){
        try {
            return new FileLogger();
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
