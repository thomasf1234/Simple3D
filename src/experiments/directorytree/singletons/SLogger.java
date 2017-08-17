package experiments.directorytree.singletons;

//SingletonLogger
public class SLogger {
    private static SLogger ourInstance = new SLogger();

    public static SLogger getInstance() {
        return ourInstance;
    }

    private SLogger() {}

    public synchronized void log(String message) {
        SConfig config = SConfig.getInstance();
        boolean debug = config.getToggle("debug");

        if (debug) { System.out.println(message); }
    }
}
