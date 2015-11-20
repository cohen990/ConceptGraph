package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Timer {
    private long startTime;
    private long endTime;

    public Timer() {
    }

    public static Timer startNew() {
        Timer timer = new Timer();
        timer.start();
        return timer;
    }

    public void start(){
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public double elapsedSeconds() {
        return (endTime - startTime) / 1000.0;
    }
}