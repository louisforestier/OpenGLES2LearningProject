package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;

/**
 * Basic Shader class to use multiple lights with the blinn phong formula.
 */
public class BlinnPhongMultipleLightShaders extends MultipleLightingShaders {

    /**
     * Constructor. nothing to do, everything is done in the super class...
     *
     * @param context
     */
    public BlinnPhongMultipleLightShaders(Context context) {
        super(context);
    }


    /**
     * Create the shader program with the blinn_phong_with_multiple_lights glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context, "blinn_phong_with_multiple_lights_vert.glsl", "blinn_phong_with_multiple_lights_frag.glsl");
    }

}
