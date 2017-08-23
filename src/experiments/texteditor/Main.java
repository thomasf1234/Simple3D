package experiments.texteditor;

import java.io.IOException;

import experiments.debug.DebugUtil;
import experiments.directorytree.singletons.SConfig;
import experiments.directorytree.singletons.SImages;
import experiments.directorytree.singletons.SLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;

//https://stackoverflow.com/questions/17192770/check-given-file-is-simple-text-file-using-java
//javac TestClass.java
//java TestClass
//http://www.skylit.com/javamethods/faqs/createjar.html
//https://stackoverflow.com/questions/13200439/javafx2-2-making-a-scrollbar-with-transparent-background

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        initSingletons();

        try {
            // loading the fxml
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(
                    "/experiments/texteditor/root.fxml"));
            // creating and initializing the scene.
            Scene scene = new Scene(borderPane);
            LineCountTextArea editableArea = (LineCountTextArea) borderPane.getCenter();
            TextArea lineCountTextArea = (TextArea) borderPane.getLeft();

            //synchronize scrollbars for both TextArea
            editableArea.scrollTopProperty().bindBidirectional(lineCountTextArea.scrollTopProperty());


            primaryStage.setScene(scene);

            // setting the height and width of stage.
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);

            // setting the App title
            primaryStage.setTitle("Untitled - TextEditor");

            scene.getStylesheets().add(this.getClass() .getResource("/experiments/texteditor/stylesheets/textArea.css").toExternalForm());

            // display the stage
            primaryStage.show();

            DebugUtil debugUtil = new DebugUtil();
            debugUtil.launchDebugStage(scene.getRoot());

            editableArea.setLineCountDestTextArea(lineCountTextArea);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void initSingletons() throws IOException {
        SLogger.init();
        SConfig.init();
        SImages.init();
    }
}