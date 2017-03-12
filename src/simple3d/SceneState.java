package simple3d;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Created by tfisher on 07/03/2017.
 */
public abstract class SceneState {
    protected SimpleScene simpleScene;
    protected SceneState nextSceneState;

    public SceneState(SimpleScene simpleScene) {
        this.simpleScene = simpleScene;
        this.nextSceneState = this;
    }

    public abstract void onKeyPressed(KeyEvent event);
    public abstract void onScroll(ScrollEvent event);
    public abstract void onMouseClick(MouseEvent event);
    public abstract void onMouseMove(MouseEvent event);
    public abstract void onMouseDrag(MouseEvent event, double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew);

    public SceneState getNextSceneState() {
        return nextSceneState;
    }

    public void setNextSceneState(SceneState nextSceneState) {
        this.nextSceneState = nextSceneState;
    }
}
