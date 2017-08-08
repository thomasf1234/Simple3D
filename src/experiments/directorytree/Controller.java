package experiments.directorytree;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.prompt.DirectoryPrompt;
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
    @FXML public ContextMenuTreeView<Path> contextMenuTreeView;

    private void initialize() {

    }

    public ContextMenuTreeView<Path> getContextMenuTreeView() {
        return contextMenuTreeView;
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
                    FileSystemTreeViewFactory.build(contextMenuTreeView, projectDir);
                }
            }
        }
    }

    private Window getWindow() {
        return borderPane.getScene().getWindow();
    }

    public void openProject(ActionEvent actionEvent) {
        File choice = DirectoryPrompt.open(getWindow());

        if (choice != null) {
            FileSystemTreeViewFactory.build(contextMenuTreeView, choice);
        }
    }
}
