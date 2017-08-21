package experiments.notepad;

import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

//https://stackoverflow.com/questions/17192770/check-given-file-is-simple-text-file-using-java
//javac TestClass.java
//java TestClass
//http://www.skylit.com/javamethods/faqs/createjar.html
//https://stackoverflow.com/questions/13200439/javafx2-2-making-a-scrollbar-with-transparent-background

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {

        try {
            // loading the fxml
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(
                    "/experiments/notepad/notepad.fxml"));
            // creating and initializing the scene.
            Scene scene = new Scene(borderPane);
            LineCountTextArea editableArea = (LineCountTextArea) borderPane.getCenter();
            TextArea lineCountTextArea = (TextArea) borderPane.getLeft();

            //synchronize scrollbars for both TextArea
            editableArea.scrollTopProperty().bindBidirectional(lineCountTextArea.scrollTopProperty());

//            UnaryOperator<TextFormatter.Change> filter = change -> {
//                String text = change.getText();
//
//                System.out.println(change.getText());
//                change.setText(text.replaceAll(String.format("%n"), String.format("%n") + "k"));
//
//                return change;
////                if (text.matches("[0-9]*")) {
////                    return change;
////                }
//
////                return null;
//            };
//            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
//            editableArea.setTextFormatter(textFormatter);


            primaryStage.setScene(scene);

            // setting the height and width of stage.
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);

            // setting the App title
            primaryStage.setTitle("Untitled - Notepad");

            scene.getStylesheets().add(this.getClass() .getResource("/experiments/notepad/stylesheets/textArea.css").toExternalForm());

            // display the stage
            primaryStage.show();

            editableArea.setLineCountDestTextArea(lineCountTextArea);


            for (Node node : editableArea.lookupAll(".scroll-pane"))
            {
                if (node instanceof ScrollPane)
                {
                    ((ScrollPane) node).setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                }
            }

            for (Node node : lineCountTextArea.lookupAll(".scroll-pane"))
            {
                if (node instanceof ScrollPane)
                {
                    ((ScrollPane) node).setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}