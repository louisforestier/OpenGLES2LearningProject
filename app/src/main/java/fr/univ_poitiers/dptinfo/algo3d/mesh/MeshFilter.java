package fr.univ_poitiers.dptinfo.algo3d.mesh;

import fr.univ_poitiers.dptinfo.algo3d.gameobject.Component;
import fr.univ_poitiers.dptinfo.algo3d.gameobject.GameObject;

/**
 * Component to contain the mesh of the object.
 * Allow to create Meshes without linking them to a GameObject, because only the MeshFilter is really linked.
 * The name is a direct rip off of the Unity Component of the same name.
 */
public class MeshFilter extends Component {

    /**
     * The Mesh contained by this component.
     */
    private Mesh mesh;

    /**
     * Constructor.
     * @param gameObject - the gameobject linked to the component
     */
    public MeshFilter(GameObject gameObject) {
        super(gameObject);
    }

    /**
     * Returns the contained mesh.
     * @return the contained mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Set the contained mesh to the parameter.
     * @param mesh - the mesh that will be displayed when rendering this GameObject.
     */
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

}
