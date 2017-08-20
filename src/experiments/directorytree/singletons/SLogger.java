package experiments.directorytree.singletons;

import java.io.IOException;

//SingletonLogger
public class SLogger extends Loader {
    private static SLogger ourInstance;

    public static SLogger getInstance(){
        return ourInstance;
    }

    private SLogger() {}

    @Override
    protected void load() throws IOException {

    }

    public static synchronized void init() throws IOException {
        if(ourInstance == null) {
            ourInstance = new SLogger();
            ourInstance.ensureLoaded();
        }
    }

    public synchronized void log(String message) {
        SConfig config = SConfig.getInstance();
        boolean debug = config.getToggle("debug");

        if (debug) { System.out.println(message); }
    }
}
