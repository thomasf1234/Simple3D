package simple3d;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simple3d.scene_states.Default;

public class Simple3D extends Application {
    private SceneState sceneState;
    private double mouseXOld = 0;
    private double mouseYOld = 0;
    private Text fps;
    private Node selected;
    private BorderPane pane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Director director = new Director();
        this.sceneState = new Default(director);
        handleKeyboard(director);
        setMouseEvents(director);
        this.pane = new BorderPane();
        pane.setCenter(director.getSubScene());
        Scene scene = new Scene(pane);

        fps = new Text();
        fps.setFill(Color.BLACK);
        fps.setFontSmoothingType(FontSmoothingType.LCD);
        pane.setBottom(fps);

        FrameRateUpdater frameRateUpdater = new FrameRateUpdater(fps);
        frameRateUpdater.start();

        primaryStage.setResizable(false);
        primaryStage.setTitle("Simple3D");
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public double getMouseXOld() {
        return mouseXOld;
    }

    public double getMouseYOld() {
        return mouseYOld;
    }

    private void handleKeyboard(final Director director) {
        director.getSubScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                CameraMan cameraMan = director.getCameraMan();

                switch (event.getCode()) {
                    case L:
                        cameraMan.faceTarget();
                        break;
                    case F:
                        cameraMan.moveForward(3);
                        break;
                    case B:
                        cameraMan.moveForward(-3);
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

                }

                event.consume();
            }
        });
    }

    private void setMouseEvents(final Director director) {
        director.getSubScene().setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        sceneState.onScroll(event);
                        event.consume();
                    }
                });

        EventHandler mouseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseXNew = event.getSceneX();
                double mouseYNew = event.getSceneY();

                if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    sceneState = sceneState.onMouseClick(event);
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    sceneState.onMouseDrag(mouseXOld, mouseYOld, mouseXNew, mouseYNew);
                } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                    sceneState.onMouseMove(event);
                }

                mouseXOld = mouseXNew;
                mouseYOld = mouseYNew;
                event.consume();
            }
        };


//        EventHandler mouseEventHandler = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                double mouseX = event.getSceneX();
//                double mouseY = event.getSceneY();
//
//                if (event.getEventType() == MouseEvent.MOUSE_PRESSED  && event.isSecondaryButtonDown()) {
//                    ContextMenu contextMenu = new ContextMenu();
//                    MenuItem add = new MenuItem("Add Cube");
//                    MenuItem copy = new MenuItem("Copy");
//                    MenuItem paste = new MenuItem("Paste");
//                    contextMenu.getItems().addAll(add, copy, paste);
//                    contextMenu.setAutoHide(true);
//                    add.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//                            Box box = new Box(10, 10, 10);
////                            director.add(box, 0, 0, 0);
//
//                            Point2D subSceneCenter = director.getSubSceneAbsoluteCenter2D();
//                            simple3d.CameraMan cameraMan = director.getCameraMan();
//                            Point3D newPos = cameraMan.getPosition().add(cameraMan.getForward().multiply(-(cameraMan.getY()/cameraMan.getForward().getY())));
//                            selected = box;
//                            selected.setTranslateX(newPos.getX());
//                            selected.setTranslateY(newPos.getY());
//                            selected.setTranslateZ(newPos.getZ());
//                            director.add(box, (float) newPos.getX(), (float) newPos.getY(), (float) newPos.getZ());
//
//                            System.out.println("Add...");
//                        }
//                    });
//                    contextMenu.show(director.getSubScene(), event.getScreenX(), event.getScreenY());
//                } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
//                    //acquire new mouse coordinates
//                    double mouseXNew = event.getSceneX();
//                    double mouseYNew = event.getSceneY();
//
//                    if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
//                        double dx = mouseXNew - mouseXOld;
//                        double dy = mouseYNew - mouseYOld;
//                        director.getCameraMan().xRotate.setAngle(director.getCameraMan().xRotate.getAngle() - dy / 5);
//                        director.getCameraMan().yRotate.setAngle(director.getCameraMan().yRotate.getAngle() + dx / 5);
//                    }
//                    mouseXOld = mouseXNew;
//                    mouseYOld = mouseYNew;
//                    event.consume();
//                } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
//                    Node selectedNode = event.getPickResult().getIntersectedNode();
//                    if (selectedNode != null && selectedNode != director.getSubScene()) {
//                        System.out.println(selectedNode.toString());
//                        director.getCameraMan().setTarget(selectedNode.getTranslateX(), selectedNode.getTranslateY(), selectedNode.getTranslateZ());
//                        director.getCameraMan().faceTarget();
//                    }
//                }  else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
//                    director.getSubScene().requestFocus();
//
//                    if (selected != null) {
//                        Point2D subSceneCenter = director.getSubSceneAbsoluteCenter2D();
//
////                        Path path = new Path();
////                        path.getElements().add(new MoveTo(subSceneCenter.getX(), subSceneCenter.getY()));
////                        path.getElements().add(new LineTo(event.getSceneX(), event.getSceneY()));
////                        path.setStrokeWidth(1);
////                        path.setStroke(Color.PINK);
////                        pane.getChildren().add(path);
//
//                        simple3d.CameraMan cameraMan = director.getCameraMan();
//                        Point3D newPos = cameraMan.getPosition().add(cameraMan.getForward().multiply(-(cameraMan.getY()/cameraMan.getForward().getY())));
//
//                        newPos = newPos.add(cameraMan.getRight().multiply(event.getSceneX() - subSceneCenter.getX()).multiply(0.2));
//                        newPos = newPos.add(cameraMan.getUp().multiply(- event.getSceneY() + subSceneCenter.getY()).multiply(0.2));
//                        selected.setTranslateX(newPos.getX());
//                        selected.setTranslateY(newPos.getY());
//                        selected.setTranslateZ(newPos.getZ());
//
//
////                        Point2D localPoint = selected.screenToLocal(event.getSceneX(), event.getSceneY());
////                        Point3D newLoc = selected.localToScene(localPoint.getX(), 0, localPoint.getY());
////                        selected.localToScen
////                        selected.setTranslateX(newLoc.getX());
////                        selected.setTranslateY(0);
////                        selected.setTranslateZ(-newLoc.getY());
//                    }
//
//                }
//            }
//        };

        director.getSubScene().addEventHandler(MouseEvent.ANY, mouseEventHandler);
    }
}
