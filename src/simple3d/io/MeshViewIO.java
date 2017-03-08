package simple3d.io;

/**
 * Created by tfisher on 07/03/2017.
 */
import java.io.File;
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

import java.io.File;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author ad
 */
public class MeshViewIO {
    public static File write(MeshView meshView) {
        File outFile = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // object element
            Element objectElement = doc.createElement("object");
            rootElement.appendChild(objectElement);

            // set name attribute to object element
            objectElement.setAttribute("name", "myObject");

            // vertices elements
            Element verticesElement = doc.createElement("vertices");

            TriangleMesh mesh = (TriangleMesh) meshView.getMesh();
            if (mesh.getPointElementSize() != 3) {
                throw new IllegalArgumentException("MeshView must be 3 dimensional: mesh.getPointElementSize must = 3");
            }
            //must be divisible by 3
            if (mesh.getPoints().size() % 3 != 0) {
                throw new IllegalArgumentException("MeshView must be 3 dimensional. mesh.getPoints.size() must be divisble by 3");
            }
            ObservableFloatArray meshPoints = mesh.getPoints();
            int vertexCount = meshPoints.size() / 3;
            verticesElement.setAttribute("count", String.valueOf(vertexCount));
            objectElement.appendChild(verticesElement);

            for (int i=0; i < vertexCount; ++i) {
                Element vertexElement = doc.createElement("vertex");
                int p0Index = i*3;
                vertexElement.setAttribute("x", String.valueOf(meshPoints.get(p0Index)));
                vertexElement.setAttribute("y", String.valueOf(meshPoints.get(p0Index+1)));
                vertexElement.setAttribute("z", String.valueOf(meshPoints.get(p0Index+2)));

                verticesElement.appendChild(vertexElement);
            }

            // triangles elements
            Element trianglesElement = doc.createElement("triangles");
            objectElement.appendChild(trianglesElement);

            ObservableFaceArray faces = mesh.getFaces();
            ObservableFloatArray textCoords = mesh.getTexCoords();
            ObservableFloatArray meshNormals = mesh.getNormals();

            //must be divisible by 6
            if (faces.size() % 6 != 0) {
                throw new IllegalArgumentException("MeshView must be well formed. Faces should follow p0,t0,p1,t1,p2,t2 where p0,p1,p2 are pointIndices of the face, and t0,t1,t2 are the textureIndicies, both with respect to getPoints(), and getTextcoords().");
            }

            int faceCount = faces.size() / 6;

            for (int i=0; i < faceCount; ++ i) {
                Element triangleElement = doc.createElement("triangle");
                int faceIndex = i*6;

                Element triangleNormalElement = doc.createElement("normal");
                triangleNormalElement.setAttribute("x", String.valueOf(meshNormals.get(faceIndex)));
                triangleNormalElement.setAttribute("y", String.valueOf(meshNormals.get(faceIndex + 1)));
                triangleNormalElement.setAttribute("z", String.valueOf(meshNormals.get(faceIndex + 2)));
                triangleElement.appendChild(triangleNormalElement);

                Element triangleVerticesElement = doc.createElement("vertices");

                for (int j=0; j<3; ++j) {
                    Element triangleVertexElement = doc.createElement("vertex");

                    int vertexIndex = faces.get(faceIndex + (j*2));
                    int textureIndex = faces.get(faceIndex + (j*2) + 1);

                    triangleVertexElement.setAttribute("index", String.valueOf(vertexIndex));

                    Element triangleUvElement = doc.createElement("uv");

                    float uvX = textCoords.get(textureIndex*2);
                    float uvY = textCoords.get((textureIndex*2) + 1);

                    triangleUvElement.setAttribute("x", String.valueOf(uvX));
                    triangleUvElement.setAttribute("y", String.valueOf(uvY));
                    triangleVertexElement.appendChild(triangleUvElement);
                    triangleVerticesElement.appendChild(triangleVertexElement);
                }

                triangleElement.appendChild(triangleVerticesElement);
                trianglesElement.appendChild(triangleElement);
            }




            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            outFile = new File("tmp/myObject.xml");
            StreamResult result = new StreamResult(outFile);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

        return outFile;
    }

    public static MeshView read(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        Element rootElement = XmlUtils.getRoot(xmlPath);
        Element objectElement = XmlUtils.getFirstChild(rootElement, "object");

        String name = objectElement.getAttribute("name");

        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);

        Element verticesElement = XmlUtils.getFirstChild(objectElement, "vertices");
        Element[] vertexElementsOrdered = XmlUtils.getChildren(verticesElement, "vertex");

        Point3D[] vertices = new Point3D[vertexElementsOrdered.length];

        for (int i = 0; i < vertices.length; i++) {
            double x = Double.parseDouble(vertexElementsOrdered[i].getAttribute("x"));
            double y = Double.parseDouble(vertexElementsOrdered[i].getAttribute("y"));
            double z = Double.parseDouble(vertexElementsOrdered[i].getAttribute("z"));

            vertices[i] = new Point3D(x, y, z);
        }

        ObservableFloatArray meshPoints = mesh.getPoints();
        for (Point3D vertex : vertices) {
            meshPoints.addAll((float) vertex.getX(), (float) vertex.getY(), (float) vertex.getZ());
        }

        Element trianglesElement = XmlUtils.getFirstChild(objectElement, "triangles");
        Element[] triangleElements = XmlUtils.getChildren(trianglesElement, "triangle");

        ObservableFloatArray textCoords = mesh.getTexCoords();
        ObservableFloatArray meshNormals = mesh.getNormals();
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

            faces.addAll(orderedVertexIndicies[0], i*3, i*2,
                    orderedVertexIndicies[1], (i*3)+1, (i*2)+1,
                    orderedVertexIndicies[2], (i*3)+2, (i*2)+2);

            meshNormals.addAll((float) normalX,(float) normalY, (float) normalZ,
                    (float) normalX,(float) normalY, (float) normalZ,
                    (float) normalX,(float) normalY, (float) normalZ);
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
}