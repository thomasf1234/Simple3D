package simple3d;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Rotate;

/**
 *
 * @author ad
 */
public class CameraMan {

    private static final int UPPER_ROTATE_BOUND = 80;
    private static final double NEAR_INFINITY = Math.pow(10, 10);
    public final Rotate xRotate;
    public final Rotate yRotate;
    private Point3D target;
    private Camera camera;
    private double x;
    private double y;
    private double z;
    private SubScene scene;

    public CameraMan(SubScene scene) {
        this.scene = scene;
        this.xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        this.yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        //default target is at infinity along Z to simulate no target
        this.target = Rotate.Z_AXIS.multiply(NEAR_INFINITY);
        initializeCamera();
    }

    public void setTarget(Point3D target) {
        this.target = target;
    }

    public void setTarget(double x, double y, double z) {
        this.target = new Point3D(x, y, z);
    }

    public void removeTarget() {
        setTarget(getForward().multiply(NEAR_INFINITY));
    }

    public void faceTarget() {
        Point3D forward = getForward();

        double xRotation = Math.toDegrees(Math.asin(-forward.getY()));
        double yRotation = Math.toDegrees(Math.atan2(forward.getX(), forward.getZ()));

        //must set xRotate axis for the correct rotation to follow
        this.xRotate.setAxis(getRight());
        this.xRotate.setAngle(xRotation);
        this.yRotate.setAngle(yRotation);
    }

    public double getTargetDistance() {
        return getPosition().distance(target);
    }

    public Point3D getPosition() {
        return new Point3D(this.x, this.y, this.z);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    public void setPosition(Point3D position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
        setCameraPosition();
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        setCameraPosition();
    }

    private void setCameraPosition() {
        this.camera.setTranslateX(this.x);
        this.camera.setTranslateY(this.y);
        this.camera.setTranslateZ(this.z);
    }

    public void moveForward(double value) {
        setPosition(getPosition().add(getForward().multiply(value)));
        faceTarget();
    }

    //returns unit vector towards target
    public Point3D getForward() {
        return this.target.subtract(getPosition()).normalize();
    }

    public Point3D getRight() {
        Point3D forward = getForward();
        return Rotate.Y_AXIS.crossProduct(forward.getX(), 0, forward.getZ()).normalize();
    }

    public Point3D getUp() {
        //return Rotate.Y_AXIS.multiply(-1);
        return getRight().crossProduct(getForward()).normalize();
    }

    public void moveRight(double value) {
        setPosition(getPosition().add(getRight().multiply(value)));
        this.xRotate.setAxis(getRight());
        faceTarget();
    }

    public void moveUp(double value) {
        //upper and lower bounds for up/down
        if ((value > 0 && this.xRotate.getAngle() > -UPPER_ROTATE_BOUND) || (value < 0 && this.xRotate.getAngle() < UPPER_ROTATE_BOUND)) {
            setPosition(getPosition().add(getUp().multiply(value)));
            faceTarget();
        }
    }

    public void reset() {
        this.setPosition(Point3D.ZERO);
        this.xRotate.setAxis(Rotate.X_AXIS);
        this.xRotate.setAngle(0);
        this.yRotate.setAngle(0);
    }

    public void initializeCamera() {
        this.camera = new PerspectiveCamera(true);
        this.camera.setNearClip(0.01);
        this.camera.setFarClip(100000.0);
        this.camera.getTransforms().addAll(this.xRotate, this.yRotate);
        this.scene.setCamera(this.camera);
        setCameraPosition();
    }

    public WritableImage render() {
        return this.scene.snapshot(null, null);
    }
}
