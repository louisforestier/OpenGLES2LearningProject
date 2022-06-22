package fr.univ_poitiers.dptinfo.algo3d.mesh;

import fr.univ_poitiers.dptinfo.algo3d.Vec3f;

/**
 * Class to calculate the mesh of a frustum.
 * Can be used to make a cylinder or a cone.
 */
public class Frustum extends Mesh {

    /**
     * Constructor
     * @param r1 - radius of the bottom of the frustum
     * @param r2 - radius of the top of the frustum
     * @param quarter - number of quarters composing the silence. Must be superior to 2.
     */
    public Frustum(float r1, float r2, int quarter) {
        if (quarter < 3) throw new IllegalArgumentException("Quarter must be superior to 2.");
        vertexpos = new float[((2 * 2) * (quarter + 1) + 2) * 3];
        triangles = new int[quarter * 4 * 3];

        int k = 0;

        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            vertexpos[k++] = (float) (r1 * Math.cos(theta));
            vertexpos[k++] = 0.f;
            vertexpos[k++] = (float) (r1 * Math.sin(theta));
            vertexpos[k++] = (float) (r2 * Math.cos(theta));
            vertexpos[k++] = 1.f;
            vertexpos[k++] = (float) (r2 * Math.sin(theta));
        }
        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            vertexpos[k++] = (float) (r1 * Math.cos(theta));
            vertexpos[k++] = 0.f;
            vertexpos[k++] = (float) (r1 * Math.sin(theta));
            vertexpos[k++] = (float) (r2 * Math.cos(theta));
            vertexpos[k++] = 1.f;
            vertexpos[k++] = (float) (r2 * Math.sin(theta));
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

        Vec3f p1 = new Vec3f(vertexpos[triangles[0] * 3], vertexpos[triangles[0] * 3 + 1], vertexpos[triangles[0] * 3 + 2]);
        Vec3f p2 = new Vec3f(vertexpos[triangles[1] * 3], vertexpos[triangles[1] * 3 + 1], vertexpos[triangles[1] * 3 + 2]);
        Vec3f p3 = new Vec3f(vertexpos[triangles[2] * 3], vertexpos[triangles[2] * 3 + 1], vertexpos[triangles[2] * 3 + 2]);
        Vec3f n = getNormal(p1, p2, p3);

        normals = new float[vertexpos.length];

        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            Vec3f n1 = new Vec3f((float) (r1 * Math.cos(theta)), n.y, (float) (r1 * Math.sin(theta)));
            Vec3f n2 = new Vec3f((float) (r2 * Math.cos(theta)), n.y, (float) (r2 * Math.sin(theta)));
            n1.normalize();
            n2.normalize();
            normals[k++] = n1.x;
            normals[k++] = n1.y;
            normals[k++] = n1.z;
            normals[k++] = n2.x;
            normals[k++] = n2.y;
            normals[k++] = n2.z;
        }
        for (int i = 0; i <= quarter; i++) {
            normals[k++] = 0.f;
            normals[k++] = -1.f;
            normals[k++] = 0.f;
            normals[k++] = 0.f;
            normals[k++] = 1.f;
            normals[k++] = 0.f;
        }
        //pas besoin d'initialiser les autres parties de ces sommets car java initialise déjà ces parties à 0, donc je ne modifie que la composante en y
        normals[normals.length - 5] = -1.f;
        normals[normals.length - 2] = 1.f;

    }
}
