package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.shape.*;
//TODO : Face select mesh
//TODO : Color Picker and edit for face color (use text coord for all colours)
//TODO : PlaneFactory
//TODO :

//default color 1pixel
//set
//ColorPicker getValue
//update the meshView colors array (This array needs to be implemented)
//Generate the new color texture file
//add the textureCoords to the mesh.getTextCoords
//update the t0,t1,t2 of the selected face/s to the new textCoord
//
//

/**
 * Created by tfisher on 05/03/2017.
 */
public class CoordSystemFactory {
    //scale = width/height of individual square, horizCount = horizontal square count
    public static MeshView build(float scale, int horizCount) {
        TriangleMesh grid = new TriangleMesh();
        ObservableFloatArray points = grid.getPoints();
        ObservableFaceArray faces = grid.getFaces();
        grid.getTexCoords().addAll(0,0);



        Point3D p0 = new Point3D(-horizCount/2, 0, -horizCount/2);
        Point3D p1 = new Point3D(-horizCount/2, 0, horizCount/2);
        Point3D p2 = p0.midpoint(p1);
        
        MeshView gridMeshView = new MeshView(grid);
        gridMeshView.setDrawMode(DrawMode.LINE);
        gridMeshView.setCullFace(CullFace.NONE);

        return gridMeshView;
    }
}
