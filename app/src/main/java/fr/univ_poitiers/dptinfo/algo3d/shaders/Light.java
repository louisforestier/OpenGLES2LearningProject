package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.opengl.Matrix;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.gameobject.Component;
import fr.univ_poitiers.dptinfo.algo3d.gameobject.GameObject;

/**
 * Component to make object emit light.
 */
public class Light extends Component {

    /**
     * Type of light.
     * By default, a point light.
     */
    private LightType type = LightType.POINT;
    /**
     * Position of the light.
     */
    private float[] position;
    /**
     * Direction of the light.
     */
    private float[] direction;
    /**
     * Ambient color of the light.
     * By default, {@link MyGLRenderer#darkgray}.
     */
    private float[] ambient = MyGLRenderer.darkgray;
    /**
     * Diffuse color of the light.
     * By default, {@link MyGLRenderer#lightgray}.
     */
    private float[] diffuse = MyGLRenderer.lightgray;
    /**
     * Specular color of the light.
     * By default, {@link MyGLRenderer#white}.
     */
    private float[] specular = MyGLRenderer.white;
    /**
     * Constant attenuation of the light.
     * By default, 1.
     */
    private float constant = 1.f;
    /**
     * Linear attenuation of the light.
     * By default, 0.09f.
     */
    private float linear = 0.09f;
    /**
     * Quadratic attenuation of the light.
     * By default 0.032f.
     */
    private float quadratic = 0.032f;
    /**
     * Cut off of the light.
     * Angle in degrees where the light is at its maximum.
     * By default, 12.5f.
     */
    private float cutOff = 12.5f;
    /**
     * Outer cut off of the light.
     * Angle in degrees where the light is visible.
     * The difference between the outer cut off and cut off gives the portion of the light where it fades out.
     * By default, 17.5f.
     */
    private float outerCutOff = 17.5f;

    /**
     * Constructor using the default value.
     * @param gameObject - the gameobject linked to the component
     */
    public Light(GameObject gameObject) {
        super(gameObject);
    }

    /**
     * Calculate the view position of the light with the given view matrix and returns it as an array of 3 float.
     * @param viewmatrix
     * @return position of the light
     */
    public float[] getPos(final float[] viewmatrix) {
        float[] lightPos = new float[4];
        Matrix.multiplyMV(lightPos, 0, viewmatrix, 0, new float[]{transform.getPosx(), transform.getPosy(), transform.getPosz(), 1.0f}, 0);
        return new float[]{lightPos[0], lightPos[1], lightPos[2]};
    }

    /**
     * Calculate the view direction of the light with the given view matrix and returns it as an array of 3 float.
     * @param viewmatrix
     * @return direction of the light
     */
    public float[] getDir(final float[] viewmatrix) {
        float[] lightDir = new float[4];
        float[] lightlocalDir = new float[]{
                (float) (Math.cos(Math.toRadians(transform.getRoty())) * Math.cos(Math.toRadians(transform.getRotx()))),
                (float) Math.sin(Math.toRadians(transform.getRotx())),
                (float) (Math.sin(Math.toRadians(transform.getRoty())) * Math.cos(Math.toRadians(transform.getRotx()))),
                0.f
        };
        Matrix.multiplyMV(lightDir, 0, viewmatrix, 0, lightlocalDir, 0);
        return new float[]{lightDir[0], lightDir[1], lightDir[2]};
    }


    /**
     * Initialize the shader uniform variables related to lights.
     * Should be usable by every shader inheriting from {@link LightingShaders} and {@link MultipleLightingShaders}.
     * Although it was not tested with {@link LightingShaders} because the {@link ShaderManager} does not support them yet.
     * @param shaders
     * @param modelviewmatrix
     */
    private void initLighting(BasicShaders shaders, final float[] modelviewmatrix) {
        shaders.use();
        position = getPos(modelviewmatrix);
        direction = getDir(modelviewmatrix);
        if (shaders.useTypeLight()) {
            if (shaders instanceof MultipleLightingShaders) {
                MultipleLightingShaders mls = (MultipleLightingShaders) shaders;
                switch (type) {
                    case DIRECTIONAL:
                        mls.setDirLight(this);
                        break;
                    case POINT:
                        mls.setPointLight(this);
                        break;
                    case SPOT:
                        mls.setSpotLight(this);
                        break;
                }
            }
            else {
                BlinnPhongTypeLightShaders tls = (BlinnPhongTypeLightShaders) shaders;
                tls.setLightType(type.getValue());
                tls.setLightPosition(position);
                tls.setAmbiantLight(ambient);
                tls.setLightColor(diffuse);
                tls.setLightSpecular(specular);
                tls.setLightAttenuation(constant,linear,quadratic);
                tls.setLightDirection(direction);
                tls.setCutOff(cutOff);
                tls.setOuterCutOff(outerCutOff);
            }
        }
        else {
            if (shaders instanceof LightingShaders){
                LightingShaders ls = (LightingShaders) shaders;
                ls.setLightPosition(position);
                ls.setAmbiantLight(ambient);
                ls.setLightColor(diffuse);
                ls.setLightSpecular(specular);
                ls.setLightAttenuation(constant,linear,quadratic);
            }
        }
    }


    /**
     *Call the {@link #initLighting(BasicShaders, float[])} method for every shader referenced by the {@link ShaderManager}.
     */
    @Override
    public void earlyUpdate() {
        super.earlyUpdate();
        for (MultipleLightingShaders s : ShaderManager.getInstance().getShaders().values()) {
            float[] modelviewmatrix = new float[16];
            Matrix.multiplyMM(modelviewmatrix, 0, s.getViewMatrix(), 0, transform.getParentModelViewMatrix(), 0);
            initLighting(s, modelviewmatrix);
        }
    }

    /**
     * Returns the light type.
     * @return the type of the light
     */
    public LightType getType() {
        return type;
    }

    /**
     * Set the light type.
     * @param type
     */
    public void setType(LightType type) {
        this.type = type;
    }

    /**
     * Returns the light position.
     * @return the position of the light
     */
    public float[] getPosition() {
        return position;
    }

    /**
     * Set the light position.
     * @param position
     */
    public void setPosition(float[] position) {
        this.position = position;
    }

    /**
     * Returns the light direction.
     * @return the direction of the light
     */
    public float[] getDirection() {
        return direction;
    }

    /**
     * Set the light direction.
     * @param direction
     */
    public void setDirection(float[] direction) {
        this.direction = direction;
    }

    /**
     * Returns the light ambient color.
     * @return the ambient color of the light
     */
    public float[] getAmbient() {
        return ambient;
    }

    /**
     * Set the light ambient color.
     * @param ambient
     */
    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
    }

    /**
     * Returns the light diffuse color.
     * @return the diffuse color of the light
     */
    public float[] getDiffuse() {
        return diffuse;
    }

    /**
     * Set the light ambient color.
     * @param diffuse
     */
    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }

    /**
     * Returns the light specular color.
     * @return the specular color of the light
     */
    public float[] getSpecular() {
        return specular;
    }

    /**
     * Set the light ambient color.
     * @param specular
     */
    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    /**
     * Returns the light constant attenuation.
     * @return constant attenuation of the light
     */
    public float getConstant() {
        return constant;
    }

    /**
     * Set the light constant attenuation.
     * @param constant
     */
    public void setConstant(float constant) {
        this.constant = constant;
    }

    /**
     * Returns the light linear attenuation.
     * @return linear attenuation of the light
     */
    public float getLinear() {
        return linear;
    }

    /**
     * Set the light linear attenuation.
     * @param linear
     */
    public void setLinear(float linear) {
        this.linear = linear;
    }

    /**
     * Returns the light quadratic attenuation.
     * @return quadratic attenuation of the light
     */
    public float getQuadratic() {
        return quadratic;
    }

    /**
     * Set the light quadratic attenuation.
     * @param quadratic
     */
    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }

    /**
     * Returns the light cut off converted in Radians.
     * @return the cut off of the light in Radians
     */
    public float getCutOff() {
        return (float) Math.cos(Math.toRadians(cutOff));
    }

    /**
     * Set the light cut off
     * @param cutOff - cut off in degrees
     */
    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    /**
     * Returns the light outer cut off converted in Radians.
     * @return the outer cut off of the light in Radians
     */
    public float getOuterCutOff() {
        return (float) Math.cos(Math.toRadians(outerCutOff));
    }

    /**
     * Set the light outer cut off
     * @param outerCutOff - outer cut off in degrees
     */
    public void setOuterCutOff(float outerCutOff) {
        this.outerCutOff = outerCutOff;
    }
}
