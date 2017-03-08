package simple3d.scene_states;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.MeshView;
import simple3d.CameraMan;
import simple3d.Director;
import simple3d.SceneState;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Selected extends SceneState {
    private Director director;
    private MeshView selectedMeshView;

    public Selected(Director director, MeshView selectedMeshView) {
        this.director = director;
        this.selectedMeshView = selectedMeshView;
    }

    public void onScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        selectedMeshView.setScaleX(selectedMeshView.getScaleX() + deltaY);
        selectedMeshView.setScaleY(selectedMeshView.getScaleY() + deltaY);
        selectedMeshView.setScaleZ(selectedMeshView.getScaleZ() + deltaY);
    }

    public SceneState onMouseClick(MouseEvent event) {
        return new Default(director);
    }

    public void onMouseMove(MouseEvent event) {
        Point2D subSceneCenter = director.getSubSceneAbsoluteCenter2D();
        CameraMan cameraMan = director.getCameraMan();

        Point3D newPos = cameraMan.getPosition().add(cameraMan.getForward().multiply(-(cameraMan.getY()/cameraMan.getForward().getY())));

        newPos = newPos.add(cameraMan.getRight().multiply(event.getSceneX() - subSceneCenter.getX()).multiply(0.2));
        newPos = newPos.add(cameraMan.getUp().multiply(- event.getSceneY() + subSceneCenter.getY()).multiply(0.2));

        selectedMeshView.setTranslateX(newPos.getX());
        selectedMeshView.setTranslateY(newPos.getY());
        selectedMeshView.setTranslateZ(newPos.getZ());
    }

    public void onMouseDrag(double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {

    }

}
