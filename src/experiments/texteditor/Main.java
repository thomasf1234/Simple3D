package experiments.texteditor;

import experiments.debug.DebugUtil;
import experiments.directorytree.singletons.SConfig;
import experiments.directorytree.singletons.SImages;
import experiments.directorytree.singletons.SLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

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

            Scene scene = new Scene(borderPane);

            primaryStage.setScene(scene);
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
            primaryStage.setTitle("Untitled - TextEditor");
            primaryStage.show();

            if (SConfig.getInstance().getToggle("debug")) {
                DebugUtil debugUtil = new DebugUtil();
                debugUtil.launchDebugStage(scene.getRoot());
            }
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