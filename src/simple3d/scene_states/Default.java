package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import simple3d.Director;
import simple3d.SceneState;
import simple3d.io.MeshViewIO;
import simple3d.io.MeshViewIO2;

import java.io.File;

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

    public SceneState onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();
        if (selectedNode != null && selectedNode != director.getSubScene()) {
            if (event.getButton() == MouseButton.SECONDARY) {
                if (selectedNode instanceof MeshView) {
                    MeshView selectedMeshView = (MeshView) selectedNode;
                    ContextMenu contextMenu = getOnMeshViewRightClickContextMenu(selectedMeshView);
                    contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
                }
            } else {
                director.getCameraMan().setTarget(selectedNode.getTranslateX(), selectedNode.getTranslateY(), selectedNode.getTranslateZ());
                director.getCameraMan().faceTarget();
                return new Selected(director, (MeshView) selectedNode);
            }
        } else {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = getRightClickContextMenu();
                contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            }
        }

        return this;
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
        MenuItem addSuzanne = new MenuItem("Add Suzanne");
        MenuItem addSuzanneBinary = new MenuItem("Add Suzanne Binary");
        contextMenu.getItems().addAll(add, addSuzanne, addSuzanneBinary);
        contextMenu.setAutoHide(true);
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Box box = new Box(10, 10, 10);
                MeshView cubeMeshView = null;

                try {
                    cubeMeshView = MeshViewIO.read("src/simple3d/resources/Cube.xml");
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

        addSuzanne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MeshView suzanneMeshView = null;

                try {
                    suzanneMeshView = MeshViewIO.read("src/simple3d/resources/Suzanne.xml");
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

        addSuzanneBinary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MeshView suzanneMeshView = null;

                try {
                    suzanneMeshView = MeshViewIO2.read("tmp/Suzanne.bin");
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

    private ContextMenu getOnMeshViewRightClickContextMenu(MeshView selectedMeshView) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem export = new MenuItem("Export");
        MenuItem exportBinary = new MenuItem("Export Binary");
        contextMenu.getItems().addAll(export, exportBinary);
        contextMenu.setAutoHide(true);
        export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    File outFile = MeshViewIO.write(selectedMeshView);
                    System.out.println(outFile.getAbsolutePath());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        exportBinary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MeshViewIO2.write(selectedMeshView, "tmp/Suzanne.bin");
                    System.out.println("Wrote binary file");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return contextMenu;
    }
}
