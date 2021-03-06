package experiments.demofxml;

//http://o7planning.org/en/11055/javafx-accordion-tutorial

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.net.URL;

//https://examples.javacodegeeks.com/desktop-java/javafx/fxml/javafx-fxml-tutorial/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Create the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader();
        // Path to the FXML File
        URL fxmlDocPath = getClass().getResource("/experiments/demofxml/title.fxml");
        fxmlLoader.setLocation(fxmlDocPath);
        // Create the Pane and all Details
        BorderPane root = (BorderPane) fxmlLoader.load();
        //load must be called before controller is initialized
        TitleController titleController  = fxmlLoader.getController();
        titleController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Simple3D - Projects");
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
