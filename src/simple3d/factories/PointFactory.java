package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import simple3d.SimpleMeshView;

/**
 * Created by tfisher on 05/03/2017.
 */
public class PointFactory {
    public static SimpleMeshView build(Point3D p0, Color color) {
        TriangleMesh pointMesh = new TriangleMesh();
        ObservableFloatArray points = pointMesh.getPoints();
        ObservableFaceArray faces = pointMesh.getFaces();

        //dummy textCoords
        pointMesh.getTexCoords().addAll(0,0);

        points.addAll((float) p0.getX(), (float) p0.getY(), (float) p0.getZ());
        points.addAll((float) p0.getX(), (float) p0.getY(), (float) p0.getZ());
        points.addAll((float) p0.getX(), (float) p0.getY(), (float) p0.getZ());

        faces.addAll(0,0, 1,0, 2,0);

        SimpleMeshView pointMeshView = new SimpleMeshView(pointMesh);
        pointMeshView.setDrawMode(DrawMode.LINE);
        pointMeshView.setCullFace(CullFace.NONE);
        PhongMaterial material = new PhongMaterial(color);
        pointMeshView.setMaterial(material);

        return pointMeshView;
    }
}
