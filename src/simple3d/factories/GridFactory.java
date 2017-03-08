package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.shape.*;

/**
 * Created by tfisher on 05/03/2017.
 */
public class GridFactory {
    //scale = width/height of individual square, horizCount = horizontal square count
    public static MeshView build(float scale, int horizCount) {
        TriangleMesh grid = new TriangleMesh();
        ObservableFloatArray points = grid.getPoints();
        ObservableFaceArray faces = grid.getFaces();
        grid.getTexCoords().addAll(0,0);

        int totalCount = (int) Math.pow(horizCount, 2);

        int horizVertexCount = horizCount + 1;
        int totalVertCount = (int) Math.pow(horizVertexCount, 2);

        float x0 = -horizCount*scale/2;
        float z0 = -horizCount*scale/2;
        Point3D p0 = new Point3D(x0, 0, z0);


        for (int vN = 0; vN < totalVertCount; ++vN) {
            Point3D pN = p0.add((vN % horizVertexCount)*scale, 0, (vN/horizVertexCount)*scale);
            points.addAll((float) pN.getX(), (float) pN.getY(), (float) pN.getZ());
        }

        for (int fN = 0; fN < totalCount; ++fN) {
            int f0N = (fN % horizCount) + (horizVertexCount*(fN/horizCount));

            faces.addAll(
                    f0N,0,  (f0N + 1),0,  (f0N + horizVertexCount),0,
                    (f0N + 1),0,  (f0N + horizVertexCount + 1),0,  (f0N + horizVertexCount),0
            );
        }

        MeshView gridMeshView = new MeshView(grid);
        gridMeshView.setDrawMode(DrawMode.LINE);
        gridMeshView.setCullFace(CullFace.NONE);

        return gridMeshView;
    }
}
