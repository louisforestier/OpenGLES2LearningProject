package fr.univ_poitiers.dptinfo.algo3d.gameobject;

/**
 * Abstract class for all components of GameObject.
 */
public abstract class Component {

    /**
     * GameObject linked to the component.
     */
    protected GameObject gameObject;

    /**
     * Transform of the GameObject.
     */
    protected Transform transform;

    /**
     * Constructor.
     * @param gameObject - the gameobject linked to the component
     */
    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
    }

    /**
     * Accessor on the transform of the gameobject.
     * @return the transform of the gameobject
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Method to be called at the start of the scene, for initialization.
     */
    public void start(){}

    /**
     * Method to be called at the beginning of the render pass.
     */
    public void earlyUpdate(){}

    /**
     * Method to be called in the middle of the render pass.
     */
    public void update(){}

    /**
     * Method to be called at the end of the render pass.
     */
    public void lateUpdate(){}
}
