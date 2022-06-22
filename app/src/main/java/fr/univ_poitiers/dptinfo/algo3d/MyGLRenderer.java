package fr.univ_poitiers.dptinfo.algo3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import fr.univ_poitiers.dptinfo.algo3d.shaders.Light;
import fr.univ_poitiers.dptinfo.algo3d.shaders.MultipleLightingShaders;
import fr.univ_poitiers.dptinfo.algo3d.shaders.ShaderManager;
import fr.univ_poitiers.dptinfo.algo3d.shaders.DepthShader;
import fr.univ_poitiers.dptinfo.algo3d.shaders.ShadowShaders;


/**
 * Class to represent the rendering of the scene
 *
 * @author Philippe Meseure
 * @version 1.0
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    /**
     * Some useful colors...
     */
    static public final float[] black = {0.0F, 0.0F, 0.0F, 1.F};
    static public final float[] white = {1.0F, 1.0F, 1.0F, 1.F};
    static public final float[] gray = {0.5F, 0.5F, 0.5F, 1.F};
    static public final float[] lightgray = {0.8F, 0.8F, 0.8F, 1.F};
    static public final float[] darkgray = {0.2F, 0.2F, 0.2F, 1.F};
    static public final float[] red = {1.F, 0.F, 0.F, 1.F};
    static public final float[] green = {0.F, 1.F, 0.F, 1.F};
    static public final float[] blue = {0.F, 0.F, 1.F, 1.F};
    static public final float[] yellow = {1.F, 1.F, 0.F, 1.F};
    static public final float[] magenta = {1.F, 0.F, 1.F, 1.F};
    static public final float[] cyan = {0.F, 1.0F, 1.F, 1.F};
    static public final float[] orange = {1.F, 0.5F, 0.F, 1.F};
    /**
     * Reference to the scene environment
     */
    private Scene scene;
    /**
     * Reference to the OpenGL surface view
     */
    private GLSurfaceView view;


    /**
     * Projection matrix to provide to the shader
     */
    private final float[] projectionmatrix = new float[16];

    /**
     * Light space matrix to provide to the shaders
     */
    private float[] lightSpaceMatrix = new float[16];


    /**
     * @return the scene environment
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * @return the surface view
     */
    public GLSurfaceView getView() {
        return this.view;
    }


    /**
     * Constructor
     *
     * @param view  OpenGL surface view
     * @param scene the scene environment
     */
    public MyGLRenderer(final GLSurfaceView view, final Scene scene) {
        this.view = view;
        this.scene = scene;
    }

    /**
     * general routine called when the support drawing function is created
     * Aims at initializing all graphics data
     *
     * @param unused
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Create shader
        ShaderManager.getInstance().getShaders().clear();
        ShaderManager.getInstance().addShaders(new ShadowShaders(this.view.getContext()));
        ShaderManager.getInstance().setDepthShader( new DepthShader(this.view.getContext()));
        checkGlError("Shader Creation");

        scene.initGraphics(this);
    }


    /**
     * Called on newdisplay events
     * Render the shadow map of the directional light, then the whole scene with reflection.
     *
     * @param unused
     */
    @Override
    public void onDrawFrame(GL10 unused) {

        // Display the scene:
        // Drawing the scene is mandatory, since display buffers are swapped in any case.
        renderShadowMap(scene.directionalLight.getCompotent(Light.class));
        renderScene(scene);
        // Dirty mode, so post a new display request to loop
        this.view.requestRender();
    }

    /**
     * Called when the surface has changed (screen rotation, for instance)
     * always called at the beginning, before first display.
     *
     * @param unused
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 unused, final int width, final int height) {
        // Adjust the viewport based on geometry changes,
        GLES20.glViewport(0, 0, width, height);
        generateShadowFBO();
        // Compute projection matrix
        float ratio = (float) width / height;
        if (width > height) // Landscape mode
        {
            Matrix.perspectiveM(this.projectionmatrix, 0, 60.F, ratio, 0.1F, 100.F);
        } else // Portrait mode
        {
            Matrix.perspectiveM(this.projectionmatrix, 0, 45.F, ratio, 0.1F, 100.F);
        }
        for (MultipleLightingShaders s : ShaderManager.getInstance().getShaders().values()) {
            s.use();
            s.setProjectionMatrix(this.projectionmatrix);
        }
    }

    /**
     * Utility method for debugging OpenGL calls.
     * If the operation is not successful, the check throws an error.
     *
     * @param gloperation - Name of the OpenGL call that was called
     */
    public static void checkGlError(String gloperation) {
        int firsterror, error;

        // Check if there is an error
        error = GLES20.glGetError();
        if (error == GLES20.GL_NO_ERROR) return;

        // In case of error, display the error list and throw an exception...
        firsterror = error;
        do {
            MainActivity.log("Gl Error " + error + " after " + gloperation);
            error = GLES20.glGetError();
        } while (error != GLES20.GL_NO_ERROR);
        throw new RuntimeException("GL Error " + firsterror + " after " + gloperation);

    }

    /**
     * Utility method to load a texture defined as a resource
     * This method is freely inspired from www.learnopengles.com
     *
     * @param context    Context of application
     * @param resourceId Id of the application resource to load
     *                   Typically, this resource is something like R.drawable.name_of_the_file
     * @return Texture handle
     */
    public static int loadTexture(final Context context, final int resourceId) {
        // Create a new texture handle to store the loaded texture
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            // Load the given ressource as a bitmap
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the allocated texture handle so that the following instructions are done
            // within this texture handle
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering parameters (can be changed to allow a better visualization)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Unbind texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            // Recycle the bitmap, it has been loaded into the graphics card memory and is no longer
            // used in the main memory
            bitmap.recycle();
        }
        return textureHandle[0];
    }

    /**
     * Frame Buffer Object id
     */
    int[] fboId;

    /**
     * Depth texture id
     */
    int[] depthTextureId;

    /**
     * Generate the Frame buffer and the depth texture.
     * It should be placed in the {@link Light#start()} method, ideally but this was just a test with one light.
     * This would allow me to generate one shadow map per light.
     */
    public void generateShadowFBO() {
        final int SHADOW_WIDTH = 2048, SHADOW_HEIGHT = 2048;

        fboId = new int[1];
        depthTextureId = new int[1];

        //Create the framebuffer object
        GLES20.glGenFramebuffers(1, fboId, 0);

        //Create the depth texture
        GLES20.glGenTextures(1, depthTextureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTextureId[0]);

        //Modify paramters of the depth texture
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId[0]);

        //Use the depth texture
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_INT, null);

        // Attach the depth texture as the framebuffer's depth buffer
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthTextureId[0], 0);

/*
        To debug the framebuffer :
        //Check FBO status
        int FBOstatus = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if (FBOstatus != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            MainActivity.log("GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");
            throw new RuntimeException("GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");
        }
*/
    }

    /**
     * Render the shadow map in the depth texture from the light view.
     * I use at the same time front face culling to generate shadows with the inner faces to get rid off shadow acne on solid objects
     * and bias to get rid off it on planes.
     * For now, its designed for a directional light only.
     * In the future, the first part of this method should be called in the {@link Light#earlyUpdate()} method to set the matrix in the depth shader.
     * The last part uses the scene, so either the lights know the scene and then the update is also done in earlyUpdate or the scene know the lights and calls an update for each light.
     * @param light
     */
    private void renderShadowMap(Light light) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId[0]);

        GLES20.glViewport(0, 0, 2048, 2048);

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        //Calculate the light projection matrix, the light view matrix and the light space matrix.
        float[] lightProjection = new float[16];
        Matrix.orthoM(lightProjection, 0, -10.f, 10.f, -10.f, 10.f, 0.1f, 50.f);
        float[] lightView = new float[16];
        float[] lightpos = light.getPos(light.getTransform().getParentModelViewMatrix());
        float[] lightdir = light.getDir(light.getTransform().getParentModelViewMatrix());
        Matrix.setLookAtM(lightView, 0, lightpos[0], lightpos[1], lightpos[2], lightpos[0] + lightdir[0], lightpos[1] + lightdir[1], lightpos[2] + lightdir[2], 0.f, 1.f, 0.f);
        lightSpaceMatrix = new float[16];
        Matrix.multiplyMM(lightSpaceMatrix, 0, lightProjection, 0, lightView, 0);

        ShaderManager.getInstance().getDepthShader().use();
        ShaderManager.getInstance().getDepthShader().setProjectionMatrix(lightProjection);
        ShaderManager.getInstance().getDepthShader().setModelViewMatrix(lightView);
        ShaderManager.getInstance().getDepthShader().setViewMatrix(lightView);

        //Prerender the scene with front face culling (except for Planes, but it is done in the draw method of the Plane)
        GLES20.glCullFace(GLES20.GL_FRONT);
        scene.update();
        GLES20.glCullFace(GLES20.GL_BACK);
    }

    /**
     * Render the scene with a reflexion on the floor, by using blending and rendering the scene two times.
     * @param scene
     */
    private void renderScene(Scene scene) {
        //Make the scene evoluate
        this.scene.step();

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glViewport(0, 0, view.getWidth(), view.getHeight());
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTextureId[0]);
        for (MultipleLightingShaders s : ShaderManager.getInstance().getShaders().values()) {
            s.use();
            s.setLightSpaceMatrix(lightSpaceMatrix);
            s.setDepthMap(1);
        }
        //Render the reflexion
        GLES20.glFrontFace(GLES20.GL_CW);
        scene.setUpReflexionMatrix();
        scene.earlyUpdate();
        scene.lateUpdate();
        //Render the real scene
        GLES20.glFrontFace(GLES20.GL_CCW);
        scene.setUpMatrix();
        scene.earlyUpdate();
        scene.lateUpdate();
    }
}
