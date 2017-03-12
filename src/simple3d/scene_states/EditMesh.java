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
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import simple3d.CameraMan;
import simple3d.Director;
import simple3d.SimpleScene;
import simple3d.util.MeshUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 07/03/2017.
 */
public class EditMesh extends Selected {
    public static Color COLOR_NOT_SELECTED = Color.RED;
    public static Color COLOR_SELECTED = Color.YELLOW;
    private List<Shape3D> points;
    private List<Shape3D> selectedPoints;

    public EditMesh(SimpleScene simpleScene, Director director, MeshView selectedMeshView) {
        super(simpleScene, director, selectedMeshView);

        Point3D[] vertices = MeshUtils.getVertices(selectedMeshView);
        points = new ArrayList<>();
        selectedPoints = new ArrayList<>();

        for (Point3D vertex : vertices) {
            Sphere point = new Sphere(0.01);
            point.setMaterial(new PhongMaterial(COLOR_NOT_SELECTED));
            //Director add group to hold Selected Mesh, this is a copy of the original that we can edit, we also temp hide the original. (a hidden group perhaps)
//            selectedMeshView.setDrawMode(DrawMode.LINE);
            point.getTransforms().addAll(selectedMeshView.getTransforms());
            point.setTranslateX((vertex.getX() * selectedMeshView.getScaleX()) + selectedMeshView.getTranslateX());
            point.setTranslateY((vertex.getY() * selectedMeshView.getScaleY()) + selectedMeshView.getTranslateY());
            point.setTranslateZ((vertex.getZ() * selectedMeshView.getScaleZ()) + selectedMeshView.getTranslateZ());
            point.setRotationAxis(selectedMeshView.getRotationAxis());
            point.setRotate(selectedMeshView.getRotate());

            points.add(point);
            director.add(point);
        }
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();

        if (event.getButton() == MouseButton.SECONDARY) {
            getOnMeshViewRightClickContextMenu().show(director.getSubScene().getScene().getWindow());
        } else {
            for (Shape3D point : points) {
                if (selectedNode == point) {
                    PhongMaterial material = (PhongMaterial) point.getMaterial();
                    material.setDiffuseColor(COLOR_SELECTED);
                    selectedPoints.add(point);
                    return;
                }
            }
        }

//        if (selectedNode != selectedMeshView) {
//            for (Shape3D point : points) {
//                director.remove(point);
//            }
//            setNextSceneState(new Default(simpleScene, director));
//        }
    }

    @Override
    public void onMouseDrag(MouseEvent event, double mouseXOld, double mouseYOld, double mouseXNew, double mouseYNew) {
        if (event.isControlDown()) {
            for (Shape3D selectedPoint : selectedPoints) {
                Point3D newRight = director.getCameraMan().getRight().multiply((mouseXNew - mouseXOld)/200);
                Point3D newUp = director.getCameraMan().getUp().multiply((- mouseYNew + mouseYOld)/200);

                selectedPoint.setTranslateX(selectedPoint.getTranslateX() + newRight.getX() + newUp.getX());
                selectedPoint.setTranslateY(selectedPoint.getTranslateY() + newRight.getY() + newUp.getY());
                selectedPoint.setTranslateZ(selectedPoint.getTranslateZ() + newRight.getZ() + newUp.getZ());

                int pointIndex = points.indexOf(selectedPoint) * 3;
                TriangleMesh triangleMesh = (TriangleMesh) selectedMeshView.getMesh();

                triangleMesh.getPoints().set(pointIndex, (float) selectedPoint.getTranslateX() - (float) selectedMeshView.getTranslateX());
                triangleMesh.getPoints().set(pointIndex + 1, (float) selectedPoint.getTranslateY() - (float) selectedMeshView.getTranslateY());
                triangleMesh.getPoints().set(pointIndex + 2, (float) selectedPoint.getTranslateZ() - (float) selectedMeshView.getTranslateZ());
            }
        }
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
            case G:

                break;
            default:
                break;
        }
    }

    protected ContextMenu getOnMeshViewRightClickContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem exitMenuItem = new MenuItem("Exit");
        contextMenu.getItems().addAll(exitMenuItem);
        contextMenu.setAutoHide(true);

        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    for (Shape3D point : points) {
                        director.remove(point);
                    }
                    setNextSceneState(new Default(simpleScene, director));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return  contextMenu;
    }
}
