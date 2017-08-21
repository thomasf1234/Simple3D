package experiments.notepad;

public class StopWatch {
    public long t0;
    public boolean running;

    public StopWatch() {
        this.running = false;
    }

    public synchronized void start() {
        this.running = true;
        t0 = t();
    }

    public double getElapsedTime() {
        return (t() - t0)/1000.0;
    }

    private long t() {
        return System.currentTimeMillis();
    }
}
