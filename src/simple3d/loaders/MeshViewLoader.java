package simple3d.loaders;

/**
 * Created by tfisher on 07/03/2017.
 */
import java.io.IOException;

import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import simple3d.util.XmlUtils;

/**
 *
 * @author ad
 */
public class MeshViewLoader {
    public static MeshView load(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        Element rootElement = XmlUtils.getRoot(xmlPath);
        Element objectElement = XmlUtils.getFirstChild(rootElement, "object");

        String name = objectElement.getAttribute("name");

        TriangleMesh mesh = new TriangleMesh();

        Element verticesElement = XmlUtils.getFirstChild(objectElement, "vertices");
        Element[] vertexElementsOrdered = XmlUtils.getChildren(verticesElement, "vertex");
        Point3D[] vertices = deriveVertices(vertexElementsOrdered);

        ObservableFloatArray meshPoints = mesh.getPoints();
        for (Point3D vertex : vertices) {
            meshPoints.addAll((float) vertex.getX(), (float) vertex.getY(), (float) vertex.getZ());
        }

        Element trianglesElement = XmlUtils.getFirstChild(objectElement, "triangles");
        Element[] triangleElements = XmlUtils.getChildren(trianglesElement, "triangle");

        ObservableFloatArray textCoords = mesh.getTexCoords();
        ObservableFaceArray faces = mesh.getFaces();

        for (int i = 0; i < triangleElements.length; ++i) {
            Element triangleElement = triangleElements[i];
            Element normalElement = XmlUtils.getFirstChild(triangleElement, "normal");
            double normalX = Double.parseDouble(normalElement.getAttribute("x"));
            double normalY = Double.parseDouble(normalElement.getAttribute("y"));
            double normalZ = Double.parseDouble(normalElement.getAttribute("z"));
            Point3D normal = new Point3D(normalX, normalY, normalZ);

            Element triangleVerticesElement = XmlUtils.getFirstChild(triangleElement, "vertices");
            Element[] triangleVertexElements = XmlUtils.getChildren(triangleVerticesElement, "vertex");

            int vertexCount = 3;
            int[] vertexIndicies = new int[vertexCount];
            Point2D[] uv = new Point2D[vertexCount];

            for(int j=0; j < vertexCount; j++) {
                vertexIndicies[j] = Integer.parseInt(triangleVertexElements[j].getAttribute("index"));

                Element uvElement = XmlUtils.getFirstChild(triangleVertexElements[j], "uv");
                double uvX = Double.parseDouble(uvElement.getAttribute("x"));
                double uvY = Double.parseDouble(uvElement.getAttribute("y"));

                uv[j] = new Point2D(uvX, uvY);
            }

            //get ordered
            int[] orderedVertexIndicies = new int[3];
            Point2D[] orderedUV = new Point2D[3];

            Point3D v01 = vertices[vertexIndicies[1]].subtract(vertices[vertexIndicies[0]]);
            Point3D v02 = vertices[vertexIndicies[2]].subtract(vertices[vertexIndicies[0]]);

            Point3D v01CrossV02 = v01.crossProduct(v02);

            if(v01CrossV02.dotProduct(normal) > 0) {
                orderedVertexIndicies[0] = vertexIndicies[0];
                orderedVertexIndicies[1] = vertexIndicies[1];
                orderedVertexIndicies[2] = vertexIndicies[2];
                orderedUV[0] = uv[0];
                orderedUV[1] = uv[1];
                orderedUV[2] = uv[2];
            } else {
                orderedVertexIndicies[0] = vertexIndicies[0];
                orderedVertexIndicies[1] = vertexIndicies[2];
                orderedVertexIndicies[2] = vertexIndicies[1];
                orderedUV[0] = uv[0];
                orderedUV[1] = uv[2];
                orderedUV[2] = uv[1];
            }

            textCoords.addAll(
                    (float) orderedUV[0].getX(), (float) orderedUV[0].getY(),
                    (float) orderedUV[1].getX(), (float) orderedUV[1].getY(),
                    (float) orderedUV[2].getX(), (float) orderedUV[2].getY()
            );

            faces.addAll(orderedVertexIndicies[0], i*3,
                    orderedVertexIndicies[1], (i*3)+1,
                    orderedVertexIndicies[2], (i*3)+2);
        }

        MeshView meshView = new MeshView(mesh);
        meshView.setCullFace(CullFace.BACK);
        meshView.setDrawMode(DrawMode.FILL);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GRAY);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(10.0);
        meshView.setMaterial(material);


        return meshView;
    }

    public static Point3D[] deriveVertices(Element[] vertexElementsOrdered) {
        Point3D[] vertices = new Point3D[vertexElementsOrdered.length];

        for (int i = 0; i < vertices.length; i++) {
            double x = Double.parseDouble(vertexElementsOrdered[i].getAttribute("x"));
            double y = Double.parseDouble(vertexElementsOrdered[i].getAttribute("y"));
            double z = Double.parseDouble(vertexElementsOrdered[i].getAttribute("z"));

            vertices[i] = new Point3D(x, y, z);
        }

        return vertices;
    }
}