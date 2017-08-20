package experiments.directorytree.controllers;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.prompt.FilePrompt;
import experiments.directorytree.singletons.SLogger;
import experiments.directorytree.threads.FileSystemWatcher;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

/**
 * Created by tfisher on 07/08/2017.
 */
public class ProjectController extends Controller {
    @FXML public BorderPane borderPane;
    @FXML public FileSystemTreeView fileSystemTreeView;

    public void newProject(ActionEvent actionEvent) throws IOException {
        File parentDir = FilePrompt.openDirectory(getWindow());

        if (parentDir != null) {
            if (parentDir.exists()) {
                File projectDir = FilePrompt.newDirectory(parentDir);

                if (projectDir != null && projectDir.exists()) {
                    FileSystemTreeViewFactory.build(fileSystemTreeView, projectDir);
                }
            }
        }
    }

    public void openProject(ActionEvent actionEvent) {
        File choice = FilePrompt.openDirectory(getWindow());

        if (choice != null) {
            FileSystemTreeViewFactory.build(fileSystemTreeView, choice);
        }
    }

    public FileSystemTreeView getFileSystemTreeView() {
        return fileSystemTreeView;
    }

    private Window getWindow() {
        return borderPane.getScene().getWindow();
    }
}
