package simple3d;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SnapshotParameters;
import javafx.scene.SubScene;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Rotate;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

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
        setDefaultTarget();
        initializeCamera();
    }

    public void setDefaultTarget() {
        //default target is at infinity along Z to simulate no target
        setTarget(Rotate.Z_AXIS.multiply(NEAR_INFINITY));
    }

    public void setTarget(Point3D target) {
        setTarget(target.getX(), target.getY(), target.getZ());
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
        xRotate.setAxis(getRight());
        xRotate.setAngle(xRotation);
        yRotate.setAngle(yRotation);
    }

    public double getTargetDistance() {
        return getPosition().distance(target);
    }

    public Point3D getPosition() {
        return new Point3D(getX(), getY(), getZ());
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
        camera.setTranslateX(this.x);
        camera.setTranslateY(this.y);
        camera.setTranslateZ(this.z);
    }

    public void moveForward(double value) {
        setPosition(getPosition().add(getForward().multiply(value)));
        faceTarget();
    }

    //returns unit vector towards target
    public Point3D getForward() {
        return this.target.subtract(getPosition()).normalize();
    }

    public Point3D getBackward() {
        return getForward().multiply(-1);
    }

    public Point3D getRight() {
        Point3D forward = getForward();
        return Rotate.Y_AXIS.crossProduct(forward.getX(), 0, forward.getZ()).normalize();
    }

    public Point3D getUp() {
        return getRight().crossProduct(getForward()).normalize();
    }

    public void moveRight(double value) {
        double r = getTargetDistance();
        double theta = value/r;
        Point3D targetPosition = new Point3D(target.getX(), target.getY(), target.getZ());
        Point3D newPosition = targetPosition.add(getBackward().multiply(r * Math.cos(theta)).add(getRight().multiply(r * Math.sin(theta))));

        double newX = newPosition.getX();
        double newY = getY();
        double newZ = newPosition.getZ();
        setPosition(newX, newY, newZ);
        this.xRotate.setAxis(getRight());
        faceTarget();
    }

    public void moveUp(double value) {
        //upper and lower bounds for up/down
        if ((value > 0 && this.xRotate.getAngle() > -UPPER_ROTATE_BOUND) || (value < 0 && this.xRotate.getAngle() < UPPER_ROTATE_BOUND)) {
            double r = getTargetDistance();
            double theta = value/r;
            Point3D targetPosition = new Point3D(target.getX(), target.getY(), target.getZ());
            Point3D newPosition = targetPosition.add(getBackward().multiply(r * Math.cos(theta)).add(getUp().multiply(r * Math.sin(theta))));

            double newX = newPosition.getX();
            double newY = newPosition.getY();
            double newZ = newPosition.getZ();
            setPosition(newX, newY, newZ);
            faceTarget();
        }
    }

    public void reset() {
        setPosition(Point3D.ZERO);
        this.xRotate.setAxis(Rotate.X_AXIS);
        this.xRotate.setAngle(0);
        this.yRotate.setAngle(0);
        setDefaultTarget();
    }

    private void initializeCamera() {
        if (camera == null) {
            this.camera = new PerspectiveCamera(true);
            camera.setNearClip(0.01);
            camera.setFarClip(100000.0);
            camera.getTransforms().addAll(xRotate, yRotate);
            scene.setCamera(camera);
            setCameraPosition();
        }
    }
//Doesn't work
//    public void render() {
//        WritableImage image = scene.snapshot(new SnapshotParameters(), null);
//
//        // TODO: probably use a file chooser here
//        File file = new File("snapshot.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
//    }
}
