package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Shader class to use multiple lights with the blinn phong formula, with textures.
 */
public class TexturesShaders extends MultipleLightingShaders {

    /**
     * GLSL attribute for vertex texture coordinate array.
     */
    protected int aVertexTexture;

    /**
     * GLSL uniform material texture unit.
     */
    protected int uTextureUnit;

    /**
     * GLSL uniform boolean to use textures or not.
     */
    protected int uTexturing;


    /**
     * Constructor. nothing to do, everything is done in the super class...
     *
     * @param context
     */
    public TexturesShaders(Context context) {
        super(context);
    }

    /**
     * Get all the uniform variables and attributes defined in the shaders
     */
    @Override
    public void findVariables() {
        super.findVariables();
        this.aVertexTexture = GLES20.glGetAttribLocation(this.shaderprogram, "aVertexTexture");
        if (this.aVertexTexture == -1)
            throw new RuntimeException("aVertexTexture not found in shaders");
        GLES20.glEnableVertexAttribArray(this.aVertexTexture);
        this.uTextureUnit = GLES20.glGetUniformLocation(this.shaderprogram, "uTextureUnit");
        if (this.uTextureUnit == -1)
            throw new RuntimeException("uTextureUnit not found in shaders");
        this.uTexturing = GLES20.glGetUniformLocation(this.shaderprogram, "uTexturing");
        if (this.uTexturing == -1) throw new RuntimeException("uTexturing not found in shaders");


    }

    /**
     * Create the shader program with the texture glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context, "texture_vert.glsl", "texture_frag.glsl");
    }

    /**
     * Set texture coordinate array for future drawings
     *
     * @param size  number of coordinates by texture
     * @param dtype type of coordinates
     */
    @Override
    public void setTexturePointer(int size, int dtype) {
        GLES20.glVertexAttribPointer(this.aVertexTexture, size, dtype, false, 0, 0);
    }

    /**
     * Set the texture unit of the material.
     * @param textureUnit - the texture
     */
    @Override
    public void setTextureUnit(final int textureUnit) {
        GLES20.glUniform1i(this.uTextureUnit, textureUnit);
    }

    /**
     * Set texturing of triangles.
     *
     * @param state on/off
     */
    @Override
    public void setTexturing(final boolean state) {
        if (this.uTexturing != -1) GLES20.glUniform1i(this.uTexturing, state ? 1 : 0);
    }
}
