package fr.univ_poitiers.dptinfo.algo3d.mesh;


//TODO :
// - revoir l'objimporter pour conserver les sommets qui peuvent etre communs, avec une hashmap de paire<vertex,normal>
// - faire la réflexion par stencil buffer
// - réparer les textures des murs avec portes
// - mettre des textures sur les autres objets, notamment les obj
// - mettre à jour le readme
// - implanter des ombres pour spot et point light https://learnopengl.com/Advanced-Lighting/Shadows/Shadow-Mapping
// - Avec API inférieur à 30, plusieurs erreurs :
//    - avec émulateur API 25 :
//        "a vertex attribute array is uninitialized. Skipping corresponding vertex attribute" lors de l'appel de renderShadow dès que j'appelle la méthode glDrawElements pour n'importe quel objet. Pourtant, je n'ai pas cette erreur si je commente l'appel à renderShadow donc tout se passe correctement lors de renderScene.
//    - avec émulateur API 28 :
//        "a vertex attribute index out of boundary is detected. Skipping corresponding vertex attribute. buf=0xe79a5a10
//        Out of bounds vertex attribute info: clientArray? 0 attribute 2 vbo 216 allocedBufferSize 1648 bufferDataSpecified? 1 wantedStart 0 wantedEnd 20416"
//        Encore déclenché par renderShadow

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to calculate the mesh of a sphere in two different ways.
 */
public class Sphere extends Mesh {

    /**
     * Array to store the vertex position of an icosphere
     */
    private float[] vertexposIco;
    /**
     * Array to store the triangles of an icosphere
     */
    private int[] trianglesIco;
    /**
     * Number of current vertices.
     */
    private int nbIndicesV;
    /**
     * Number of current triangles.
     */
    private int nbIndicesT;

    /**
     * Map to store the paires of vertices already calculated.
     */
    private Map<Pair<Integer, Integer>, Integer> middleVertices = new HashMap<>();

    /**
     * Constructor of UV sphere.
     * @param slice - number of slices composing the silence. Must be superior to 2.
     * @param quarter - number of quarters composing the silence. Must be superior to 2.
     */
    public Sphere(int slice, int quarter) {
        if (quarter < 3) throw new IllegalArgumentException("Quarter must be superior to 2.");
        if (slice < 3) throw new IllegalArgumentException("Slice must be superior to 2.");
        float r = 1.f;
        vertexpos = new float[((slice - 1) * (quarter + 1) + 2) * 3];
        int k = 0;
        for (int i = 1; i < slice; i++) {
            double theta = Math.toRadians(90.0 - (180.0 / slice) * i);
            for (int j = 0; j <= quarter; j++) {
                double phi = Math.toRadians((360.0 / quarter) * j);
                // formule pour l'indice sans utiliser l'astuce de la variable k, (i - 1) * (1 + quarter) * 3 + (j * 3)
                vertexpos[k++] = (float) (r * Math.cos(theta) * Math.cos(phi));
                vertexpos[k++] = (float) (r * Math.sin(theta));
                vertexpos[k++] = (float) (r * Math.cos(theta) * Math.sin(phi));
            }
        }
        vertexpos[vertexpos.length - 5] = -1;
        vertexpos[vertexpos.length - 2] = 1;

        triangles = new int[(quarter * (slice - 2) * 2 + quarter * 2) * 3];
        k = 0;
        for (int i = 0; i < slice - 2; i++) {
            for (int j = 0; j < quarter; j++) {
                triangles[k++] = (i * (quarter + 1) + j);
                triangles[k++] = (i * (quarter + 1) + 1 + j);
                triangles[k++] = (i * (quarter + 1) + quarter + 2 + j);
                triangles[k++] = (i * (quarter + 1) + j);
                triangles[k++] = (i * (quarter + 1) + quarter + 2 + j);
                triangles[k++] = (i * (quarter + 1) + quarter + 1 + j);
            }
        }
        for (int i = 0; i < quarter; i++) {
            triangles[k++] = (vertexpos.length / 3 - 1);
            triangles[k++] = (i + 1);
            triangles[k++] = i;
        }
        for (int i = 0; i < quarter; i++) {
            triangles[k++] = (vertexpos.length / 3 - 2);
            triangles[k++] = (i - 1 + vertexpos.length / 3 - 2 - quarter);
            triangles[k++] = (i + vertexpos.length / 3 - 2 - quarter);
        }
        normals = vertexpos;
        k = 0;
        texturesCoord = new float[((slice - 1) * (quarter + 1) + 2) * 2];
        for (int i = slice - 1; i > 0; i--) {
            double theta = Math.toRadians(90.0 - (180.0 / slice) * i);
            for (int j = quarter; j >= 0; j--) {
                double phi = Math.toRadians((360.0 / quarter) * j);
                texturesCoord[k++] = (float) (phi / (2 * Math.PI));
                texturesCoord[k++] = (float) (theta / Math.PI + 0.5);
            }
        }
        texturesCoord[texturesCoord.length - 4] = 0;
        texturesCoord[texturesCoord.length - 3] = 1;
        texturesCoord[texturesCoord.length - 2] = 0;
        texturesCoord[texturesCoord.length - 1] = 0;
    }

    /**
     * Constructor of icosphere
     * @param nbDiv - number of subdivision to do.
     */
    public Sphere(int nbDiv) {
        vertexpos = new float[]{
                1.F, 0.F, 0.F,
                0.F, 1.F, 0.F,
                0.F, 0.F, 1.F,
                -1.F, 0.F, 0.F,
                0.F, -1.F, 0.F,
                0.F, 0.F, -1.F
        };
        nbIndicesV = vertexpos.length;
        triangles = new int[]{
                0, 1, 2,
                0, 5, 1,
                0, 4, 5,
                0, 2, 4,
                3, 5, 4,
                3, 4, 2,
                3, 1, 5,
                3, 2, 1

        };
        nbIndicesT = 0;
        if (nbDiv > 0) {
            trianglesIco = new int[(int) (8 * 3 * Math.pow(4, nbDiv))];
            int nbVertices = trianglesIco.length * 3 / 2 - trianglesIco.length + 6; //adapté de la relation d'euler : V - E + F = 2 => 3V - 3E + 3F = 2*3
            vertexposIco = new float[nbVertices];

            System.arraycopy(vertexpos, 0, vertexposIco, 0, vertexpos.length);
            for (int i = 0; i < triangles.length; i += 3) {
                divideTriangle(triangles[i], triangles[i + 1], triangles[i + 2], nbDiv);
            }
            vertexpos = vertexposIco;
            triangles = trianglesIco;
            normals = vertexpos;
        }

    }

    /**
     * Recursive method to divide a triangle until the nbDiv reached 0, then the vertices index are stored as a triangle
     * @param v1 - index of the first vertex of the triangle
     * @param v2 - index of the second vertex of the triangle
     * @param v3 - index of the thrid vertex of the triangle
     * @param nbDiv - number of remaining subdivisions
     */
    private void divideTriangle(int v1, int v2, int v3, int nbDiv) {
        if (nbDiv == 0) {
            trianglesIco[nbIndicesT] = v1;
            trianglesIco[nbIndicesT + 1] = v2;
            trianglesIco[nbIndicesT + 2] = v3;
            nbIndicesT += 3;
        } else {
            int middleV1V2 = getMiddle(v1, v2);
            int middleV2V3 = getMiddle(v2, v3);
            int middleV3V1 = getMiddle(v3, v1);
            divideTriangle(v1, middleV1V2, middleV3V1, nbDiv - 1);
            divideTriangle(middleV1V2, v2, middleV2V3, nbDiv - 1);
            divideTriangle(middleV2V3, v3, middleV3V1, nbDiv - 1);
            divideTriangle(middleV1V2, middleV2V3, middleV3V1, nbDiv - 1);
        }
    }

    /**
     * Calculate a new vertex in the middle of the v1v2 segment and returns its index.
     * @param v1 - index of the first vertex
     * @param v2 - index of the second vertex
     * @return
     */
    private int getMiddle(int v1, int v2) {
        float x = (vertexposIco[v1 * 3] + vertexposIco[v2 * 3]) / 2;
        float y = (vertexposIco[v1 * 3 + 1] + vertexposIco[v2 * 3 + 1]) / 2;
        float z = (vertexposIco[v1 * 3 + 2] + vertexposIco[v2 * 3 + 2]) / 2;
        double norm = Math.sqrt(x * x + y * y + z * z);
        x /= norm;
        y /= norm;
        z /= norm;
        Pair<Integer, Integer> key;
        if (v1 < v2) {
            key = new Pair<>(v1, v2);
        } else {
            key = new Pair<>(v2, v1);
        }
        if (middleVertices.containsKey(key)) {
            return middleVertices.get(key);
        } else {
            int vertex = (nbIndicesV / 3);
            vertexposIco[nbIndicesV] = x;
            vertexposIco[nbIndicesV + 1] = y;
            vertexposIco[nbIndicesV + 2] = z;
            nbIndicesV += 3;
            middleVertices.put(key, vertex);
            return vertex;
        }
    }


}
