package experiments.directorytree;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by tfisher on 07/08/2017.
 */
public class Controller {
    private Stage primaryStage;

    @FXML public ContextMenuTreeView<Path> contextMenuTreeView;

    private void initialize() {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ContextMenuTreeView<Path> getContextMenuTreeView() {
        return contextMenuTreeView;
    }

    public void newProject(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        File choice = dc.showDialog(primaryStage);
        if (choice == null || !choice.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not open directory");
            alert.setContentText("The file is invalid.");

            alert.showAndWait();
        } else {
            FileSystemTreeViewFactory.build(contextMenuTreeView, choice);
        }
    }
}
