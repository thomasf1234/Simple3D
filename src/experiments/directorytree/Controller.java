package experiments.directorytree;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.prompt.DirectoryPrompt;
import experiments.directorytree.threads.FileSystemWatcher;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created by tfisher on 07/08/2017.
 */
public class Controller {
    @FXML public BorderPane borderPane;
    @FXML public FileSystemTreeView fileSystemTreeView;
    private FileSystemWatcher fileSystemWatcher;

    //initialize() must be public
    public void initialize() {
        //start the file system watcher
        fileSystemWatcher = new FileSystemWatcher("fsw", fileSystemTreeView);
        fileSystemWatcher.start();
    }

    //finish() will run necessary clean up
    public void finish() {
        SLogger.getInstance().log("Finishing fileSystemWatcher");
        fileSystemWatcher.finish();
    }

    public void newProject(ActionEvent actionEvent) {
        File choice = DirectoryPrompt.open(getWindow());

        if (choice != null) {
            TextInputDialog dialog = new TextInputDialog("MyProject");
            dialog.setTitle("Dialog Title");
            dialog.setHeaderText("New Project name");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String projectName = result.get().trim();

                Path projectPath = Paths.get(choice.getAbsolutePath(), projectName);
                File projectDir = projectPath.toFile();

                if (projectDir.exists()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    String errorMessage = String.format("Directory already exists '%s'.", projectDir.getAbsolutePath());
                    alert.setHeaderText("Error");
                    alert.setContentText(errorMessage);

                    alert.showAndWait();

                } else {
                    Util.createDirectory(projectDir);
                    FileSystemTreeViewFactory.build(fileSystemTreeView, projectDir);
                }
            }
        }
    }

    public void openProject(ActionEvent actionEvent) {
        File choice = DirectoryPrompt.open(getWindow());

        if (choice != null) {
            FileSystemTreeViewFactory.build(fileSystemTreeView, choice);
        }
    }

    private Window getWindow() {
        return borderPane.getScene().getWindow();
    }
}
