package experiments.directorytree;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;

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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File choice = directoryChooser.showDialog(getWindow());

        if (choice == null || !choice.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not open directory");
            alert.setContentText("The file is invalid.");

            alert.showAndWait();
        } else {
            FileSystemTreeViewFactory.build(contextMenuTreeView, choice);
        }
    }

    private Window getWindow() {
        return borderPane.getScene().getWindow();
    }
}
