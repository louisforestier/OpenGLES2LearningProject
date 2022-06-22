package fr.univ_poitiers.dptinfo.algo3d.mesh;

import android.opengl.Matrix;

import fr.univ_poitiers.dptinfo.algo3d.gameobject.Component;
import fr.univ_poitiers.dptinfo.algo3d.gameobject.GameObject;
import fr.univ_poitiers.dptinfo.algo3d.shaders.DepthShader;
import fr.univ_poitiers.dptinfo.algo3d.shaders.ShaderManager;

/**
 * Component to render the mesh of the GameObject.
 */
public class MeshRenderer extends Component {

    /**
     * The material used by the renderer.
     */
    private Material material;

    /**
     * Constructor.
     * @param gameObject - the gameobject linked to the component
     */
    public MeshRenderer(GameObject gameObject) {
        super(gameObject);
    }

    /**
     * Returns the material.
     * @return the material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the renderer material to the parameter.
     * @param material - material to be used by the renderer.
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Call the {@link Mesh#initGraphics()} of the mesh contained in the {@link MeshFilter}, if they are not null, to send the data to the GPU.
     */
    @Override
    public void start() {
        MeshFilter mf = gameObject.getCompotent(MeshFilter.class);
        material.getShader().use();
        if (mf != null && mf.getMesh() != null)
            gameObject.getCompotent(MeshFilter.class).getMesh().initGraphics();
    }

    /**
     * Call the {@link #renderShadow()} method.
     */
    @Override
    public void update() {
        renderShadow();
    }

    /**
     * Update the model view matrix of the {@link DepthShader} and call the {@link Mesh#draw(DepthShader)} of the mesh contained in the {@link MeshFilter}, if they are not null, to draw the shadow of the mesh in the shadow map.
     */
    private void renderShadow() {
        MeshFilter mf = gameObject.getCompotent(MeshFilter.class);
        if (mf != null && mf.getMesh() != null) {
            float[] modelviewmatrix = new float[16];
            Matrix.multiplyMM(modelviewmatrix, 0, ShaderManager.getInstance().getDepthShader().getViewMatrix(), 0, transform.getGlobalModelMatrix(), 0);
            ShaderManager.getInstance().getDepthShader().use();
            ShaderManager.getInstance().getDepthShader().setModelViewMatrix(modelviewmatrix);
            gameObject.getCompotent(MeshFilter.class).getMesh().draw(ShaderManager.getInstance().getDepthShader());
        }
    }


    /**
     * Call the {@link #render()} method.
     */
    @Override
    public void lateUpdate() {
        render();
    }

    /**
     * Update the model view matrix of the material shader, call the {@link Material#update()} method and then draw the mesh depending on the material DrawMode.
     */
    private void render() {
        if (gameObject.getCompotent(MeshFilter.class) != null) {
            float[] modelviewmatrix = new float[16];
            Matrix.multiplyMM(modelviewmatrix, 0, material.getShader().getViewMatrix(), 0, transform.getGlobalModelMatrix(), 0);
            material.getShader().use();
            material.update();
            material.getShader().setModelViewMatrix(modelviewmatrix);
            material.getShader().setModelMatrix(transform.getGlobalModelMatrix());
            switch (material.getDrawMode()) {
                case TRIANGLES:
                    gameObject.getCompotent(MeshFilter.class).getMesh().draw(material.getShader());
                    break;
                case WIREFRAME:
                    gameObject.getCompotent(MeshFilter.class).getMesh().drawLinesOnly(material.getShader());
                    break;
                case TRIANGLES_AND_WIREFRAME:
                    gameObject.getCompotent(MeshFilter.class).getMesh().drawWithLines(material.getShader());
            }
        }
    }


}
