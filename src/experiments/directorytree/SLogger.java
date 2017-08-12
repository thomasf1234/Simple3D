package experiments.directorytree;

public class SLogger {
    private static SLogger ourInstance = new SLogger();

    public static SLogger getInstance() {
        return ourInstance;
    }

    public void log(String message) {
        Config config = Config.getInstance();
        boolean debug = config.getToggle("debug");

        if (debug) { System.out.println(message); }
    }
}
