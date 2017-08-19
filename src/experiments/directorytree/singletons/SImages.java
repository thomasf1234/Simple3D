package experiments.directorytree.singletons;

import experiments.directorytree.Util;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//http://fxexperience.com/2009/07/free-icons-for-your-javafx-applications/
public class SImages extends SLoader {
    private static SImages ourInstance;

    public static SImages getInstance() {
        return ourInstance;
    }

    private Path imagesDirPath;
    private Map<String,Image> imageHashMap;

    //initializes ourInstance. Multiple calls have no effect.
    public static synchronized void init() throws IOException {
        if (ourInstance == null) {
            ourInstance = new SImages();
            ourInstance.ensureLoaded();
        }
    }

    //default config path to experiments/directorytree/config
    private SImages() {
        super();
        this.imagesDirPath = Paths.get("src/simple3d/resources/experiments/directorytree/images");
    }

    @Override
    protected void load() throws IOException {
        this.imageHashMap = new HashMap<String, Image>();
        loadImages(imagesDirPath.toFile());
    }

    public Path getImagesDirPath() {
        return imagesDirPath;
    }

    //returns null if key is not present
    public Image getImage(String key) {
        Image image = null;

        if (imageHashMap.containsKey(key)) {
            image = imageHashMap.get(key);
        }

        return image;
    }

    private void loadImages(File directory) throws FileNotFoundException {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                loadImages(file);
            } else {
                loadImage(file);
            }
        }
    }

    private void loadImage(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        Image image = new Image(fis);
        imageHashMap.put(file.getName(), image);
    }
}
