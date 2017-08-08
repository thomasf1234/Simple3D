package experiments.directorytree;

import experiments.directorytree.exceptions.DirectoryNotCreated;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
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

    public static void createDirectory(File directory) throws DirectoryNotCreated {
        boolean successful = directory.mkdir();

        if (successful) {
            if (directory.exists()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                String alertMessage = String.format("Successfully created project at '%s'.", directory.getAbsolutePath());
                alert.setHeaderText("Success");
                alert.setContentText(alertMessage);

                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                String errorMessage = String.format("Failed trying create directory '%s'.", directory.getAbsolutePath());
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);

                alert.showAndWait();
                throw new DirectoryNotCreated(directory.getAbsolutePath());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage = String.format("Failed trying to create directory '%s'.", directory.getAbsolutePath());
            alert.setHeaderText("Error");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            throw new DirectoryNotCreated(directory.getAbsolutePath());
        }

    }
}
