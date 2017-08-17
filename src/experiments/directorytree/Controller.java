package experiments.directorytree;

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

    private Window getWindow() {
        return borderPane.getScene().getWindow();
    }
}
