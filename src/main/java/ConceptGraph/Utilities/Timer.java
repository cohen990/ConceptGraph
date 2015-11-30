package ConceptGraph.Utilities;

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