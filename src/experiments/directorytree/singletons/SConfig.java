package experiments.directorytree.singletons;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
//http://fxexperience.com/2009/07/free-icons-for-your-javafx-applications/
public class SConfig extends SLoader {
    private static SConfig ourInstance;

    public static SConfig getInstance() {
        return ourInstance;
    }

    private Path configDirPath;
    private Properties toggleProperties;

    //default config path to experiments/directorytree/config
    private SConfig() {
        super();
        this.configDirPath = Paths.get("src/simple3d/resources/experiments/directorytree/config");
    }

    @Override
    protected void load() throws IOException {
        this.toggleProperties = loadProperties("toggles.properties");
    }

    //initializes ourInstance. Multiple calls have no effect.
    public static synchronized void init() throws IOException {
        if (ourInstance == null) {
            ourInstance = new SConfig();
            ourInstance.ensureLoaded();
        }
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
