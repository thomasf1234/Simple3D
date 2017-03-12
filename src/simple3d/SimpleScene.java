package simple3d;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Created by tfisher on 12/03/2017.
 */
public class SimpleScene extends Scene {
    private BorderPane pane;

    public SimpleScene(BorderPane pane) {
        super(pane);
        this.pane = pane;
    }

    public BorderPane getPane() {
        return pane;
    }
}
