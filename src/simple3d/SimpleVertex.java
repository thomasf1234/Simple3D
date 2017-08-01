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
    private Translate baseTranslate;
    private Translate translate;
    private Scale scale;
    private final Rotate xRotate;
    private final Rotate yRotate;

    public SimpleVertex() {
        super(RADIUS);
        this.baseTranslate = new Translate();
        this.scale = new Scale();
        this.translate = new Translate();
        this.xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        this.yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        getTransforms().addAll(translate, baseTranslate, xRotate, yRotate);
    }

    public Translate getTranslate() {
        return translate;
    }

    public Translate getBaseTranslate() {
        return baseTranslate;
    }

    public void setScale(double x, double y, double z) {
        scale.setX(x);
        scale.setY(y);
        scale.setZ(z);
    }

    public void setBaseTranslate(double x, double y, double z) {
        baseTranslate.setX(x * scale.getX());
        baseTranslate.setY(y * scale.getY());
        baseTranslate.setZ(z * scale.getZ());
    }

    public void rotate(double angleX, double angleY) {
        xRotate.setPivotX(-baseTranslate.getX());
        xRotate.setPivotY(-baseTranslate.getY());
        xRotate.setPivotZ(-baseTranslate.getZ());
        yRotate.setPivotX(-baseTranslate.getX());
        yRotate.setPivotY(-baseTranslate.getY());
        yRotate.setPivotZ(-baseTranslate.getZ());

        xRotate.setAngle(angleX);
        yRotate.setAngle(angleY);
    }
}
