package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;
import android.opengl.GLES20;

import fr.univ_poitiers.dptinfo.algo3d.MainActivity;

/**
 * Shader class to use lightning with the blinn phong formula and different types of light.
 */
public class BlinnPhongTypeLightShaders extends LightingShaders {

    /**
     * GLSL uniform light type
     */
    protected int uLightType;

    /**
     * GLSL uniform light direction (for spot and directional)
     */
    protected int uLightDirection;
    /**
     * GLSL light cut off (for spot)
     */
    private int uCutOff;
    /**
     * GLSL light outer cut off (for spot)
     */
    private int uOuterCutOff;

    /**
     * Constructor. nothing to do, everything is done in the super class...
     *
     * @param context
     */
    public BlinnPhongTypeLightShaders(Context context) {
        super(context);
    }

    /**
     * Get all the uniform variables and attributes defined in the shaders
     */
    @Override
    public void findVariables() {
        super.findVariables();
        this.uLightType = GLES20.glGetUniformLocation(this.shaderprogram, "uLightType");
        if (this.uLightType == -1) MainActivity.log("Warning:  uLightType not found in shaders...");
        this.uCutOff = GLES20.glGetUniformLocation(this.shaderprogram, "uCutOff");
        if (this.uCutOff == -1) MainActivity.log("Warning:  uCutOff not found in shaders...");
        this.uOuterCutOff = GLES20.glGetUniformLocation(this.shaderprogram, "uOuterCutOff");
        if (this.uOuterCutOff == -1)
            MainActivity.log("Warning:  uOuterCutOff not found in shaders...");
        this.uLightDirection = GLES20.glGetUniformLocation(this.shaderprogram, "uLightDir");
        if (this.uLightDirection == -1) throw new RuntimeException("uLightDir not found in shaders");
    }

    /**
     * Set the light type.
     * @param lightType - type of light
     */
    public void setLightType(int lightType) {
        GLES20.glUniform1i(this.uLightType, lightType);
    }

    /**
     * Set the light direction.
     * @param lightdir - direction of the light
     */
    public void setLightDirection(final float[] lightdir) {
        GLES20.glUniform3fv(this.uLightDirection, 1, lightdir, 0);
    }

    /**
     * Returns true to set the GLSL variables correctly in the {@link Light#initLighting(BasicShaders, float[])} method.
     * @return
     */
    @Override
    public boolean useTypeLight() {
        return true;
    }

    /**
     * Create the shader program with the blinn_phong_with_type_light glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context, "blinn_phong_with_type_light_vert.glsl", "blinn_phong_with_type_light_frag.glsl");
    }

    /**
     * Set the light cut off.
     * @param cutOff - cut off of the light
     */
    public void setCutOff(float cutOff) {
        GLES20.glUniform1f(this.uCutOff, cutOff);
    }

    /**
     * Set the light outer cut off.
     * @param outerCutOff - outer cut off of the light
     */
    public void setOuterCutOff(float outerCutOff) {
        GLES20.glUniform1f(this.uOuterCutOff, outerCutOff);
    }

}
