package experiments.directorytree;

/**
 * Created by tfisher on 04/08/2017.
 */

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

//http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm
//http://o7planning.org/en/11147/javafx-treeview-tutorial
//https://stackoverflow.com/questions/27894108/how-do-i-make-a-mouse-click-event-be-acknowledged-by-a-treeitem-in-a-treeview
//icon size should be 16x16
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        TreeView<String> a = new TreeView<String>();
        BorderPane b = new BorderPane();
        Button c = new Button("Load Folder");
        c.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setInitialDirectory(new File(System.getProperty("user.home")));
                File choice = dc.showDialog(primaryStage);
                if (choice == null || !choice.isDirectory()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("Could not open directory");
                    alert.setContentText("The file is invalid.");

                    alert.showAndWait();
                } else {
                    a.setRoot(getNodesForDirectory(choice));
                }
            }
        });
        b.setTop(c);
        b.setCenter(a);
        primaryStage.setScene(new Scene(b, 600, 400));
        primaryStage.setTitle("Folder View");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public TreeItem<String> getNodesForDirectory(File directory) { //Returns a TreeItem representation of the specified directory
        TreeItem<String> root = new TreeItem<String>(directory.getName());
        for (File f : directory.listFiles()) {
            System.out.println("Loading " + f.getName());
            if (f.isDirectory()) { //Then we call the function recursively
                root.getChildren().add(getNodesForDirectory(f));
            } else {
                String fileName = f.getName();
                TreeItem treeItem = new TreeItem<String>(fileName);
                root.getChildren().add(treeItem);
            }
        }
        return root;
    }
}