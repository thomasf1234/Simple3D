package simple3d.util;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 07/03/2017.
 */
public class MeshUtils {
    public static Point3D[] getVertices(MeshView meshView) {
        TriangleMesh triangleMesh = (TriangleMesh) meshView.getMesh();
        ObservableFloatArray points = triangleMesh.getPoints();
        int vertexCount = points.size()/3;
        //throw exception if not triangle
        Point3D[] vertices = new Point3D[vertexCount];
        for (int i=0; i<vertexCount; ++i) {
            int vertexIndex = i*3;

            float x = points.get(vertexIndex);
            float y = points.get(vertexIndex + 1);
            float z = points.get(vertexIndex + 2);

            Point3D vertex = new Point3D(x, y, z);
            vertices[i] = vertex;
        }

        return vertices;
    }
}

