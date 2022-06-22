package fr.univ_poitiers.dptinfo.algo3d.gameobject;

import android.opengl.Matrix;
import fr.univ_poitiers.dptinfo.algo3d.Vec3f;

/**
 * Class to represent the Transform of a GameObject, to manipule its position, rotation and scale.
 */
public class Transform extends Component{

    /**
     * Vec3f storing the position.
     */
    private Vec3f pos;

    /**
     * Vec3f storing the rotation, with euler angles.
     */
    private Vec3f rot;

    /**
     * Vec3f storing the scale.
     */
    private Vec3f scale;

    /**
     * Constructor.
     * @param gameObject
     */
    public Transform(GameObject gameObject) {
        super(gameObject);
        transform = this;
        pos = new Vec3f();
        rot = new Vec3f();
        scale = new Vec3f(1.0f,1.0f,1.0f);
    }


    /**
     * Returns an array corresponding to the local Model Matrix of the GameObject.
     * @return float array with a size of 16, representing a 4*4 local model matrix
     */
    public float[] getLocalModelMatrix(){
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix,0,pos.x,pos.y,pos.z);
        Matrix.rotateM(modelMatrix, 0, rot.z, 0.0F, 0.0F, 1.0F);
        Matrix.rotateM(modelMatrix, 0, rot.x, 1.0F, 0.0F, 0.0F);
        Matrix.rotateM(modelMatrix, 0, rot.y, 0.0F, 1.0F, 0.0F);
        Matrix.scaleM(modelMatrix,0,scale.x,scale.y,scale.z);
        return modelMatrix;
    }

    /**
     * Returns an array corresponding to the global Model Matrix, by getting its parent global Model Matrix recursively.
     * Not a good solution, because every child call the method for its parent so each matrix are calculated as many times as they have children.
     * I did it because i found it easier to do without breaking my design and also because it was easier for me to debug, each object do its own calculation.
     * Should go the other way, the parent should give its matrix to its children.
     * @return float array with a size of 16, representing a 4*4 global model matrix
     */
    public float[] getGlobalModelMatrix(){
        if (gameObject.getParent() == null) {
            return getLocalModelMatrix();
        } else {
            float[] modelviewmatrix = new float[16];
            Matrix.multiplyMM(modelviewmatrix,0,gameObject.getParent().getTransform().getGlobalModelMatrix(),0, getLocalModelMatrix(),0);
            return modelviewmatrix;
        }
    }

    /**
     * Returns the global Model Matrix of the parent GameObject or the Identity Matrix if there is none.
     * @return float array with a size of 16, representing a 4*4 parent global model matrix
     */
    public float[] getParentModelViewMatrix() {
        float[] modelviewmatrix = new float[16];
        if (gameObject.getParent() == null) {

            Matrix.setIdentityM(modelviewmatrix,0);
            return modelviewmatrix;
        } else {
            this.gameObject.getParent().getTransform().getGlobalModelMatrix();
        }
        return new float[0];
    }

    /**
     * Returns the x attribute of the position Vector
     * @return
     */
    public float getPosx() {
        return pos.x;
    }

    /**
     * Set the x attribute of the position Vector
     * @return the modified instance
     */
    public Transform posx(float posx) {
        this.pos.x = posx;
        return this;
    }

    /**
     * Returns the y attribute of the position Vector
     * @return
     */
    public float getPosy() {
        return pos.y;
    }

    /**
     * Set the y attribute of the position Vector
     * @return the modified instance
     */
    public Transform posy(float posy) {
        this.pos.y = posy;
        return this;
    }

    /**
     * Returns the z attribute of the position Vector
     * @return
     */
    public float getPosz() {
        return pos.z;
    }

    /**
     * Set the z attribute of the position Vector
     * @return the modified instance
     */
    public Transform posz(float posz) {
        this.pos.z = posz;
        return this;
    }

    /**
     * Returns the x attribute of the rotation Vector
     * @return
     */
    public float getRotx() {
        return rot.x;
    }

    /**
     * Set the x attribute of the rotation Vector
     * @return the modified instance
     */
    public Transform rotx(float rotx) {
        this.rot.x = rotx;
        return this;
    }

    /**
     * Returns the y attribute of the rotation Vector
     * @return
     */
    public float getRoty() {
        return rot.y;
    }

    /**
     * Set the y attribute of the rotation Vector
     * @return the modified instance
     */
    public Transform roty(float roty) {
        this.rot.y = roty;
        return this;
    }

    /**
     * Returns the z attribute of the rotation Vector
     * @return
     */
    public float getRotz() {
        return rot.z;
    }

    /**
     * Set the z attribute of the rotation Vector
     * @return the modified instance
     */
    public Transform rotz(float rotz) {
        this.rot.z = rotz;
        return this;
    }

    /**
     * Returns the x attribute of the scale Vector
     * @return
     */
    public float getScalex() {
        return scale.x;
    }

    /**
     * Set the x attribute of the scale Vector
     * @return the modified instance
     */
    public Transform scalex(float scalex) {
        this.scale.x = scalex;
        return this;
    }

    /**
     * Returns the y attribute of the scale Vector
     * @return
     */
    public float getScaley() {
        return scale.y;
    }

    /**
     * Set the y attribute of the scale Vector
     * @return the modified instance
     */
    public Transform scaley(float scaley) {
        this.scale.y = scaley;
        return this;
    }

    /**
     * Returns the z attribute of the scale Vector
     * @return
     */
    public float getScalez() {
        return scale.z;
    }

    /**
     * Set the z attribute of the scale Vector
     * @return the modified instance
     */
    public Transform scalez(float scalez) {
        this.scale.z = scalez;
        return this;
    }
}
