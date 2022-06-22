package fr.univ_poitiers.dptinfo.algo3d.gameobject;

import fr.univ_poitiers.dptinfo.algo3d.mesh.Material;
import fr.univ_poitiers.dptinfo.algo3d.mesh.Plane;

/**
 * Class to build a wall with a door or not, with the {@link Plane} Mesh.
 */
public class Wall extends GameObject {

    /**
     * Constructor.
     * @param width - width of the wall
     * @param height - height of the wall
     * @param material - material of the wall
     * @param hasDoor - boolean to decide if the wall has a door
     */
    public Wall(float width, float height, Material material, boolean hasDoor) {
        super();
        getTransform().posy(height / 2);
        if (!hasDoor) {
            GameObject plane = new GameObject();
            plane.setMesh(Plane.INSTANCE);
            plane.addMeshRenderer(material);
            plane.getTransform().scalex(width / 10).scalez(height / 10).rotx(90.f);
            this.addChildren(plane);
        } else {
            GameObject left = new GameObject();
            left.setMesh(Plane.INSTANCE);
            left.addMeshRenderer(material);
            left.getTransform().scalex(((width - 1.f) / 2) / 10).scalez(height / 10).posx(-0.5f - (width - 1.f) / 4).rotx(90.f);
            this.addChildren(left);

            GameObject middle = new GameObject();
            middle.setMesh(Plane.INSTANCE);
            middle.addMeshRenderer(material);
            middle.getTransform().scalex(1.f / 10).scalez((height - 2.f) / 10).posy(2.f - (height / 2) + (height - 2.f) / 2).rotx(90.f);
            this.addChildren(middle);

            GameObject right = new GameObject();
            right.setMesh(Plane.INSTANCE);
            right.addMeshRenderer(material);
            right.getTransform().scalex(((width - 1.f) / 2) / 10).scalez(height / 10).posx(0.5f + (width - 1.f) / 4).rotx(90.f);
            this.addChildren(right);
        }
    }
}
