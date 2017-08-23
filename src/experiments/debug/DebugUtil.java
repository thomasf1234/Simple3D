package experiments.debug;

import experiments.debug.controllers.RootController;
import experiments.debug.factories.SceneGraphTreeViewFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DebugUtil {
    public void launchDebugStage(Node node) throws IOException {
        String fxmlPath = "/experiments/debug/root.fxml";
        // Create the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader();
        // Path to the FXML File
        URL fxmlDocPath = getClass().getResource(fxmlPath);
        fxmlLoader.setLocation(fxmlDocPath);
        // Create the Pane and all Details
        BorderPane borderPane = fxmlLoader.load();
        //load must be called before controller is initialized
        RootController rootController = fxmlLoader.getController();


        // creating and initializing the scene.
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(scene);

        SceneGraphTreeViewFactory.build(rootController.nodeTreeView, rootController.debugTextArea, node);
        stage.show();
    }
}
