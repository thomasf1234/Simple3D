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
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import simple3d.*;
import simple3d.io.MeshViewIO2;
import simple3d.util.MeshUtils;

import java.io.File;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Selected extends Default {
    protected SimpleMeshView selectedMeshView;

    public Selected(SimpleScene simpleScene, Director director, SimpleMeshView selectedMeshView) {
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
                cameraMan.setTarget(selectedMeshView.getTranslate().getX(), selectedMeshView.getTranslate().getY(), selectedMeshView.getTranslate().getZ());
                cameraMan.faceTarget();
                break;
            case UP:
                cameraMan.moveUp(1);
                break;
            case DOWN:
                cameraMan.moveUp(-1);
                break;
            case LEFT:
                cameraMan.moveRight(-1);
                break;
            case RIGHT:
                cameraMan.moveRight(1);
                break;
            case O:
                cameraMan.reset();
                break;
            case R:
                cameraMan.removeTarget();
                break;
            case K:
                selectedMeshView.getyRotate().setAngle(40);
                selectedMeshView.getxRotate().setAngle(70);
                break;
            default:
                break;
        }
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();

        if (event.getButton() == MouseButton.SECONDARY) {
            if (selectedNode == selectedMeshView) {
                SimpleMeshView selectedMeshView = (SimpleMeshView) selectedNode;
                ContextMenu contextMenu = getOnMeshViewRightClickContextMenu(selectedMeshView);
                contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            }
        } else {
            if (selectedNode != selectedMeshView) {
                setNextSceneState(new Default(simpleScene, director));
            }
        }
    }

    @Override
    public void onMouseDrag(MouseEvent event, double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {
        if (event.isControlDown()) {
            Point3D newRight = director.getCameraMan().getRight().multiply((mouseXNew - mouseXOld)/50);
            Point3D newUp = director.getCameraMan().getUp().multiply((- mouseYNew + mouseYOld)/50);
            selectedMeshView.getTranslate().setX(selectedMeshView.getTranslate().getX() + newRight.getX() + newUp.getX());
            selectedMeshView.getTranslate().setY(selectedMeshView.getTranslate().getY() + newRight.getY() + newUp.getY());
            selectedMeshView.getTranslate().setZ(selectedMeshView.getTranslate().getZ() + newRight.getZ() + newUp.getZ());
        }
    }

    protected void openMeshDialog(MeshView selectedMeshView) {
        Label label = new Label("Selected: " + selectedMeshView.getProperties().get("name"));

        final CheckBox checkBox = new CheckBox("Wireframe");

        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected()) {
                    selectedMeshView.setDrawMode(DrawMode.LINE);
                    selectedMeshView.setCullFace(CullFace.NONE);
                } else {
                    selectedMeshView.setDrawMode(DrawMode.FILL);
                    selectedMeshView.setCullFace(CullFace.BACK);
                }
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
