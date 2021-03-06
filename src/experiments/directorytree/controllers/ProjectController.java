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
    @FXML public FileSystemTreeView fileSystemTreeView;

    public void newProject(ActionEvent actionEvent) throws IOException {
        File parentDir = FilePrompt.openDirectory(getStage());

        if (parentDir != null) {
            if (parentDir.exists()) {
                File projectDir = FilePrompt.newDirectory(parentDir);

                if (projectDir != null && projectDir.exists()) {
                    FileSystemTreeViewFactory.build(fileSystemTreeView, projectDir);
                }
            }
        }

        actionEvent.consume();
    }

    public void openProject(ActionEvent actionEvent) {
        File choice = FilePrompt.openDirectory(getStage());

        if (choice != null) {
            FileSystemTreeViewFactory.build(fileSystemTreeView, choice);
        }

        actionEvent.consume();
    }

    public FileSystemTreeView getFileSystemTreeView() {
        return fileSystemTreeView;
    }
}
