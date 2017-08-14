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
    //returns null if user cancelled dialog
    public static File open(Window window) {
        File returnValue;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File choice = directoryChooser.showDialog(window);


        if (choice == null) {
            //User cancelled dialog
            returnValue = null;
        } else {
            if (choice.exists()) {
                if (choice.isDirectory()) {
                    //valid choice
                    returnValue = choice;
                } else {
                    returnValue = null;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Could not open directory");
                    alert.setContentText(String.format("%s is not a directory.", choice));
                    alert.showAndWait();
                }
            } else {
                returnValue = null;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Could not open directory");
                alert.setContentText(String.format("%s no longer exists", choice));
                alert.showAndWait();
            }
        }
        return returnValue;
    }
}
