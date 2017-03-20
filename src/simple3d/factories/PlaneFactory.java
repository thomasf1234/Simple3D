package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import simple3d.SimpleMeshView;

/**
 * Created by tfisher on 05/03/2017.
 */
public class PlaneFactory {
    public static SimpleMeshView build(double scale, Color color) {
        TriangleMesh planeMesh = new TriangleMesh();
        ObservableFloatArray points = planeMesh.getPoints();
        ObservableFaceArray faces = planeMesh.getFaces();

        //dummy textCoords
        planeMesh.getTexCoords().addAll(0,0);

        Point3D[] vertices = new Point3D[4];
        vertices[0] = new Point3D(-1, 1, 0);
        vertices[1] = new Point3D(1, 1, 0);
        vertices[2] = new Point3D(1, -1, 0);
        vertices[3] = new Point3D(-1, -1, 0);

        for (Point3D vertex : vertices) {
            points.addAll((float) (vertex.getX() * (scale/2)), (float) (vertex.getY() * (scale/2)), (float) (vertex.getZ() * (scale/2)));
        }

        faces.addAll(0,0, 1,0, 2,0,
                0,0, 2,0, 3,0);

        SimpleMeshView plane = new SimpleMeshView(planeMesh);
        plane.setDrawMode(DrawMode.FILL);
        plane.setCullFace(CullFace.NONE);
        PhongMaterial material = new PhongMaterial(color);
        plane.setMaterial(material);

        return plane;
    }
}
