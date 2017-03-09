package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.stage.FileChooser;
import simple3d.Director;
import simple3d.SceneState;
import simple3d.Simple3D;
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
        MenuItem export = new MenuItem("Export");
        contextMenu.getItems().addAll(export);
        contextMenu.setAutoHide(true);

        export.setOnAction(new EventHandler<ActionEvent>() {
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

        return contextMenu;
    }
}
