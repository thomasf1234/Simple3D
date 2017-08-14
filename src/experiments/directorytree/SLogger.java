package experiments.directorytree;

//SingletonLogger
public class SLogger {
    private static SLogger ourInstance = new SLogger();

    public static SLogger getInstance() {
        return ourInstance;
    }

    private SLogger() {}

    public synchronized void log(String message) {
        Config config = Config.getInstance();
        boolean debug = config.getToggle("debug");

        if (debug) { System.out.println(message); }
    }
}
