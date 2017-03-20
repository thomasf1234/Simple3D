package simple3d;

import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * Created by tfisher on 12/03/2017.
 */
public class SimpleMeshView extends MeshView {
    private Translate translate;
    private final Scale scale;
    private final Rotate xRotate;
    private final Rotate yRotate;

    public SimpleMeshView(Mesh mesh) {
        super(mesh);
        this.translate = new Translate();
        this.scale = new Scale();
        this.xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        this.yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        getTransforms().addAll(translate, scale, xRotate, yRotate);
    }

    public Scale getScale() {
        return scale;
    }

    public Translate getTranslate() {
        return translate;
    }

    public Rotate getxRotate() {
        return xRotate;
    }

    public Rotate getyRotate() {
        return yRotate;
    }
}
