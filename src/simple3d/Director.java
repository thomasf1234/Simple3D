package simple3d;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import simple3d.factories.CoordSystemFactory;
import simple3d.factories.GridFactory;
import simple3d.factories.LineFactory;
import simple3d.factories.PointFactory;

/**
 * Created by tfisher on 06/03/2017.
 */
public class Director {
    private Group root;
    private Group editGroup;
    private Group nonEditGroup;
    private SubScene subScene;
    private CameraMan cameraMan;

    public Director() {
        this.root = new Group();
        this.editGroup = new Group();

        //nonEditGroup for nodes that cannot be altered such as the grid
        this.nonEditGroup = new Group();
        root.getChildren().addAll(nonEditGroup, editGroup);

        //create empty subscene
        this.subScene = new SimpleSubScene(root, 800, 600, true, SceneAntialiasing.DISABLED);
        subScene.setFill(Color.BLACK);

        SimpleMeshView coordAxisMeshView = CoordSystemFactory.build();

        nonEditGroup.getChildren().addAll(coordAxisMeshView);

        //add cameraman
        this.cameraMan = new CameraMan(subScene);
        cameraMan.setPosition(Point3D.ZERO);

        //add light to nonEditGroup
        AmbientLight light = new AmbientLight();
        light.setColor(Color.WHITE);
        ObservableList<Node> affectedNodes = light.getScope();
        affectedNodes.add(nonEditGroup);
        affectedNodes.add(editGroup);
        nonEditGroup.getChildren().add(light);

        //default lighting for editable group
        PointLight defaultPointLight = new PointLight();
        defaultPointLight.setColor(Color.WHITE);
        ObservableList<Node> defaultPointLightAffectedNodes = defaultPointLight.getScope();
        defaultPointLightAffectedNodes.add(editGroup);
        defaultPointLight.setTranslateX(-50);
        defaultPointLight.setTranslateY(-50);
        defaultPointLight.setTranslateZ(0);
        nonEditGroup.getChildren().add(defaultPointLight);
    }

    public CameraMan getCameraMan() {
        return cameraMan;
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public void add(Node node) {
        editGroup.getChildren().add(node);
    }

    public void remove(Node node) {
        editGroup.getChildren().remove(node);
    }

    public Point2D getSubSceneAbsoluteCenter2D() {
        Point2D s2DCenter = new Point2D(subScene.getTranslateX() + subScene.getWidth()/2.0,
                subScene.getTranslateY() + subScene.getHeight()/2.0);

        return s2DCenter;
    }

    public boolean isSelectable(Node node) {
        return editGroup.getChildren().contains(node);
    }
}
