package fr.univ_poitiers.dptinfo.algo3d.mesh;

/**
 * Class to calculate the mesh of a cube
 */
public class Cube extends Mesh {

    /**
     * Constructor
     * @param length - length of an edge of the cube, preferably 1.
     */
    public Cube(float length) {
        float x = length / 2;
        vertexpos = new float[]{
                -x, 0.F, -x,    // sommet 0
                x, 0.F, -x,     // sommet 1
                x, 0.F, x,      // sommet 2
                -x, 0.F, x,     // sommet 3
                -x, length, -x,   // sommet 4
                x, length, -x,    // sommet 5
                x, length, x,     // sommet 6
                -x, length, x,    // sommet 7
                -x, length, -x,   // sommet 4
                x, length, -x,    // sommet 5
                x, 0.F, -x,     // sommet 1
                -x, 0.F, -x,    // sommet 0
                x, length, -x,    // sommet 5
                x, length, x,     // sommet 6
                x, 0.F, x,      // sommet 2
                x, 0.F, -x,     // sommet 1
                -x, length, x,    // sommet 7
                -x, length, -x,   // sommet 4
                -x, 0.F, -x,    // sommet 0
                -x, 0.F, x,     // sommet 3
                x, length, x,     // sommet 6
                -x, length, x,    // sommet 7
                -x, 0.F, x,     // sommet 3
                x, 0.F, x,      // sommet 2
        };
        triangles = new int[]{
                0, 1, 2, //0123
                3, 0, 2,
                7, 6, 5,//7654
                4, 7, 5,
                8, 9, 10,//8-9-10-11
                11, 8, 10,
                12, 13, 14,//12-13-14-15
                15, 12, 14,
                16, 17, 18,//16-17-18-19
                19, 16, 18,
                20, 21, 22,//20-21-22-23
                23, 20, 22
        };

        this.calculateFlatShadingNormals();
    }
}
