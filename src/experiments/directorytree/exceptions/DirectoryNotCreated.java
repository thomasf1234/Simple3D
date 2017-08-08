package experiments.directorytree.exceptions;

/**
 * Created by tfisher on 08/08/2017.
 */
public class DirectoryNotCreated extends RuntimeException {
    private String dirPath;

    public DirectoryNotCreated(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getDirPath() {
        return dirPath;
    }
}
