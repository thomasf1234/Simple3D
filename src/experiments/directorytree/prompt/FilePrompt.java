package experiments.directorytree.prompt;

import experiments.directorytree.utils.FileUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created by tfisher on 08/08/2017.
 */
public class FilePrompt {
    //returns null if user cancelled dialog
    public static File openDirectory(Window window) {
        File returnValue;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open directory");
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

    public static File newDirectory(File parentDirectory) throws IOException {
        File directory = null;
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Create new directory");
        dialog.setHeaderText("");
        dialog.setContentText("Enter directory name");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String dirName = result.get().trim();

            Path dirPath = Paths.get(parentDirectory.getAbsolutePath(), dirName);
            directory = dirPath.toFile();

            if (directory.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                String errorMessage = String.format("Directory already exists '%s'.", directory.getAbsolutePath());
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);

                alert.showAndWait();

            } else {
                FileUtils.createDirectory(directory);
            }
        }

        return directory;
    }

    public static File newFile(File parentDirectory) throws IOException {
        File file = null;
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Create new file");
        dialog.setHeaderText("");
        dialog.setContentText("Enter file name");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String fileName = result.get().trim();

            Path filePath = Paths.get(parentDirectory.getAbsolutePath(), fileName);
            file = filePath.toFile();

            if (file.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                String errorMessage = String.format("File already exists '%s'.", file.getAbsolutePath());
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);

                alert.showAndWait();

            } else {
                FileUtils.createFile(file);
            }
        }

        return file;
    }
}
