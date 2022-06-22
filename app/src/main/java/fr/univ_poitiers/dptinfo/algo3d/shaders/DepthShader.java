package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;

/**
 * Shader class to generate the shadow map.
 */
public class DepthShader extends BasicShaders {


    /**
     * Constructor.
     *
     * @param context
     */
    public DepthShader(Context context) {
        super(context);
    }

    /**
     * Create the shader program with the depth glsl.
     * @param context - context of the application
     * @return the shader program handle
     */
    @Override
    public int createProgram(Context context) {
        return initializeShadersFromResources(context, "depth_vert.glsl", "depth_frag.glsl");
    }
}
