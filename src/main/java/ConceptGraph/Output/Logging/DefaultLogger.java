package ConceptGraph.Output.Logging;


import ConceptGraph.Utilities.Timer;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultLogger implements Logger {

    public void logDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        log(dateFormat.format(date)); //2014/08/06 15:59:48
    }

    public void log(String message) {
        log(message, false);
    }

    public void log(String message, boolean withNewLine) {
        try{
            System.out.println(message + System.getProperty("line.separator") + (withNewLine ? System.getProperty("line.separator") : ""));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void logTimeElapsed(String actionCompleted, Timer timer, boolean withNewLine) {
        log(actionCompleted + " in " + timer.elapsedSeconds() + " seconds", withNewLine);
    }

    public void logMemUsage() {
        log("Mem usage: " +
                NumberFormat.getNumberInstance(Locale.US).format(
                        (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                                / 1024) + "KB");
    }

    public void logException(Exception e){
        e.printStackTrace();
    }

    public void logWarning(String warning) {
        log("!!! --> " + warning, true);
    }
}
