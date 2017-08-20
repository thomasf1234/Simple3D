package experiments.directorytree.singletons.directors;

import experiments.directorytree.controllers.Controller;
import experiments.directorytree.singletons.Loader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class Director extends Loader {
    private String fxmlPath;
    private FXMLLoader fxmlLoader;
    private Controller controller;
    private Scene scene;

    public Director(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public Controller getController() {
        return controller;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public Scene getScene() {
        return scene;
    }

    public void finish() {

    }

    @Override
    protected void load() throws IOException {
        // Create the FXMLLoader
        this.fxmlLoader = new FXMLLoader();
        // Path to the FXML File
        URL fxmlDocPath = getClass().getResource(fxmlPath);
        fxmlLoader.setLocation(fxmlDocPath);
        // Create the Pane and all Details
        Parent root = fxmlLoader.load();
        //load must be called before controller is initialized
        this.controller = fxmlLoader.getController();

        this.scene = new Scene(root, 400, 400);
    }
}
