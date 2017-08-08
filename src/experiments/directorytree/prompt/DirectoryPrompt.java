package experiments.directorytree.prompt;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created by tfisher on 08/08/2017.
 */
public class DirectoryPrompt {
    public static File open(Window window) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File choice = directoryChooser.showDialog(window);

        //User cancelled dialog
        if (choice == null) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText("Could not open directory");
//            alert.setContentText("No directory chosen.");
//
//            alert.showAndWait();
        } else if (!choice.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not open directory");
            alert.setContentText("The file is not a directory.");

            alert.showAndWait();
        } else {
            if (choice.exists()) {
                return choice;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Could not open directory");
                alert.setContentText("Unknown error: DirectoryChooser yielded non-null result");

                alert.showAndWait();
                throw new RuntimeException("Unknown error: DirectoryChooser yielded non-null result");
            }
        }

        return null;
    }
}
