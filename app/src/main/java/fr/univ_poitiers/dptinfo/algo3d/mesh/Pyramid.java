package fr.univ_poitiers.dptinfo.algo3d.mesh;

/**
 * Class to calculate the mesh of a pyramid .
 * Can be used to make a cone with flat shading.
 */
public class Pyramid extends Mesh {

    /**
     * Constructor.
     * @param quarter - number of sides of the pyramid. Must be superior to 2.
     */
    public Pyramid(int quarter) {
        if (quarter < 3) throw new IllegalArgumentException("Quarter must be superior to 2.");
        vertexpos = new float[(2 * (quarter + 1) + 1 + quarter) * 3]; //+quarter pour le sommet,+1 pour la base et +1 pour le sommet de jointure de la base (le sommet répété pour theta = 0 et theta = 360)
        triangles = new int[quarter * 2 * 3];
        int k = 0;
        float r = 1.f;
        float height = 1.f;
        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            vertexpos[k++] = (float) (r * Math.cos(theta));
            vertexpos[k++] = 0.f;
            vertexpos[k++] = (float) (r * Math.sin(theta));
        }
        for (int i = 0; i <= quarter; i++) {
            double theta = Math.toRadians((360.0 / quarter) * i);
            vertexpos[k++] = (float) (r * Math.cos(theta));
            vertexpos[k++] = 0.f;
            vertexpos[k++] = (float) (r * Math.sin(theta));
        }
        for (int i = 0; i < quarter; i++) {
            vertexpos[k++] = 0.f;
            vertexpos[k++] = height;
            vertexpos[k++] = 0.f;
        }

        vertexpos[k++] = 0.f;
        vertexpos[k++] = 0.f;
        vertexpos[k++] = 0.f;

        k = 0;

        for (int i = 0; i < quarter; i++) {
            triangles[k++] = i;
            triangles[k++] = (quarter + 1) * 2 + i;
            triangles[k++] = i + 1;
            triangles[k++] = i + quarter + 1;
            triangles[k++] = i + quarter + 1 + 1;
            triangles[k++] = (vertexpos.length - 1) / 3;
        }
        this.calculateFlatShadingNormals();
    }
}
