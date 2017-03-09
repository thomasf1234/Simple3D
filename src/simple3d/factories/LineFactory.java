package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

/**
 * Created by tfisher on 05/03/2017.
 */
public class LineFactory {
    public static MeshView build(Point3D p0, Point3D p1, Color color) {
        TriangleMesh lineMesh = new TriangleMesh();
        ObservableFloatArray points = lineMesh.getPoints();
        ObservableFaceArray faces = lineMesh.getFaces();

        //dummy textCoords
        lineMesh.getTexCoords().addAll(0,0);

        Point3D p2 = p0.midpoint(p1);
        points.addAll((float) p0.getX(), (float) p0.getY(), (float) p0.getZ());
        points.addAll((float) p1.getX(), (float) p1.getY(), (float) p1.getZ());
        points.addAll((float) p2.getX(), (float) p2.getY(), (float) p2.getZ());

        faces.addAll(0,0, 1,0, 2,0);

        MeshView lineMeshView = new MeshView(lineMesh);
        lineMeshView.setDrawMode(DrawMode.LINE);
        lineMeshView.setCullFace(CullFace.NONE);
        PhongMaterial material = new PhongMaterial(color);
        lineMeshView.setMaterial(material);

        return lineMeshView;
    }
}
