package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import simple3d.CameraMan;
import simple3d.Director;
import simple3d.SceneState;
import simple3d.loaders.MeshViewLoader;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Default extends SceneState {
    private Director director;

    public Default(Director director) {
        this.director = director;
    }

    public void onScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if (director.getCameraMan().getTargetDistance() > deltaY) {
            director.getCameraMan().moveForward(deltaY);
        }
    }

    public void onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();
        if (selectedNode != null && selectedNode != director.getSubScene()) {
            if (event.isSecondaryButtonDown()) {

            } else {
                System.out.println(selectedNode.toString());
                director.getCameraMan().setTarget(selectedNode.getTranslateX(), selectedNode.getTranslateY(), selectedNode.getTranslateZ());
                director.getCameraMan().faceTarget();
            }
        } else {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = getRightClickContextMenu();
                contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            }
        }
    }

    public void onMouseMove(MouseEvent event) {
        director.getSubScene().requestFocus();
    }

    public void onMouseDrag(double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {
        double dx = mouseXNew - mouseXOld;
        double dy = mouseYNew - mouseYOld;
        director.getCameraMan().xRotate.setAngle(director.getCameraMan().xRotate.getAngle() - dy / 5);
        director.getCameraMan().yRotate.setAngle(director.getCameraMan().yRotate.getAngle() + dx / 5);
    }


    private ContextMenu getRightClickContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem add = new MenuItem("Add Cube");
        MenuItem copy = new MenuItem("Add Suzanne");
        MenuItem paste = new MenuItem("Paste");
        contextMenu.getItems().addAll(add, copy, paste);
        contextMenu.setAutoHide(true);
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Box box = new Box(10, 10, 10);
                MeshView cubeMeshView = null;

                try {
                    cubeMeshView = MeshViewLoader.load("src/simple3d/resources/Cube.xml");
                    director.add(cubeMeshView, (float) 0.0, (float) 0.0, (float) 0.0);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }

//                Point2D subSceneCenter = director.getSubSceneAbsoluteCenter2D();
//                CameraMan cameraMan = director.getCameraMan();
//                Point3D newPos = cameraMan.getPosition().add(cameraMan.getForward().multiply(-(cameraMan.getY()/cameraMan.getForward().getY())));
//                box.setTranslateX(newPos.getX());
//                box.setTranslateY(newPos.getY());
//                box.setTranslateZ(newPos.getZ());
//                director.add(box, (float) newPos.getX(), (float) newPos.getY(), (float) newPos.getZ());
//
//                System.out.println("Adding box...");
            }
        });

        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MeshView suzanneMeshView = null;

                try {
                    suzanneMeshView = MeshViewLoader.load("src/simple3d/resources/Suzanne.xml");
                    suzanneMeshView.setScaleX(5.0);
                    suzanneMeshView.setScaleY(5.0);
                    suzanneMeshView.setScaleZ(5.0);
                    director.add(suzanneMeshView, (float) 0.0, (float) 0.0, (float) 0.0);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return contextMenu;
    }
}
