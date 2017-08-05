package experiments.demofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class TitleController {
    private Stage primaryStage;

    private void initialize() {
    }

    public void newProject(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        // Path to the FXML File
//        URL fxmlDocPath = getClass().getResource("/experiments/demofxml/subSample.fxml");
        URL fxmlDocPath = getClass().getResource("/experiments/demofxml/subSample.fxml");
        fxmlLoader.setLocation(fxmlDocPath);
        // Create the Pane and all Details
        BorderPane root = null;
        try {
            root = (BorderPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //load must be called before controller is initialized
        primaryStage.setTitle("Simple3D");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    public void openProject(ActionEvent actionEvent) {
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
