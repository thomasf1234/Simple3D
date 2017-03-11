package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import simple3d.CameraMan;
import simple3d.Director;
import simple3d.SceneState;
import simple3d.Simple3D;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Selected extends SceneState {
    private Director director;
    private MeshView selectedMeshView;

    public Selected(Director director, MeshView selectedMeshView) {
        super();
        this.director = director;
        this.selectedMeshView = selectedMeshView;
        Stage stage = new Stage();
        stage.setX(0);
        stage.setY(0);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Mesh Edit");
        stage.setScene(new Scene(createToolBar()));
        stage.initOwner(director.getPrimaryStage());
        stage.show();
    }


    @Override
    public void onKeyPressed(KeyEvent event) {

    }

    public void onScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if (director.getCameraMan().getTargetDistance() > deltaY) {
            director.getCameraMan().moveForward(deltaY);
        }
    }

    public SceneState onMouseClick(MouseEvent event) {
        SceneState newSceneState = this;
        Node selectedNode = event.getPickResult().getIntersectedNode();
        if(selectedNode == selectedMeshView) {
            if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("Must add right Click functionality");
                //ContextMenu contextMenu = getRightClickContextMenu();
                //contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            }
        } else {
            newSceneState = new Default(director);
        }

        return newSceneState;
    }

    public void onMouseMove(MouseEvent event) {
        director.getSubScene().requestFocus();
    }

    public void onMouseDrag(MouseEvent event, double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {
        if (event.isControlDown()) {
            Point2D subSceneCenter = director.getSubSceneAbsoluteCenter2D();
            CameraMan cameraMan = director.getCameraMan();

            Point3D newPos = cameraMan.getPosition().add(cameraMan.getForward().multiply(-(cameraMan.getY()/cameraMan.getForward().getY())));

            newPos = newPos.add(cameraMan.getRight().multiply(event.getSceneX() - subSceneCenter.getX()).multiply(0.2));
            newPos = newPos.add(cameraMan.getUp().multiply(- event.getSceneY() + subSceneCenter.getY()).multiply(0.2));

            selectedMeshView.setTranslateX(newPos.getX());
            selectedMeshView.setTranslateY(newPos.getY());
            selectedMeshView.setTranslateZ(newPos.getZ());
        }
    }

    private ToolBar createToolBar() {
        Label label = new Label("Selected: " + selectedMeshView.getProperties().get("name"));

        final CheckBox checkBox = new CheckBox("Wireframe");

        if (selectedMeshView == null) {
            checkBox.setSelected(false);
            checkBox.setDisable(true);
        } else {
            checkBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    selectedMeshView.setDrawMode(checkBox.isSelected() ? DrawMode.LINE : DrawMode.FILL);
                }
            });

            if (selectedMeshView.getDrawMode() == DrawMode.LINE) {
                checkBox.setSelected(true);
            } else {
                checkBox.setSelected(false);
            }

        }

        ToolBar toolBar = new ToolBar(label, checkBox);
        toolBar.setOrientation(Orientation.VERTICAL);

        return toolBar;
    }
}
