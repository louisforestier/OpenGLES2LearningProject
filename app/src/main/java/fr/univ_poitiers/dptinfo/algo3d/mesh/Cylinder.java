package fr.univ_poitiers.dptinfo.algo3d.mesh;

/**
 * Class to calculate the mesh of a cylinder
 */
public class Cylinder extends Mesh {

    /**
     * Constructor.
     * @param quarter - number of quarters composing the silence. Must be superior to 2.
     */
    public Cylinder(int quarter) {
        if (quarter < 3) throw new IllegalArgumentException("Quarter must be superior to 2.");
        vertexpos = new float[((2 * 2) * (quarter + 1) + 2) * 3];
        triangles = new int[quarter * 4 * 3];
        int k = 0;
        float r = 1.f;

        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            float x = (float) (r * Math.cos(theta));
            float z = (float) (r * Math.sin(theta));
            vertexpos[k++] = x;
            vertexpos[k++] = 0.f;
            vertexpos[k++] = z;
            vertexpos[k++] = x;
            vertexpos[k++] = 1.f;
            vertexpos[k++] = z;
        }
        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            float x = (float) (r * Math.cos(theta));
            float z = (float) (r * Math.sin(theta));
            vertexpos[k++] = x;
            vertexpos[k++] = 0.f;
            vertexpos[k++] = z;
            vertexpos[k++] = x;
            vertexpos[k++] = 1.f;
            vertexpos[k++] = z;
        }
        vertexpos[vertexpos.length - 5] = 0.f;
        vertexpos[vertexpos.length - 2] = 1.f;

        k = 0;
        for (int i = 0; i < quarter; i++) {
            triangles[k++] = (i * 2 + 1);
            triangles[k++] = (i * 2 + 3);
            triangles[k++] = (i * 2 + 2);
            triangles[k++] = (i * 2 + 1);
            triangles[k++] = (i * 2 + 2);
            triangles[k++] = (i * 2);
            triangles[k++] = ((i + quarter + 1) * 2 + 1);
            triangles[k++] = (vertexpos.length / 3 - 1);
            triangles[k++] = ((i + quarter + 1) * 2 + 3);
            triangles[k++] = ((i + quarter + 1) * 2);
            triangles[k++] = ((i + quarter + 1) * 2 + 2);
            triangles[k++] = (vertexpos.length / 3 - 2);
        }

        k = 0;
        normals = new float[vertexpos.length];

        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            float x = (float) (r * Math.cos(theta));
            float z = (float) (r * Math.sin(theta));
            normals[k++] = x;
            normals[k++] = 0.f;
            normals[k++] = z;
            normals[k++] = x;
            normals[k++] = 0.f;
            normals[k++] = z;
        }
        for (int i = 0; i <= quarter; i++) {
            normals[k++] = 0.f;
            normals[k++] = -1.f;
            normals[k++] = 0.f;
            normals[k++] = 0.f;
            normals[k++] = 1.f;
            normals[k++] = 0.f;
        }
        normals[normals.length - 5] = -1.f;
        normals[normals.length - 2] = 1.f;
    }
}
