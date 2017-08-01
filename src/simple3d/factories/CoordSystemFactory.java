package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import simple3d.SimpleMeshView;

/**
 * Created by tfisher on 05/03/2017.
 */
public class CoordSystemFactory {
    //https://stackoverflow.com/questions/26831871/coloring-individual-triangles-in-a-triangle-mesh-on-javafx
    public static SimpleMeshView build() {
        TriangleMesh grid = new TriangleMesh();
        ObservableFloatArray points = grid.getPoints();
        ObservableFaceArray faces = grid.getFaces();
        //need to give the float coordinates representing centre of the pixel, [0,0] to [1,1] everything is warped into this scaled square
        grid.getTexCoords().addAll(
                0.5f, 0.125f, //gray
                0.5f, 0.375f,  //red
                0.5f, 0.625f,  //blue
                0.5f, 0.875f  //green
        );

        PhongMaterial material = new PhongMaterial();
        Image texture = new Image("palette.png");
        material.setDiffuseMap(texture);

        //y axis
        Point3D yAxis0 = new Point3D(0, -10, 0);
        Point3D yAxis1 = new Point3D(0, 10, 0);
        Point3D yAxis2 = new Point3D(0, 0, 0);

        //add y axis vertices
        points.addAll((float) yAxis0.getX(), (float) yAxis0.getY(), (float) yAxis0.getZ());
        points.addAll((float) yAxis1.getX(), (float) yAxis1.getY(), (float) yAxis1.getZ());
        points.addAll((float) yAxis2.getX(), (float) yAxis2.getY(), (float) yAxis2.getZ());

        int yTextCoord = 3;
        //add y axis faces
        faces.addAll(
                0, yTextCoord,
                1, yTextCoord,
                2, yTextCoord);

        for (int i = -10; i < 11; ++i) {
            int xTextCoord;
            int zTextCoord;

            if (i == 0) {
                xTextCoord = 1;  //red
                zTextCoord = 2;  //blue
            } else {
                xTextCoord = 0;  //grey
                zTextCoord = 0;  //grey
            }

            //line parallel to x axis
            Point3D px0 = new Point3D(-10, 0, 1 * i);
            Point3D px1 = new Point3D(10, 0, 1 * i);
            Point3D px2 = px0.midpoint(px1);
            points.addAll((float) px0.getX(), (float) px0.getY(), (float) px0.getZ());
            points.addAll((float) px1.getX(), (float) px1.getY(), (float) px1.getZ());
            points.addAll((float) px2.getX(), (float) px2.getY(), (float) px2.getZ());

            //line parallel to z axis
            Point3D pz0 = new Point3D(1 * i, 0, -10);
            Point3D pz1 = new Point3D(1 * i, 0, 10);
            Point3D pz2 = pz0.midpoint(pz1);
            points.addAll((float) pz0.getX(), (float) pz0.getY(), (float) pz0.getZ());
            points.addAll((float) pz1.getX(), (float) pz1.getY(), (float) pz1.getZ());
            points.addAll((float) pz2.getX(), (float) pz2.getY(), (float) pz2.getZ());


            //add faces for both lines
            int faceIndex = ((i + 10) * 6) + 3;
            faces.addAll(
                    faceIndex, xTextCoord,
                    faceIndex + 1, xTextCoord,
                    faceIndex + 2, xTextCoord);

            faces.addAll(
                    faceIndex + 3, zTextCoord,
                    faceIndex + 4, zTextCoord,
                    faceIndex + 5, zTextCoord);
        }

        SimpleMeshView gridMeshView = new SimpleMeshView(grid);
        gridMeshView.setMaterial(material);
        gridMeshView.setDrawMode(DrawMode.LINE);
        gridMeshView.setCullFace(CullFace.NONE);

        return gridMeshView;
    }
}
