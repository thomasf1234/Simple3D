package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simple3d.CameraMan;
import simple3d.Director;
import simple3d.SceneState;
import simple3d.Simple3D;
import simple3d.io.MeshViewIO;
import simple3d.io.MeshViewIO2;
import simple3d.util.MeshUtils;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.Label;
import java.io.File;

/**
 * Created by tfisher on 07/03/2017.
 */
public class Default extends SceneState {
    private Director director;
    private MeshView selectedMeshView;
    private Stage meshViewStage;

    public Default(Director director) {
        this.director = director;
    }

    public void onKeyPressed(KeyEvent event) {
        CameraMan cameraMan = director.getCameraMan();

        switch (event.getCode()) {
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

    public void onScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if (director.getCameraMan().getTargetDistance() > deltaY) {
            director.getCameraMan().moveForward(deltaY/10);
        }
    }

    public SceneState onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();
        if (selectedNode != null && director.isSelectable(selectedNode)) {
            if (selectedNode instanceof MeshView) {
                if (selectedNode == selectedMeshView) {
                    if (meshViewStage == null) {
                        meshViewStage = openMeshDialog(selectedMeshView);
                    }
                } else {
                    if (meshViewStage != null) {
                        meshViewStage.close();
                    }

                    this.selectedMeshView = (MeshView) selectedNode;
                    meshViewStage = openMeshDialog(selectedMeshView);
                }

                if (event.getButton() == MouseButton.SECONDARY) {
                    MeshView selectedMeshView = (MeshView) selectedNode;
                    ContextMenu contextMenu = getOnMeshViewRightClickContextMenu(selectedMeshView);
                    contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
                }
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

    public void onMouseDrag(MouseEvent event, double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {
        double dx = mouseXNew - mouseXOld;
        double dy = mouseYNew - mouseYOld;

        if (event.isShiftDown()) {
            CameraMan cameraMan = director.getCameraMan();
            cameraMan.removeTarget();
            cameraMan.moveRight(-dx/8);
            cameraMan.moveUp(dy/8);
        } else {
            director.getCameraMan().xRotate.setAngle(director.getCameraMan().xRotate.getAngle() - dy / 5);
            director.getCameraMan().yRotate.setAngle(director.getCameraMan().yRotate.getAngle() + dx / 5);
        }
    }


    private ContextMenu getRightClickContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        //Sub-Menu
        Menu addMenu = new Menu("Add");
        MenuItem addCubeMenuItem = new MenuItem("Cube");
        MenuItem addSuzanneMenuItem = new MenuItem("Suzanne");
        addMenu.getItems().addAll(addCubeMenuItem, addSuzanneMenuItem);

        MenuItem importMenuItem = new MenuItem("Import");
        contextMenu.getItems().addAll(addMenu, importMenuItem);
        contextMenu.setAutoHide(true);
        addCubeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MeshView cubeMeshView = null;

                try {
                    cubeMeshView = MeshViewIO2.read(new File("src/simple3d/resources/Cube.bin"));
                    cubeMeshView.setScaleX(5.0);
                    cubeMeshView.setScaleY(5.0);
                    cubeMeshView.setScaleZ(5.0);
                    cubeMeshView.getProperties().put("name", "Cube001");
                    director.add(cubeMeshView, (float) 0.0, (float) 0.0, (float) 0.0);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        addSuzanneMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MeshView suzanneMeshView = null;

                try {
                    suzanneMeshView = MeshViewIO2.read(new File("src/simple3d/resources/Suzanne.bin"));
                    suzanneMeshView.setScaleX(5.0);
                    suzanneMeshView.setScaleY(5.0);
                    suzanneMeshView.setScaleZ(5.0);
                    suzanneMeshView.getProperties().put("name", "Suzanne001");
                    director.add(suzanneMeshView, (float) 0.0, (float) 0.0, (float) 0.0);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        importMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MeshView meshView = null;

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
                    director.add(meshView, (float) 0.0, (float) 0.0, (float) 0.0);
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
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem exportMenuItem = new MenuItem("Export");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editMenuItem, exportMenuItem, deleteMenuItem);
        contextMenu.setAutoHide(true);

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Point3D[] vertices = MeshUtils.getVertices(selectedMeshView);


                    for (Point3D vertex : vertices) {
                        Sphere point = new Sphere(0.05);
                        point.setMaterial(new PhongMaterial(Color.RED));
                        //Director add group to hold Selected Mesh, this is a copy of the original that we can edit, we also temp hide the original. (a hidden group perhaps)
                        selectedMeshView.setDrawMode(DrawMode.LINE);
                        point.getTransforms().addAll(selectedMeshView.getTransforms());
                        point.setTranslateX(vertex.getX() * selectedMeshView.getScaleX());
                        point.setTranslateY(vertex.getY() * selectedMeshView.getScaleY());
                        point.setTranslateZ(vertex.getZ() * selectedMeshView.getScaleZ());
                        point.setRotationAxis(selectedMeshView.getRotationAxis());
                        point.setRotate(selectedMeshView.getRotate());

                        director.add(point);
                    }
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

    private Stage openMeshDialog(MeshView selectedMeshView) {
        javafx.scene.control.Label label = new javafx.scene.control.Label("Selected: " + selectedMeshView.getProperties().get("name"));

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

        Stage stage = new Stage();
        stage.setX(0);
        stage.setY(0);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Mesh Edit");
        stage.setScene(new Scene(toolBar));
        stage.initOwner(director.getPrimaryStage());
        stage.show();

        return stage;
    }
}
