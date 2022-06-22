package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Shader class to use multiple lights with the blinn phong formula, with textures and some shadows for directional lights.
 * More a proof of concept than a real shadow implementation.
 */
public class ShadowShaders extends TexturesShaders {

    /**
     * GLSL uniform light space matrix to calculate the positions of the object in light space.
     */
    private int lightSpaceMatrix;

    /**
     * GLSL uniform model matrix to calculate the positions of the object in light space.
     */
    private int uModelMatrix;

    /**
     * GLSL uniform shadow map.
     */
    private int shadowMap;


    /**
     * Constructor. nothing to do, everything is done in the super class...
     *
     * @param context
     */

    public ShadowShaders(Context context) {
        super(context);
    }

    /**
     * Get all the uniform variables and attributes defined in the shaders
     */
    @Override
    public void findVariables() {
        super.findVariables();

        this.lightSpaceMatrix = GLES20.glGetUniformLocation(this.shaderprogram, "lightSpaceMatrix");
        if (this.lightSpaceMatrix == -1)
            throw new RuntimeException("lightSpaceMatrix not found in shaders");

        this.uModelMatrix = GLES20.glGetUniformLocation(this.shaderprogram, "uModelMatrix");
        if (this.uModelMatrix == -1)
            throw new RuntimeException("uModelMatrix not found in shaders");

        this.shadowMap = GLES20.glGetUniformLocation(this.shaderprogram, "shadowMap");
        if (this.shadowMap == -1) throw new RuntimeException("shadowMap not found in shaders");

    }

    /**
     * Create the shader program with the shadow glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context, "shadow_vert.glsl", "shadow_frag.glsl");
    }


    /**
     * Set the light space matrix.
     * @param matrix - light space matrix
     */
    @Override
    public void setLightSpaceMatrix(float[] matrix) {
        GLES20.glUniformMatrix4fv(this.lightSpaceMatrix, 1, false, matrix, 0);
    }

    /**
     * Set the model matrix
     * @param matrix - model matrix
     */
    @Override
    public void setModelMatrix(float[] matrix) {
        GLES20.glUniformMatrix4fv(this.uModelMatrix, 1, false, matrix, 0);
    }

    /**
     * Set the depth/shadow map.
     * @param depthMap
     */
    @Override
    public void setDepthMap(int depthMap) {
        GLES20.glUniform1i(this.shadowMap, depthMap);
    }

}
