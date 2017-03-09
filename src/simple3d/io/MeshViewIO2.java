package simple3d.io;

/**
 * Created by tfisher on 07/03/2017.
 */

import javafx.collections.ObservableFloatArray;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import java.io.*;

/**
 *
 * @author ad
 */
public class MeshViewIO2 {

    //at byte [0]
    //vertexCount/{0..3} normalCount/{4..7} uvCount/{8..11}  faceCount/{12..15}
    //at byte [16]
    // v0X/{16..19} v0Y/{20..23} v0Z/{24..27}
    // v1X/{28..31} v1Y/{32..35} v1Z/{36..39}
    //..
    //at byte [16 + (3*4)*vertexCount]
    // n0X/[4] n0Y n0Z
    // n1X n1Y n1Z
    //..
    //at byte [16 + (3*4)*vertexCount + (3*4)*normalCount]
    // uv0X uv0Y
    // uv1X uv1Y
    //..
    //at byte [16 + (3*4)*vertexCount + (3*4)*normalCount + (2*4)*uvCount]
    //(#f0) v0 n0 t0 v1 n1 t1 v2 n2 t2
    //(#f1) v0 n0 t0 v1 n1 t1 v2 n2 t2
    //..
    public static void write(MeshView meshView, File outFile) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            oos = new ObjectOutputStream(fos);

            TriangleMesh mesh = (TriangleMesh) meshView.getMesh();
            ObservableFloatArray meshPoints = mesh.getPoints();
            ObservableFloatArray meshNormals = mesh.getNormals();
            ObservableFloatArray meshTextCoords = mesh.getTexCoords();
            ObservableFaceArray meshFaces = mesh.getFaces();

            int meshPointsLength = meshPoints.size();
            int meshNormalsLength = meshNormals.size();
            int meshTextCoordsLength = meshTextCoords.size();
            int meshFacesLength = meshFaces.size();

            if ((meshPointsLength % 3 == 0) &&
                    (meshNormalsLength % 3 == 0) &&
                    (meshTextCoordsLength % 2 == 0) &&
                    (meshFacesLength % 9 ==0)
                    ) {

                int vertexCount = meshPoints.size() / 3;
                int normalCount = meshNormals.size() / 3;
                int uvCount = meshTextCoords.size() / 2;
                int triangleCount = meshFaces.size() / 9;

                oos.writeInt(vertexCount);
                oos.writeInt(normalCount);
                oos.writeInt(uvCount);
                oos.writeInt(triangleCount);

                for(int i=0; i< vertexCount; ++i) {
                    int vertexIndex = i*3;

                    oos.writeFloat(meshPoints.get(vertexIndex));
                    oos.writeFloat(meshPoints.get(vertexIndex + 1));
                    oos.writeFloat(meshPoints.get(vertexIndex + 2));
                }

                for(int i=0; i< normalCount; ++i) {
                    int normalIndex = i*3;

                    oos.writeFloat(meshNormals.get(normalIndex));
                    oos.writeFloat(meshNormals.get(normalIndex + 1));
                    oos.writeFloat(meshNormals.get(normalIndex + 2));
                }

                for(int i=0; i< uvCount; ++i) {
                    int textCoordsIndex = i*2;

                    oos.writeFloat(meshTextCoords.get(textCoordsIndex));
                    oos.writeFloat(meshTextCoords.get(textCoordsIndex + 1));
                }

                for(int i=0; i< triangleCount; ++i) {
                    int triangleIndex = i*9;

                    oos.writeShort(meshFaces.get(triangleIndex));
                    oos.writeShort(meshFaces.get(triangleIndex + 1));
                    oos.writeShort(meshFaces.get(triangleIndex + 2));
                    oos.writeShort(meshFaces.get(triangleIndex + 3));
                    oos.writeShort(meshFaces.get(triangleIndex + 4));
                    oos.writeShort(meshFaces.get(triangleIndex + 5));
                    oos.writeShort(meshFaces.get(triangleIndex + 6));
                    oos.writeShort(meshFaces.get(triangleIndex + 7));
                    oos.writeShort(meshFaces.get(triangleIndex + 8));
                }

                oos.close();
            } else {
                throw new IllegalArgumentException("MeshView not well formed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch(IOException e) {
                    System.out.println("Failed to close stream");
                }
            }
        }
    }

    public static MeshView read(File inputFile)  {
        MeshView meshView = null;
        ObjectInputStream ois = null;

        try {
            if (inputFile.exists()) {
                FileInputStream fis = new FileInputStream(inputFile);
                ois = new ObjectInputStream(fis);

                int pointsCount = ois.readInt() * 3;
                int normalsCount = ois.readInt() * 3;
                int textCoordsCount = ois.readInt() * 2;
                int facesCount = ois.readInt() * 9;

                float[] points = new float[pointsCount];
                float[] normals = new float[normalsCount];
                float[] textCoords = new float[textCoordsCount];
                int[] faces = new int[facesCount];

                for (int i=0; i< pointsCount; ++i) {
                    points[i] = ois.readFloat();
                }

                for (int i=0; i< normalsCount; ++i) {
                    normals[i] = ois.readFloat();
                }

                for (int i=0; i< textCoordsCount; ++i) {
                    textCoords[i] = ois.readFloat();
                }

                for (int i=0; i< facesCount; ++i) {
                    faces[i] = ois.readShort();
                }
                ois.close();

                TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
                triangleMesh.getPoints().setAll(points);
                triangleMesh.getNormals().setAll(normals);
                triangleMesh.getTexCoords().setAll(textCoords);
                triangleMesh.getFaces().setAll(faces);

                meshView = new MeshView(triangleMesh);
                meshView.setCullFace(CullFace.BACK);
                meshView.setDrawMode(DrawMode.FILL);
                PhongMaterial material = new PhongMaterial();
                material.setDiffuseColor(Color.GRAY);
                material.setSpecularColor(Color.WHITE);
                material.setSpecularPower(10.0);
                meshView.setMaterial(material);
            } else {
                throw new FileNotFoundException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch(IOException e) {
                    System.out.println("Failed to close stream");
                }
            }
        }

        return meshView;
    }
}