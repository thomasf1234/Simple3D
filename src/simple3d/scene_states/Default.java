package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import simple3d.*;
import simple3d.factories.LineFactory;
import simple3d.factories.PlaneFactory;
import simple3d.io.MeshViewIO2;
import simple3d.util.MeshUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Default extends SceneState {
    protected Director director;

    public Default(SimpleScene simpleScene, Director director) {
        super(simpleScene);
        this.director = director;
        simpleScene.getPane().setRight(null);
    }

    public void onKeyPressed(KeyEvent event) {
        CameraMan cameraMan = director.getCameraMan();

        switch (event.getCode()) {
            case C:
                cameraMan.setTarget(Point3D.ZERO);
                cameraMan.faceTarget();
                break;
            case X:
                cameraMan.setPosition(30, 0, 0);
                cameraMan.setTarget(Point3D.ZERO);
                cameraMan.faceTarget();
                break;
            case Y:
                cameraMan.setPosition(0, 30, 0);
                cameraMan.setTarget(Point3D.ZERO);
                cameraMan.faceTarget();
                break;
            case Z:
                cameraMan.setPosition(0, 0, 30);
                cameraMan.setTarget(Point3D.ZERO);
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
            default:
                break;
        }
    }

    public void onScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if (director.getCameraMan().getTargetDistance() > deltaY/10) {
            director.getCameraMan().moveForward(deltaY/100);
        }
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        dragPath = new Path();
        dragPath.setStroke(Color.PINK);
        dragPath.setStrokeWidth(1);
        dragPath.setFill(new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.5));
        dragPath.getElements().add(new MoveTo(event.getSceneX(), event.getSceneY()));
        simpleScene.getPane().getChildren().add(dragPath);
    }

    @Override
    public void onMouseReleased(MouseEvent event) {
//        long t0 = System.nanoTime();
//        final AtomicInteger count = new AtomicInteger(0);
//        IntStream.range(0, (int) simpleScene.getWidth()).parallel().forEach(i -> {
//            final int row = i;
//            IntStream.range(0, (int) simpleScene.getHeight()).parallel().forEach(j -> {
//                count.incrementAndGet();
////                if (dragPath.contains(i, j)) {
////                    count.incrementAndGet();
////                    //System.out.println("contains x,y : " + i + "," + j);
////                }
//            });
//        });
//        long t1 = System.nanoTime();
//        System.out.println("points : " + count.get() + ", t: " + (t1-t0)/1_000_000_000.0 + "s");

        dragPath.getElements().clear();
        simpleScene.getPane().getChildren().remove(dragPath);
    }

    public void onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();

        if (event.getButton() == MouseButton.SECONDARY) {
            if (selectedNode != null && director.isSelectable(selectedNode)) {
                SimpleMeshView selectedMeshView = (SimpleMeshView) selectedNode;
                ContextMenu contextMenu = getOnMeshViewRightClickContextMenu(selectedMeshView);
                contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            } else {
                ContextMenu contextMenu = getRightClickContextMenu();
                contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
            }
        } else {
            if (selectedNode != null && director.isSelectable(selectedNode)) {
                SimpleMeshView selectedMeshView = (SimpleMeshView) selectedNode;
                setNextSceneState(new Selected(simpleScene, director, selectedMeshView));
            }
        }
    }

    public void onMouseMove(MouseEvent event) {
        director.getSubScene().requestFocus();
    }

    private Path dragPath;

    public void onMouseDrag(MouseEvent event, double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {
        double dx = mouseXNew - mouseXOld;
        double dy = mouseYNew - mouseYOld;

//        if (event.isShiftDown()) {
//            CameraMan cameraMan = director.getCameraMan();
//            cameraMan.removeTarget();
//            cameraMan.moveRight(-dx/8);
//            cameraMan.moveUp(dy/8);
//        } else {
//            director.getCameraMan().xRotate.setAngle(director.getCameraMan().xRotate.getAngle() - dy / 5);
//            director.getCameraMan().yRotate.setAngle(director.getCameraMan().yRotate.getAngle() + dx / 5);
//        }

        LineTo endTo = null;
        if (dragPath.getElements().size() > 1) {
            endTo = (LineTo) dragPath.getElements().get(dragPath.getElements().size() - 1);
            dragPath.getElements().remove(endTo);
        } else {
            MoveTo start = (MoveTo) dragPath.getElements().get(0);
            endTo = new LineTo(start.getX(), start.getY());
        }

        dragPath.getElements().addAll(new LineTo(event.getSceneX(), event.getSceneY()), endTo);

    }


    protected ContextMenu getRightClickContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        //Sub-Menu
        Menu addMenu = new Menu("Add");
        MenuItem addLineMenuItem = new MenuItem("Line");
        MenuItem addPlaneMenuItem = new MenuItem("Plane");
        MenuItem addCubeMenuItem = new MenuItem("Cube");
        MenuItem addSuzanneMenuItem = new MenuItem("Suzanne");
        addMenu.getItems().addAll(addLineMenuItem, addPlaneMenuItem, addCubeMenuItem, addSuzanneMenuItem);

        MenuItem importMenuItem = new MenuItem("Import");
        contextMenu.getItems().addAll(addMenu, importMenuItem);
        contextMenu.setAutoHide(true);
        addLineMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SimpleMeshView line = null;

                try {
                    line = LineFactory.build(new Point3D(-1, -1, 0), new Point3D(1, -1, 0), Color.WHITE);
                    line.getProperties().put("name", "Line001");
                    director.add(line);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        addPlaneMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SimpleMeshView plane = null;

                try {
                    plane = PlaneFactory.build(1, Color.GRAY);
                    plane.getProperties().put("name", "Plane001");
                    director.add(plane);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        addCubeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SimpleMeshView cubeMeshView = null;

                try {
                    cubeMeshView = MeshViewIO2.read(new File("src/simple3d/resources/Cube.bin"));
                    cubeMeshView.getProperties().put("name", "Cube001");
                    director.add(cubeMeshView);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        addSuzanneMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SimpleMeshView suzanneMeshView = null;

                try {
                    suzanneMeshView = MeshViewIO2.read(new File("src/simple3d/resources/Suzanne.bin"));
//                    suzanneMeshView.getScale().setX(2.0);
//                    suzanneMeshView.getScale().setY(2.0);
//                    suzanneMeshView.getScale().setZ(2.0);
                    suzanneMeshView.getTranslate().setX(0);
                    suzanneMeshView.getTranslate().setY(0);
                    suzanneMeshView.getTranslate().setZ(0);

                    suzanneMeshView.getProperties().put("name", "Suzanne001");
                    director.add(suzanneMeshView);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        importMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SimpleMeshView meshView = null;

                try {
                    FileChooser fileChooser = new FileChooser();

                    //Set extension filter
                    fileChooser.setTitle("Import MeshView");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", "*.bin");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showOpenDialog(director.getSubScene().getScene().getWindow());

                    if(file != null){
                        meshView = MeshViewIO2.read(file);
                        System.out.println("Read binary file");
                    }

                    meshView.setScaleX(5.0);
                    meshView.setScaleY(5.0);
                    meshView.setScaleZ(5.0);
                    director.add(meshView);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });



        return contextMenu;
    }

    protected ContextMenu getOnMeshViewRightClickContextMenu(SimpleMeshView selectedMeshView) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem exportMenuItem = new MenuItem("Export");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editMenuItem, exportMenuItem, deleteMenuItem);
        contextMenu.setAutoHide(true);

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    setNextSceneState(new EditMesh(simpleScene, director, selectedMeshView));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        exportMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileChooser fileChooser = new FileChooser();

                    //Set extension filter
                    fileChooser.setTitle("Export MeshView");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", "*.bin");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(director.getSubScene().getScene().getWindow());

                    if(file != null){
                        MeshViewIO2.write(selectedMeshView, file);
                        System.out.println("Wrote binary file");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    director.remove(selectedMeshView);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return contextMenu;
    }
}
