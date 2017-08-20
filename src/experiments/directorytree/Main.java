package experiments.directorytree;

/**
 * Created by tfisher on 04/08/2017.
 */

import experiments.directorytree.singletons.SConfig;
import experiments.directorytree.singletons.SImages;
import experiments.directorytree.singletons.SLogger;
import experiments.directorytree.singletons.directors.SProjectDirector;
import experiments.directorytree.singletons.directors.STitleDirector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm
//http://o7planning.org/en/11147/javafx-treeview-tutorial
//https://stackoverflow.com/questions/27894108/how-do-i-make-a-mouse-click-event-be-acknowledged-by-a-treeitem-in-a-treeview
//icon size should be 16x16
//https://stackoverflow.com/questions/27894108/how-do-i-make-a-mouse-click-event-be-acknowledged-by-a-treeitem-in-a-treeview
//https://stackoverflow.com/questions/15792090/javafx-treeview-item-action-event
//https://gist.github.com/jewelsea/2305098
//http://www.java2s.com/Code/Java/JavaFX/Stagecloseevent.htm

//
//       updateItem
//
//        protected void updateItem(T item, boolean empty)
//
//
//        The updateItem method should not be called by developers, but it is the best method for developers to override
//        to allow for them to customise the visuals of the cell. To clarify, developers should never call this method
//        in their code (they should leave it up to the UI control, such as the ListView control) to call this method.
//        However, the purpose of having the updateItem method is so that developers, when specifying custom cell
//        factories (again, like the ListView cell factory), the updateItem method can be overridden to allow for
//        complete customisation of the cell.
//
//        It is very important that subclasses of Cell override the updateItem method properly, as failure to do so will
//        lead to issues such as blank cells or cells with unexpected content appearing within them. Here is an example
//        of how to properly override the updateItem method:
//
//        protected void updateItem(T item, boolean empty) {
//          super.updateItem(item, empty);
//
//          if (empty || item == null) {
//            setText(null);
//            setGraphic(null);
//          } else {
//          setText(item.toString());
//          }
//        }
//
//
//        Note in this code sample two important points:
//        We call the super.updateItem(T, boolean) method. If this is not done, the item and empty properties are not
//        correctly set, and you are likely to end up with graphical issues. We test for the empty condition, and if
//        true, we set the text and graphic properties to null. If we do not do this, it is almost guaranteed that end
//        users will see graphical artifacts in cells unexpectedly.
//
//
//        Parameters:
//        item - The new item for the cell.
//        empty - whether or not this cell represents data from the list. If it is empty, then it does not represent any
//        domain data, but is a cell being used to render an "empty" row.
//

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = STitleDirector.getInstance().getScene();
        primaryStage.setTitle("Simple3D");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        SLogger.getInstance().log("Stage is closing");
        SProjectDirector.getInstance().finish();
        STitleDirector.getInstance().finish();
    }

    public static void main(String[] args) throws IOException {
        initSingletons();
        launch(args);
    }

    private static void initSingletons() throws IOException {
        SLogger.init();
        SConfig.init();
        SImages.init();
        STitleDirector.init();
        SProjectDirector.init();
    }

}

