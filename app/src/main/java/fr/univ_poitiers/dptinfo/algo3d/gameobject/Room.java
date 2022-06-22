package fr.univ_poitiers.dptinfo.algo3d.gameobject;


import android.opengl.GLES20;

import fr.univ_poitiers.dptinfo.algo3d.mesh.Material;
import fr.univ_poitiers.dptinfo.algo3d.mesh.Plane;

/**
 * Class to build rooms in the scene with specific dimensions, materials and doors.
 */
public class Room extends GameObject {

    /**
     * Constructor.
     * @param doors - array of boolean to decide wich walls have doors in the N,W,S,E order.
     * @param length - length of the room, on the z axis
     * @param width - width of the room, on the x axis
     * @param height - height of the room, on the y axis
     * @param floorMaterial - material of the floor
     * @param ceilingMaterial - material of the ceiling
     * @param wallMaterial - material of the walls
     */
    public Room(boolean[] doors, float length, float width, float height, Material floorMaterial, Material ceilingMaterial, Material wallMaterial) {
        super();
        GameObject floor = new GameObject();
        floor.setMesh(Plane.INSTANCE);
        floor.getTransform().scalex(width / 10).scalez(length / 10);
        floor.addMeshRenderer(floorMaterial);
        GameObject ceiling = new GameObject();
        ceiling.setMesh(Plane.INSTANCE);
        ceiling.getTransform().posy(height).rotx(180.f).scalex(width / 10).scalez(length / 10);
        ceiling.addMeshRenderer(ceilingMaterial);
        Wall w1 = new Wall(width, height, wallMaterial, doors[0]);
        w1.getTransform().posz(-length / 2);
        Wall w2 = new Wall(length, height, wallMaterial, doors[1]);
        w2.getTransform().roty(90.f).posx(-width / 2);
        Wall w3 = new Wall(width, height, wallMaterial, doors[2]);
        w3.getTransform().roty(180.f).posz(length / 2);
        Wall w4 = new Wall(length, height, wallMaterial, doors[3]);
        w4.getTransform().roty(270.f).posx(width / 2);
        this.addChildren(floor);
        this.addChildren(ceiling);
        this.addChildren(w1);
        this.addChildren(w2);
        this.addChildren(w3);
        this.addChildren(w4);
    }


    /**
     * Override of the lateUpdate method to add reflection effect with blending.
     */
    @Override
    public void lateUpdate() {
        for (Component c : components) {
            c.lateUpdate();
        }
        if (this.children.size() > 0) {
            for (GameObject go : this.children) {
                //si go est le sol
                if (go.getTransform().getPosx() == 0 && go.getTransform().getPosy() == 0 && go.getTransform().getPosz() == 0) {
                    GLES20.glEnable(GLES20.GL_BLEND);
                    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                    go.lateUpdate();
                    GLES20.glDisable(GLES20.GL_BLEND);
                } else {
                    go.lateUpdate();
                }
            }
        }
    }

}

