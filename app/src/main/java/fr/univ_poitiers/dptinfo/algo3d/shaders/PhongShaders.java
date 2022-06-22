package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;

/**
 * Shader class to use lightning with the phong formula.
 */
public class PhongShaders extends LightingShaders {

    /**
     * Constructor. nothing to do, everything is done in the super class...
     *
     * @param context
     */
    public PhongShaders(Context context) {
        super(context);
    }

    /**
     * Create the shader program with the phong glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context, "phong_vert.glsl", "phong_frag.glsl");
    }
}
