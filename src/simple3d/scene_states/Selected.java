package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.stage.FileChooser;
import simple3d.CameraMan;
import simple3d.Director;
import simple3d.SceneState;
import simple3d.SimpleScene;
import simple3d.io.MeshViewIO2;
import simple3d.util.MeshUtils;

import java.io.File;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Selected extends Default {
    protected MeshView selectedMeshView;

    public Selected(SimpleScene simpleScene, Director director, MeshView selectedMeshView) {
        super(simpleScene, director);
        this.selectedMeshView = selectedMeshView;
        openMeshDialog(selectedMeshView);
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        CameraMan cameraMan = director.getCameraMan();

        switch (event.getCode()) {
            case C:
                cameraMan.setTarget(Point3D.ZERO);
                cameraMan.faceTarget();
                break;
            case F:
                cameraMan.setTarget(selectedMeshView.getTranslateX(), selectedMeshView.getTranslateY(), selectedMeshView.getTranslateZ());
                cameraMan.faceTarget();
                break;
            case UP:
                cameraMan.moveUp(3);
                break;
            case DOWN:
                cameraMan.moveUp(-3);
                break;
            case LEFT:
                cameraMan.moveRight(-3);
                break;
            case RIGHT:
                cameraMan.moveRight(3);
                break;
            case O:
                cameraMan.reset();
                break;
            case R:
                cameraMan.removeTarget();
                break;
            default:
                break;
        }
    }

    @Override
    public SceneState onMouseClick(MouseEvent event) {
        SceneState nextScene = this;
        Node selectedNode = event.getPickResult().getIntersectedNode();

        if (event.getButton() == MouseButton.SECONDARY) {
            if (selectedNode == selectedMeshView) {
                MeshView selectedMeshView = (MeshView) selectedNode;
                ContextMenu contextMenu = getOnMeshViewRightClickContextMenu(selectedMeshView);
                contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            }
        } else {
            if (selectedNode != selectedMeshView) {
                nextScene = new Default(simpleScene, director);
            }
        }

        return nextScene;
    }

    protected void openMeshDialog(MeshView selectedMeshView) {
        Label label = new Label("Selected: " + selectedMeshView.getProperties().get("name"));

        final CheckBox checkBox = new CheckBox("Wireframe");

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

        ToolBar toolBar = new ToolBar(label, checkBox);
        toolBar.setOrientation(Orientation.VERTICAL);

        simpleScene.getPane().setRight(toolBar);
    }
}
