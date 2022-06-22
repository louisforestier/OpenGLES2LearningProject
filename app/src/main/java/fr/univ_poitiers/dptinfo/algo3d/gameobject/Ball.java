package fr.univ_poitiers.dptinfo.algo3d.gameobject;


import fr.univ_poitiers.dptinfo.algo3d.mesh.Material;
import fr.univ_poitiers.dptinfo.algo3d.mesh.Mesh;
import fr.univ_poitiers.dptinfo.algo3d.mesh.Sphere;

/**
 * Class to add balls with a Sphere Mesh
 */
public class Ball extends GameObject {

    /**
     * Static Instance of the Sphere used to represent the balls.
     */
    static private Mesh sphere = new Sphere(50, 50);
    /**
     * Static boolean to know if the Sphere Mesh is already initialized or not.
     */
    static boolean isInitialized = false;

    /**
     * Constructor.
     * @param radius
     * @param posx
     * @param posz
     * @param material
     */
    public Ball(float radius, float posx, float posz, Material material) {
        super();
        this.setMesh(Ball.sphere);
        this.addMeshRenderer(material);
        this.getTransform().posx(posx).posz(posz).posy(radius).scalex(radius).scaley(radius).scalez(radius);
    }


    /**
     * Override of the start method to act as a sort of Singleton Pattern.
     */
    @Override
    public void start() {
        if (!isInitialized) {
            super.start();
            isInitialized = true;
        }
    }

    /**
     * The OGL context reset when application goes on pause, so we have to reset the boolean so the Sphere will be correctly initialized after resuming.
     */
    static public void onPause() {
        isInitialized = false;
    }
}
