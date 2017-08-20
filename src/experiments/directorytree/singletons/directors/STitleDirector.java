package experiments.directorytree.singletons.directors;

import experiments.directorytree.controllers.TitleController;
import experiments.directorytree.threads.FileSystemWatcher;

import java.io.IOException;

public class STitleDirector extends Director {
    private static STitleDirector ourInstance;

    public static STitleDirector getInstance() {
        return ourInstance;
    }

    public static synchronized void init() throws IOException {
        if (ourInstance == null) {
            ourInstance = new STitleDirector();
            ourInstance.ensureLoaded();
        }
    }

    public STitleDirector() {
        super("/experiments/directorytree/title.fxml");
    }

    @Override
    public TitleController getController() {
        return (TitleController) super.getController();
    }
}
