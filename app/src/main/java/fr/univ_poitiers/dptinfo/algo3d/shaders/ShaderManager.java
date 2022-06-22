package fr.univ_poitiers.dptinfo.algo3d.shaders;


import java.util.HashMap;
import java.util.Map;

/**
 * Class singleton to manage the shaders.
 */
public class ShaderManager {

    /**
     * Map of the shaders used in the scene with the class as key.
     */
    private Map<Class<? extends MultipleLightingShaders>, MultipleLightingShaders> shaders = new HashMap<>();

    /**
     * Shader to be used for the shadow map.
     */
    private DepthShader depthShader;

    /**
     * Singleton instance.
     */
    private static ShaderManager INSTANCE;

    /**
     * Constructor.
     */
    private ShaderManager() {
    }

    /**
     * Returns the singleton instance.
     * Thread proof with double check to not put the synchronized over all the method.
     * @return
     */
    public static ShaderManager getInstance() {
        if (INSTANCE != null)
            return INSTANCE;
        synchronized (ShaderManager.class) {
            if (INSTANCE == null)
                INSTANCE = new ShaderManager();
        }
        return INSTANCE;
    }

    /**
     * Returns the shader of the type in parameter stored in the Map.
     * Returns null if there is none.
     * @param type
     * @return shader
     */
    public MultipleLightingShaders getShader(Class<? extends MultipleLightingShaders> type) {
        return shaders.get(type);
    }

    /**
     * Returns the Map of shaders.
     * @return map of shaders
     */
    public Map<Class<? extends MultipleLightingShaders>, MultipleLightingShaders> getShaders() {
        return shaders;
    }

    /**
     * Add the given shaders to the map
     * @param shaders
     */
    public void addShaders(MultipleLightingShaders shaders) {
        this.shaders.put(shaders.getClass(), shaders);
    }

    /**
     * Returns the Depth shader.
     * @return
     */
    public DepthShader getDepthShader() {
        return depthShader;
    }

    /**
     * Set the depth shader.
     * @param depthShader
     */
    public void setDepthShader(DepthShader depthShader) {
        this.depthShader = depthShader;
    }
}
