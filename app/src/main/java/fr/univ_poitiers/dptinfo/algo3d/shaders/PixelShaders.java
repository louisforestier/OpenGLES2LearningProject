package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;

/**
 * Shader class to use lightning with the lambert formula.
 */
public class PixelShaders extends LightingShaders{

    /**
     * Constructor. nothing to do, everything is done in the super class...
     *
     * @param context
     */
    public PixelShaders(Context context) {
        super(context);
    }

    /**
     * Create the shader program with the pixel glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context,"pixel_vert.glsl","pixel_frag.glsl");
    }
}
