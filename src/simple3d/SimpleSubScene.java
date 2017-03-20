package simple3d;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.input.PickResultChooser;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.input.PickResult;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Transform;

import java.util.List;

/**
 * Created by tfisher on 12/03/2017.
 */
public class SimpleSubScene extends SubScene {
    public SimpleSubScene(Group root, int width, int height, boolean depthBuffer, SceneAntialiasing sceneAntialiasing) {
        super(root, width, height, depthBuffer, sceneAntialiasing);
    }

    //sceneX and sceneY from MouseEvent, (0,0) is upper left corner of scene.
    public Node getPick(double sceneX, double sceneY) {
        Scene scene = getScene();
        ParallelCamera camera = new ParallelCamera();

        //System.out.println("x,y " + sceneX + "," + sceneY);
        Affine3D localToSceneTx = new Affine3D();
        localToSceneTx.setToIdentity();
//        Transform localToSceneTransform = getLocalToSceneTransform();
//        localToSceneTransform.impl_apply(localToSceneTx);

        PickRay pickRay = PickRay.computeParallelPickRay(sceneX, sceneY,
                scene.getHeight(),
                localToSceneTx,
                camera.getNearClip(), camera.getFarClip(),
                null);

        //ensure direction is (0,0,1)
        pickRay.getDirectionNoClone().normalize();
        PickResultChooser result = new PickResultChooser();
        scene.getRoot().impl_pickNode(pickRay, result);

        return result.toPickResult().getIntersectedNode();
    }
}
