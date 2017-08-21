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
    public void newProject(ActionEvent actionEvent) throws IOException {
        try {
            File parentDir = FilePrompt.openDirectory(getStage());

            if (parentDir != null) {
                if (parentDir.exists()) {
                    File projectDir = FilePrompt.newDirectory(parentDir);

                    if (projectDir != null && projectDir.exists()) {
                        loadProjectScene(projectDir);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        actionEvent.consume();
    }

    public void openProject(ActionEvent actionEvent) {
        File parentDir = FilePrompt.openDirectory(getStage());

        if (parentDir != null) {
            if (parentDir.exists()) {
                loadProjectScene(parentDir);
            }
        }

        actionEvent.consume();
    }

    private void loadProjectScene(File rootDir) {
        Stage stage = getStage();

        FileSystemTreeViewFactory.build(SProjectDirector.getInstance().getController().getFileSystemTreeView(), rootDir);
        stage.setScene(SProjectDirector.getInstance().getScene());

    }
}
