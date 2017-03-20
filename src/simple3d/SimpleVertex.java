package simple3d;

import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * Created by tfisher on 12/03/2017.
 */
public class SimpleVertex extends Sphere {
    private static double RADIUS = 0.01;
    private Translate translate;

    public SimpleVertex() {
        super(RADIUS);
        this.translate = new Translate();
        getTransforms().addAll(translate);
    }

    public Translate getTranslate() {
        return translate;
    }
}
