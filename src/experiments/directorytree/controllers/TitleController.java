package experiments.directorytree.controllers;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.prompt.FilePrompt;
import experiments.directorytree.singletons.directors.SProjectDirector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TitleController extends Controller {
    @FXML
    public BorderPane titleBorderPane;

    public void newProject(ActionEvent actionEvent) throws IOException {
        try {
            File parentDir = FilePrompt.openDirectory(getStage());

            if (parentDir != null) {
                if (parentDir.exists()) {
                    File projectDir = FilePrompt.newDirectory(parentDir);

                    if (projectDir != null && projectDir.exists()) {
                        FileSystemTreeViewFactory.build(SProjectDirector.getInstance().getController().getFileSystemTreeView(), projectDir);
                    }
                }
            }
            getStage().setScene(SProjectDirector.getInstance().getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProject(ActionEvent actionEvent) {
        File parentDir = FilePrompt.openDirectory(getStage());

        if (parentDir != null) {
            if (parentDir.exists()) {
                FileSystemTreeViewFactory.build(SProjectDirector.getInstance().getController().getFileSystemTreeView(), parentDir);
                getStage().setScene(SProjectDirector.getInstance().getScene());
            }
        }
    }

    private Stage getStage() {
        return (Stage) titleBorderPane.getScene().getWindow();
    }
}
