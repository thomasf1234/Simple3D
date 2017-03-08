package simple3d.factories;

import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by tfisher on 07/03/2017.
 */
class GridFactoryTest {
    @Test
    void build() {
        MeshView grid = GridFactory.build(1,1);
        TriangleMesh gridMesh = (TriangleMesh) grid.getMesh();
        assertEquals("[-0.5, 0.0, -0.5, 0.5, 0.0, -0.5, -0.5, 0.0, 0.5, 0.5, 0.0, 0.5]", gridMesh.getPoints().toString());

        grid = GridFactory.build(10,1);
        gridMesh = (TriangleMesh) grid.getMesh();
        assertEquals("[-5.0, 0.0, -5.0, 5.0, 0.0, -5.0, -5.0, 0.0, 5.0, 5.0, 0.0, 5.0]", gridMesh.getPoints().toString());

        grid = GridFactory.build(10,2);
        gridMesh = (TriangleMesh) grid.getMesh();
        assertEquals("[-10.0, 0.0, -10.0, 0.0, 0.0, -10.0, 10.0, 0.0, -10.0, -10.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, -10.0, 0.0, 10.0, 0.0, 0.0, 10.0, 10.0, 0.0, 10.0]", gridMesh.getPoints().toString());

    }

}