package fr.univ_poitiers.dptinfo.algo3d.mesh;


import android.opengl.GLES20;

import fr.univ_poitiers.dptinfo.algo3d.shaders.DepthShader;

/**
 * Class to calculate a 10x10 plane composed of 200 triangles.
 */
public class Plane extends Mesh {

    /**
     * A static instance to be used. Can also be instantiated with a constructor.
     */
    public static final Plane INSTANCE = new Plane();


    /**
     * Constructor.
     */
    public Plane() {
        vertexpos = new float[11 * 11 * 3];
        triangles = new int[200 * 3];
        int k = 0;
        for (float z = -5; z < 6; z++) {
            for (float x = -5; x < 6; x++) {
                vertexpos[k++] = x;
                vertexpos[k++] = 0.f;
                vertexpos[k++] = z;
            }
        }
        k = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                triangles[k++] = (i * (10 + 1) + j);
                triangles[k++] = (i * (10 + 1) + 10 + 2 + j);
                triangles[k++] = (i * (10 + 1) + 1 + j);
                triangles[k++] = (i * (10 + 1) + j);
                triangles[k++] = (i * (10 + 1) + 10 + 1 + j);
                triangles[k++] = (i * (10 + 1) + 10 + 2 + j);
            }
        }


        normals = new float[11 * 11 * 3];
        for (int i = 0; i < normals.length; i += 3) {
            normals[i] = 0.f;
            normals[i + 1] = 1.f;
            normals[i + 2] = 0.f;
        }

        k = 0;
        texturesCoord = new float[11 * 11 * 2];
        for (float s = 0; s < 11; s++) {
            for (float t = 0; t < 11; t++) {
                texturesCoord[k++] = t / 10;
                texturesCoord[k++] = s / 10;
            }
        }
    }

    /**
     * Draw the shadow of the mesh with the back face culled.
     * This is done to not used front face culling for planes, as this method only works for solid objects with a volume.
     * @param shaders
     */
    @Override
    public void draw(DepthShader shaders) {
        GLES20.glCullFace(GLES20.GL_BACK);
        super.draw(shaders);
        GLES20.glCullFace(GLES20.GL_FRONT);
    }
}
