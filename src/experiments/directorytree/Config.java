package experiments.directorytree;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private enum State { NOT_LOADED, LOADED }
    private static Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    private volatile State state;
    private Path configDirPath;
    private Properties toggleProperties;

    //default config path to experiments/directorytree/config
    private Config() {
        this.configDirPath = Paths.get("src/simple3d/resources/experiments/directorytree/config");
        this.state = State.NOT_LOADED;
        try {
            ensureLoaded();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public State getState() {
        return state;
    }

    public Path getConfigDirPath() {
        return configDirPath;
    }

    //returns false if key is not present
    public boolean getToggle(String key) {
        boolean boolValue = false;

        if (toggleProperties.containsKey(key)) {
            String value = toggleProperties.getProperty(key).trim();
            boolValue = Boolean.parseBoolean(value);
        }

        return boolValue;
    }

    public boolean isLoaded() {
        return state == State.LOADED;
    }

    private synchronized void ensureLoaded() throws IOException {
        if (!isLoaded()) {
            this.toggleProperties = loadProperties("toggles.properties");
            this.state = State.LOADED;
        }
    }

    private Properties loadProperties(String relativePropertiesPath) throws IOException {
        Properties properties;
        Path propertiesPath = configDirPath.resolve(relativePropertiesPath);
        File propertiesFile = propertiesPath.toFile();

        if (propertiesFile.exists()) {
            InputStream inputStream = null;
            properties = new Properties();

            try {
                inputStream = new FileInputStream(propertiesFile);
                // load a properties file
                properties.load(inputStream);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            FileNotFoundException fileNotFoundException = new FileNotFoundException(propertiesFile.getAbsolutePath());
            throw fileNotFoundException;
        }

        return properties;
    }
}
