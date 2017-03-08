package simple3d;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import simple3d.factories.GridFactory;

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
        this.nonEditGroup = new Group();
        root.getChildren().addAll(nonEditGroup, editGroup);

        //create empty subscene
        this.subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        //add grid
        MeshView gridMeshView = GridFactory.build(10, 20);
        nonEditGroup.getChildren().add(gridMeshView);

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

    public void add(Node node, float x, float y, float z) {
        editGroup.getChildren().add(node);
    }

    public Point2D getSubSceneAbsoluteCenter2D() {
        Point2D s2DCenter = new Point2D(subScene.getTranslateX() + subScene.getWidth()/2.0,
                subScene.getTranslateY() + subScene.getHeight()/2.0);

        return s2DCenter;
    }
}
