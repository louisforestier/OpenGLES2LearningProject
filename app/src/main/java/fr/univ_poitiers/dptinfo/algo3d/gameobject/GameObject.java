package fr.univ_poitiers.dptinfo.algo3d.gameobject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.univ_poitiers.dptinfo.algo3d.mesh.Material;
import fr.univ_poitiers.dptinfo.algo3d.mesh.Mesh;
import fr.univ_poitiers.dptinfo.algo3d.mesh.MeshFilter;
import fr.univ_poitiers.dptinfo.algo3d.mesh.MeshRenderer;

/**
 * Class to represent all the objects in the scene.
 */
public class GameObject {

    /**
     * The transform of the GameObject.
     * Is also in the component list but because this object is called frequently, bypassing the getComponent method is better.
     */
    private Transform transform;
    /**
     * The list of children of the GameObject.
     */
    protected List<GameObject> children = new ArrayList<>();
    /**
     * The list of components of the GameObject.
     */
    protected List<Component> components = new ArrayList<>();
    /**
     * The parent of the GameObject.
     */
    private GameObject parent = null;

    /**
     * Method to unreferenced the components, children and parent, especially to cut the cross referenced between the Transform and the GameObject.
     * @param gameObject
     */
    public static void destroy(GameObject gameObject){
        for (Component c : gameObject.components) {
            c.gameObject = null;
            c.transform = null;
        }
        gameObject.transform = null;
        gameObject.parent = null;
        gameObject.components.clear();
        gameObject.children.clear();
    }

    /**
     * Constructor.
     * Create a Transform component.
     */
    public GameObject() {
        this.addComponent(Transform.class);
        this.transform = (Transform) components.get(0);
    }

    /**
     * Add a MeshFilter component then set its Mesh to the given parameter.
     * @param mesh
     */
    public void setMesh(Mesh mesh) {
        addComponent(MeshFilter.class);
        getCompotent(MeshFilter.class).setMesh(mesh);
    }

    /**
     * Add a MeshRenderer component and set its Material to the given parameter.
     * @param material
     */
    public void addMeshRenderer(Material material) {
        addComponent(MeshRenderer.class);
        getCompotent(MeshRenderer.class).setMaterial(material);
    }

    /**
     * Add a component of the given type with introspection.
     * @param type
     * @param <T>
     */
    public <T extends Component> void addComponent(Class<T> type) {
        try {
            components.add(type.getDeclaredConstructor(GameObject.class).newInstance(this));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Access a component of the given type.
     * @param type
     * @param <T>
     * @return the component found, null if none was found.
     */
    public <T extends Component> T getCompotent(Class<T> type) {
        for (Component c : components) {
            if (type.isInstance(c))
                return type.cast(c);
        }
        return null;
    }

    /**
     * Access to the transform component.
     * @return the transform
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Add a child to the list of children and set the child parent to this instance.
     * @param child
     */
    public void addChildren(GameObject child) {
        this.children.add(child);
        child.parent = this;
    }

    /**
     * Access the parent of the GameObject.
     * @return
     */
    public GameObject getParent() {
        return parent;
    }

    /**
     * Method to be called at the start of the scene, for initialization.
     * Calls all {@link Component#start()} method for each component and calls this method for each child.
     */
    public void start() {
        for (Component c : components)
            c.start();
        if (this.children.size() > 0) {
            for (GameObject go : this.children) {
                go.start();
            }
        }
    }

    /**
     * Method to be called at the beginning of the render pass.
     * Calls all {@link Component#earlyUpdate()}  method for each component and calls this method for each child.
     */
    public void earlyUpdate() {
        for (Component c : components) {
            c.earlyUpdate();
        }
        if (this.children.size() > 0) {
            for (GameObject go : this.children) {
                go.earlyUpdate();
            }
        }
    }

    /**
     * Method to be called in the middle of the render pass.
     * Calls all {@link Component#update()} method for each component and calls this method for each child.
     */
    public void update() {
        for (Component c : components) {
            c.update();
        }
        if (this.children.size() > 0) {
            for (GameObject go : this.children) {
                go.update();
            }
        }
    }

    /**
     * Method to be called at the end of the render pass.
     * Calls all {@link Component#lateUpdate()} method for each component and calls this method for each child.
     */
    public void lateUpdate() {
        for (Component c : components) {
            c.lateUpdate();
        }
        if (this.children.size() > 0) {
            for (GameObject go : this.children) {
                go.lateUpdate();
            }
        }
    }
}
