package ConceptGraph.Output.Logging;

import ConceptGraph.Utilities.Timer;

/**
 * Created by danco on 29/11/2015.
 */
public interface Logger {
    void logDate();

    void log(String message);

    void log(String message, boolean withNewLine);

    void logTimeElapsed(String actionCompleted, Timer timer, boolean withNewLine);

    void logMemUsage();

    void logException(Exception e);

    void logWarning(String warning);
}

