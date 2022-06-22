package fr.univ_poitiers.dptinfo.algo3d.objimporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.univ_poitiers.dptinfo.algo3d.mesh.Mesh;
import fr.univ_poitiers.dptinfo.algo3d.shaders.ShadingMode;

/**
 * Class to build a mesh from an .obj files.
 */
public class OBJImporter {

    /**
     * Static method to parse the obj file and build the position and triangles array.
     * Normals are either calculated or parsed if they are present in the obj file.
     * Texture coordinates are not tested yet.
     * Still a work in progress, should use a map of to verify if a vertex index, normal index paire already exists.
     * For now, all vertices are independent when normals are given, so the size of the Mesh can be quite big.
     * @param stream - stream of the corresponding .obj file, preferably the return of {@link android.content.res.Resources#openRawResource(int)}
     * @param shadingMode - how the normals should be calculated (flat or smooth).
     * @return the imported mesh
     */
    public static Mesh importOBJ(InputStream stream, ShadingMode shadingMode) {
        List<Float> verticesList = new ArrayList<>();
        List<Float> normalsList = new ArrayList<>();
        List<Float> texturesList = new ArrayList<>();
        List<OBJFace> trianglesList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String lineText;
        try {
            while ((lineText = reader.readLine()) != null) {
                if (!lineText.isEmpty()) {
                    String[] data = lineText.split(" ");
                    switch (data[0]) {
                        case "v":
                            verticesList.add(Float.parseFloat(data[1]));
                            verticesList.add(Float.parseFloat(data[2]));
                            verticesList.add(Float.parseFloat(data[3]));
                            break;
                        case "vn":
                            normalsList.add(Float.parseFloat(data[1]));
                            normalsList.add(Float.parseFloat(data[2]));
                            normalsList.add(Float.parseFloat(data[3]));
                            break;
                        case "vt":
                            texturesList.add(Float.parseFloat(data[1]));
                            texturesList.add(Float.parseFloat(data[2]));
                            break;

                        case "f":
                            trianglesList.add(new OBJFace(data));
                            break;
                        default:
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mesh mesh;
        float[] vertexpos;
        int[] triangles = new int[trianglesList.size() * 3];
        vertexpos = new float[trianglesList.size() * 9];
        if (normalsList.isEmpty() && texturesList.isEmpty()) {
            if (shadingMode == ShadingMode.SMOOTH_SHADING) {
                vertexpos = new float[verticesList.size()];
                for (int i = 0; i < verticesList.size(); i++) {
                    vertexpos[i] = verticesList.get(i);
                }
                for (int i = 0; i < trianglesList.size(); i++) {
                    triangles[i * 3] = trianglesList.get(i).getV1().getV() - 1;
                    triangles[i * 3 + 1] = trianglesList.get(i).getV2().getV() - 1;
                    triangles[i * 3 + 2] = trianglesList.get(i).getV3().getV() - 1;
                }
                mesh = new Mesh(vertexpos, triangles);
                mesh.calculateSmoothShadingNormals();

            } else {
                for (int i = 0; i < trianglesList.size(); i++) {
                    vertexpos[i * 9] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3);
                    vertexpos[i * 9 + 1] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3 + 1);
                    vertexpos[i * 9 + 2] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3 + 2);
                    vertexpos[i * 9 + 3] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3);
                    vertexpos[i * 9 + 4] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3 + 1);
                    vertexpos[i * 9 + 5] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3 + 2);
                    vertexpos[i * 9 + 6] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3);
                    vertexpos[i * 9 + 7] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3 + 1);
                    vertexpos[i * 9 + 8] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3 + 2);
                    triangles[i * 3] = i * 3;
                    triangles[i * 3 + 1] = i * 3 + 1;
                    triangles[i * 3 + 2] = i * 3 + 2;
                }
                mesh = new Mesh(vertexpos, triangles);
                mesh.calculateFlatShadingNormals();
            }
        } else if (texturesList.isEmpty()) {
            float[] normals = new float[vertexpos.length];
            for (int i = 0; i < trianglesList.size(); i++) {
                vertexpos[i * 9] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3);
                vertexpos[i * 9 + 1] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3 + 1);
                vertexpos[i * 9 + 2] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3 + 2);
                vertexpos[i * 9 + 3] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3);
                vertexpos[i * 9 + 4] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3 + 1);
                vertexpos[i * 9 + 5] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3 + 2);
                vertexpos[i * 9 + 6] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3);
                vertexpos[i * 9 + 7] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3 + 1);
                vertexpos[i * 9 + 8] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3 + 2);
                normals[i * 9] = normalsList.get((trianglesList.get(i).getV1().getVn() - 1) * 3);
                normals[i * 9 + 1] = normalsList.get((trianglesList.get(i).getV1().getVn() - 1) * 3 + 1);
                normals[i * 9 + 2] = normalsList.get((trianglesList.get(i).getV1().getVn() - 1) * 3 + 2);
                normals[i * 9 + 3] = normalsList.get((trianglesList.get(i).getV2().getVn() - 1) * 3);
                normals[i * 9 + 4] = normalsList.get((trianglesList.get(i).getV2().getVn() - 1) * 3 + 1);
                normals[i * 9 + 5] = normalsList.get((trianglesList.get(i).getV2().getVn() - 1) * 3 + 2);
                normals[i * 9 + 6] = normalsList.get((trianglesList.get(i).getV3().getVn() - 1) * 3);
                normals[i * 9 + 7] = normalsList.get((trianglesList.get(i).getV3().getVn() - 1) * 3 + 1);
                normals[i * 9 + 8] = normalsList.get((trianglesList.get(i).getV3().getVn() - 1) * 3 + 2);
                triangles[i * 3] = i * 3;
                triangles[i * 3 + 1] = i * 3 + 1;
                triangles[i * 3 + 2] = i * 3 + 2;
            }
            mesh = new Mesh(vertexpos, triangles, normals);
        } else {
            float[] normals = new float[vertexpos.length];
            float[] textures = new float[vertexpos.length / 3 * 2];
            for (int i = 0; i < trianglesList.size(); i++) {
                vertexpos[i * 9] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3);
                vertexpos[i * 9 + 1] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3 + 1);
                vertexpos[i * 9 + 2] = verticesList.get((trianglesList.get(i).getV1().getV() - 1) * 3 + 2);
                vertexpos[i * 9 + 3] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3);
                vertexpos[i * 9 + 4] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3 + 1);
                vertexpos[i * 9 + 5] = verticesList.get((trianglesList.get(i).getV2().getV() - 1) * 3 + 2);
                vertexpos[i * 9 + 6] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3);
                vertexpos[i * 9 + 7] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3 + 1);
                vertexpos[i * 9 + 8] = verticesList.get((trianglesList.get(i).getV3().getV() - 1) * 3 + 2);
                textures[i * 6] = texturesList.get((trianglesList.get(i).getV1().getVt() - 1) * 2);
                textures[i * 6 + 1] = texturesList.get((trianglesList.get(i).getV1().getVt() - 1) * 2 + 1);
                textures[i * 6 + 2] = texturesList.get((trianglesList.get(i).getV2().getVt() - 1) * 2);
                textures[i * 6 + 3] = texturesList.get((trianglesList.get(i).getV2().getVt() - 1) * 2 + 1);
                textures[i * 6 + 4] = texturesList.get((trianglesList.get(i).getV3().getVt() - 1) * 2);
                textures[i * 6 + 5] = texturesList.get((trianglesList.get(i).getV3().getVt() - 1) * 2 + 1);
                normals[i * 9] = normalsList.get((trianglesList.get(i).getV1().getVn() - 1) * 3);
                normals[i * 9 + 1] = normalsList.get((trianglesList.get(i).getV1().getVn() - 1) * 3 + 1);
                normals[i * 9 + 2] = normalsList.get((trianglesList.get(i).getV1().getVn() - 1) * 3 + 2);
                normals[i * 9 + 3] = normalsList.get((trianglesList.get(i).getV2().getVn() - 1) * 3);
                normals[i * 9 + 4] = normalsList.get((trianglesList.get(i).getV2().getVn() - 1) * 3 + 1);
                normals[i * 9 + 5] = normalsList.get((trianglesList.get(i).getV2().getVn() - 1) * 3 + 2);
                normals[i * 9 + 6] = normalsList.get((trianglesList.get(i).getV3().getVn() - 1) * 3);
                normals[i * 9 + 7] = normalsList.get((trianglesList.get(i).getV3().getVn() - 1) * 3 + 1);
                normals[i * 9 + 8] = normalsList.get((trianglesList.get(i).getV3().getVn() - 1) * 3 + 2);
                triangles[i * 3] = i * 3;
                triangles[i * 3 + 1] = i * 3 + 1;
                triangles[i * 3 + 2] = i * 3 + 2;
            }
            mesh = new Mesh(vertexpos, triangles, normals, textures);
        }
        return mesh;
    }

}
