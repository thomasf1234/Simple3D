package experiments.directorytree.exceptions;

import java.io.IOException;

/**
 * Created by tfisher on 08/08/2017.
 */
public class FileNotCreated extends IOException {
    private String filePath;

    public FileNotCreated(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
