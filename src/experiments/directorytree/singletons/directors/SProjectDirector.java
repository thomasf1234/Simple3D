package experiments.directorytree.singletons.directors;

import experiments.directorytree.controllers.ProjectController;
import experiments.directorytree.singletons.SLogger;
import experiments.directorytree.threads.FileSystemWatcher;
import experiments.directorytree.tree_views.FileSystemTreeView;

import java.io.IOException;

public class SProjectDirector extends Director {
    private static SProjectDirector ourInstance;

    public static SProjectDirector getInstance() {
        return ourInstance;
    }

    public static synchronized void init() throws IOException {
        if (ourInstance == null) {
            ourInstance = new SProjectDirector();
            ourInstance.ensureLoaded();
        }
    }
    private FileSystemWatcher fileSystemWatcher;

    public SProjectDirector() {
        super("/experiments/directorytree/project.fxml");
    }

    @Override
    protected void load() throws IOException {
        super.load();
        FileSystemTreeView fileSystemTreeView = getController().getFileSystemTreeView();
        fileSystemWatcher = new FileSystemWatcher("fsw", fileSystemTreeView);
        fileSystemWatcher.start();
    }

    @Override
    public ProjectController getController() {
        return (ProjectController) super.getController();
    }

    @Override
    public void finish() {
        super.finish();
        SLogger.getInstance().log("Finishing fileSystemWatcher");
        fileSystemWatcher.finish();
    }
}
