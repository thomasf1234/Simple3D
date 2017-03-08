package simple3d;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Created by tfisher on 07/03/2017.
 */
public abstract class SceneState {
    public abstract void onScroll(ScrollEvent event);
    public abstract void onMouseClick(MouseEvent event);
    public abstract void onMouseMove(MouseEvent event);
    public abstract void onMouseDrag(double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew);
}
