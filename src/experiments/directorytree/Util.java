package experiments.directorytree;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by tfisher on 07/08/2017.
 */
public class Util {
    //Returns a TreeItem representation of the specified directory
    public static TreeItem<Path> getNodesForDirectory(File directory) {
        Path dirPath = directory.toPath();
        TreeItem<Path> root = new TreeItem<Path>(dirPath);
        for (File file : directory.listFiles()) {
            if (!file.isHidden()) {
                if (file.isDirectory()) {
                    //Then we call the function recursively
                    root.getChildren().add(getNodesForDirectory(file));
                } else {
                    TreeItem treeItem = new TreeItem<Path>(file.toPath());
                    root.getChildren().add(treeItem);
                }
            }
        }
        return root;
    }
}
