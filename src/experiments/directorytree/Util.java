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
