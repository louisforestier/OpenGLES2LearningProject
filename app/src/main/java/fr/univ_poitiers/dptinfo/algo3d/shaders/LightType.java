package fr.univ_poitiers.dptinfo.algo3d.shaders;

/**
 * Enum to indicates the type of light and get an integer value representing the type.
 * POINT = 0
 * DIRECTIONAL = 1
 * SPOT = 2
 */
public enum LightType {
    POINT(0),
    DIRECTIONAL(1),
    SPOT(2);

    /**
     * Value to be used for GLSL differentiation of the light type.
     */
    private final int value;

    /**
     * Constructor.
     * @param value
     */
    LightType(int value) {
        this.value = value;
    }

    /**
     * Returns the value corresponding to the light type.
     * @return
     */
    public int getValue() {
        return value;
    }
}
