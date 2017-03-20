package simple3d.scene_states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import simple3d.*;
import simple3d.util.MeshUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by tfisher on 07/03/2017.
 */
public class EditMesh extends Selected {
    public static Color COLOR_NOT_SELECTED = Color.RED;
    public static Color COLOR_SELECTED = Color.YELLOW;
    private List<SimpleVertex> points;
    private List<SimpleVertex> selectedPoints;
    private Rectangle boxSelect;
    private Map<Integer, HashMap<Integer, Node>> tmpPoints;

    public EditMesh(SimpleScene simpleScene, Director director, SimpleMeshView selectedMeshView) {
        super(simpleScene, director, selectedMeshView);

        Point3D[] vertices = MeshUtils.getVertices(selectedMeshView);
        points = new ArrayList<>();
        selectedPoints = new ArrayList<>();

        for (Point3D vertex : vertices) {
            SimpleVertex point = new SimpleVertex();
            point.setMaterial(new PhongMaterial(COLOR_NOT_SELECTED));
            //must make scale, translate and rotation transforms on Mesh such that they can all be applied to the point
            //Director add group to hold Selected Mesh, this is a copy of the original that we can edit, we also temp hide the original. (a hidden group perhaps)
            double x = vertex.getX() * selectedMeshView.getScale().getX();
            double y = vertex.getY() * selectedMeshView.getScale().getY();
            double z = vertex.getZ() * selectedMeshView.getScale().getZ();

            Translate baseTranslate = new Translate(x, y, z);
            Rotate xRotate = new Rotate(selectedMeshView.getxRotate().getAngle(), -x, -y, -z, Rotate.X_AXIS);
            Rotate yRotate = new Rotate(selectedMeshView.getyRotate().getAngle(), -x, -y, -z, Rotate.Y_AXIS);
            point.getTranslate().setX(x);
            point.getTranslate().setY(y);
            point.getTranslate().setZ(z);
            point.getTransforms().addAll(selectedMeshView.getTranslate().clone(), xRotate, yRotate);

            points.add(point);
            director.add(point);
        }
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        System.out.println("Mouse Pressed");
        boxSelect = new Rectangle();
        boxSelect.setTranslateX(event.getSceneX());
        boxSelect.setTranslateY(event.getSceneY());
        boxSelect.setFill(new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.5));
        boxSelect.setStroke(Color.CYAN);
        boxSelect.setStrokeWidth(1);
        boxSelect.setDisable(true);
        SimpleScene simpleScene = (SimpleScene) director.getSubScene().getScene();
        simpleScene.getPane().getChildren().add(boxSelect);
        tmpPoints = new HashMap<Integer, HashMap<Integer, Node>>();
    }

    //L-Click and drag to select
    //Holding Shift for +=
    //R-Click and drag to deselect
    @Override
    public void onMouseReleased(MouseEvent event) {
        System.out.println("Mouse Released");

        if (!event.isShiftDown()) {
            selectedPoints.forEach(point -> {
                PhongMaterial material = (PhongMaterial) point.getMaterial();
                material.setDiffuseColor(COLOR_NOT_SELECTED);
            });
            selectedPoints.clear();
        }

        SimpleSubScene simpleSubScene = (SimpleSubScene) director.getSubScene();
        long t0 = System.currentTimeMillis();
        int sceneX = (int) boxSelect.getTranslateX();
        int sceneY = (int) boxSelect.getTranslateY();

        //Parallel version
//        IntStream.range(sceneX, (int) event.getSceneX()).parallel().forEach(i -> {
//            final int row = i;
//            IntStream.range(sceneY, (int) event.getSceneY()).parallel().forEach(j -> {
//                Node selectedNode = simpleSubScene.getPick(row, j);
//                if (selectedNode instanceof SimpleVertex) {
//                    SimpleVertex point = (SimpleVertex) selectedNode;
//                    PhongMaterial material = (PhongMaterial) point.getMaterial();
//                    if (event.getButton() == MouseButton.SECONDARY) {
//                        if (selectedPoints.contains(point)) {
//                            material.setDiffuseColor(COLOR_NOT_SELECTED);
//                            selectedPoints.remove(point);
//                        }
//                    } else {
//                        material.setDiffuseColor(COLOR_SELECTED);
//                        selectedPoints.add(point);
//                    }
//                }
//            });
//        });

        //Parallel version
        int x = (int) ((event.getSceneX() - sceneX) / 2.0);
        IntStream.range(0, x).parallel().forEach(i -> {
            int xIndex = i * 2;
            if (tmpPoints.get(xIndex) == null) {
                tmpPoints.put(xIndex, new HashMap<Integer, Node>());
            }
            final int row = xIndex;
            int y = (int) ((event.getSceneY() - sceneY) / 2.0);
            IntStream.range(0, y).parallel().forEach(j -> {
                int yIndex = j*2;
                if (!tmpPoints.get(row).containsKey(yIndex)) {
                    tmpPoints.get(row).put(yIndex, simpleSubScene.getPick(row + xIndex, yIndex + sceneY));
                }

                Node selectedNode = tmpPoints.get(row).get(yIndex);
                if (selectedNode instanceof SimpleVertex) {
                    SimpleVertex point = (SimpleVertex) selectedNode;
                    PhongMaterial material = (PhongMaterial) point.getMaterial();
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if (selectedPoints.contains(point)) {
                            material.setDiffuseColor(COLOR_NOT_SELECTED);
                            selectedPoints.remove(point);
                        }
                    } else {
                        material.setDiffuseColor(COLOR_SELECTED);
                        selectedPoints.add(point);
                    }
                }
            });
        });

        long t1 = System.currentTimeMillis();
        System.out.println("t: " + (t1-t0)/1000.0 + "s");
        simpleScene.getPane().getChildren().remove(boxSelect);
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        Node selectedNode = event.getPickResult().getIntersectedNode();

        if (event.getButton() == MouseButton.SECONDARY) {
            getOnMeshViewRightClickContextMenu().show(director.getSubScene().getScene().getWindow());
        } else {
            for (SimpleVertex point : points) {
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
        //System.out.println("Mouse Dragged");
        //must add box select
        boxSelect.setWidth(mouseXNew - boxSelect.getTranslateX());
        boxSelect.setHeight(mouseYNew - boxSelect.getTranslateY());

        //***********************************************
        SimpleSubScene simpleSubScene = (SimpleSubScene) director.getSubScene();
        int sceneX = (int) boxSelect.getTranslateX();
        int sceneY = (int) boxSelect.getTranslateY();

        //Parallel version
        int x = (int) ((event.getSceneX() - sceneX) / 2.0);
        IntStream.range(0, x).parallel().forEach(i -> {
            int xIndex = i * 2;
            if (tmpPoints.get(xIndex) == null) {
                tmpPoints.put(xIndex, new HashMap<Integer, Node>());
            }
            final int row = xIndex;
            int y = (int) ((event.getSceneY() - sceneY) / 2.0);
            IntStream.range(0, y).parallel().forEach(j -> {
                int yIndex = j*2;
                if (!tmpPoints.get(row).containsKey(yIndex)) {
                    tmpPoints.get(row).put(yIndex, simpleSubScene.getPick(row + sceneX, yIndex + sceneY));
                }
            });
        });
        //***********************************************

        if (event.isControlDown()) {
            for (SimpleVertex selectedPoint : selectedPoints) {
                Point3D newRight = director.getCameraMan().getRight().multiply((mouseXNew - mouseXOld)/1000.0);
                Point3D newUp = director.getCameraMan().getUp().multiply((- mouseYNew + mouseYOld)/1000.0);

                selectedPoint.getTranslate().setX(selectedPoint.getTranslate().getX() + newRight.getX() + newUp.getX());
                selectedPoint.getTranslate().setY(selectedPoint.getTranslate().getY() + newRight.getY() + newUp.getY());
                selectedPoint.getTranslate().setZ(selectedPoint.getTranslate().getZ() + newRight.getZ() + newUp.getZ());

                int pointIndex = points.indexOf(selectedPoint) * 3;
                TriangleMesh triangleMesh = (TriangleMesh) selectedMeshView.getMesh();

                triangleMesh.getPoints().set(pointIndex, (float) selectedPoint.getTranslate().getX() - (float) selectedMeshView.getTranslate().getX());
                triangleMesh.getPoints().set(pointIndex + 1, (float) selectedPoint.getTranslate().getY() - (float) selectedMeshView.getTranslate().getY());
                triangleMesh.getPoints().set(pointIndex + 2, (float) selectedPoint.getTranslate().getZ() - (float) selectedMeshView.getTranslate().getZ());
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
                    for (SimpleVertex point : points) {
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
