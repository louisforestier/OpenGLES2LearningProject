package fr.univ_poitiers.dptinfo.algo3d.mesh;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.Vec3f;
import fr.univ_poitiers.dptinfo.algo3d.shaders.DepthShader;
import fr.univ_poitiers.dptinfo.algo3d.shaders.MultipleLightingShaders;

/**
 * Class to contain all datas for Meshes and binding/using the buffers on the GPU.
 */
public class Mesh {
    /**
     * Handle for the positions buffer.
     */
    private int glposbuffer;
    /**
     * Handle for the triangles buffer.
     */
    private int gltrianglesbuffer;
    /**
     * Handle for the normals buffer
     */
    private int glnormalbuffer;
    /**
     * Handle for the textures coordinates buffer
     */
    private int gltexturebuffer;

    /**
     * Array to store the vertex positions.
     */
    protected float[] vertexpos;
    /**
     * Array to store the triangles.
     */
    protected int[] triangles;
    /**
     * Array to store the normals.
     */
    protected float[] normals;
    /**
     * Array to store the textures coordinates.
     */
    protected float[] texturesCoord;


    /**
     * Default Constructor to be used by subclasses.
     */
    protected Mesh() {
    }

    /**
     * Constructor to be used mostly by the OBJImporter when no normals are given.
     * @param vertexpos - vertices position array
     * @param triangles - triangles array
     */
    public Mesh(float[] vertexpos, int[] triangles) {
        this.vertexpos = vertexpos;
        this.triangles = triangles;
        this.normals = new float[vertexpos.length];
    }

    /**
     * Constructor to be used mostly by the OBJImporter when normals are already in the file.
     * @param vertexpos - vertices position array
     * @param triangles - triangles array
     * @param normals - normals array
     */
    public Mesh(float[] vertexpos, int[] triangles, float[] normals) {
        this.vertexpos = vertexpos;
        this.triangles = triangles;
        this.normals = normals;
    }

    /**
     * Constructor to be used mostly by the OBJImporter when all vertex informations are present.
     * @param vertexpos - vertices position array
     * @param triangles - triangles array
     * @param normals - normals array
     * @param textures - texture coordinates array
     */
    public Mesh(float[] vertexpos, int[] triangles, float[] normals, float[] textures) {
        this.vertexpos = vertexpos;
        this.triangles = triangles;
        this.normals = normals;
        this.texturesCoord = textures;
    }

    /**
     * Calculate and set the normals of the Mesh for a flat shading appearance.
     */
    public void calculateFlatShadingNormals() {
        normals = new float[vertexpos.length];
        for (int i = 0; i < triangles.length; i += 3) {
            Vec3f p1 = new Vec3f(vertexpos[triangles[i] * 3], vertexpos[triangles[i] * 3 + 1], vertexpos[triangles[i] * 3 + 2]);
            Vec3f p2 = new Vec3f(vertexpos[triangles[i + 1] * 3], vertexpos[triangles[i + 1] * 3 + 1], vertexpos[triangles[i + 1] * 3 + 2]);
            Vec3f p3 = new Vec3f(vertexpos[triangles[i + 2] * 3], vertexpos[triangles[i + 2] * 3 + 1], vertexpos[triangles[i + 2] * 3 + 2]);
            Vec3f n = getNormal(p1, p2, p3);
            normals[triangles[i] * 3] = n.x;
            normals[triangles[i] * 3 + 1] = n.y;
            normals[triangles[i] * 3 + 2] = n.z;
            normals[triangles[i + 1] * 3] = n.x;
            normals[triangles[i + 1] * 3 + 1] = n.y;
            normals[triangles[i + 1] * 3 + 2] = n.z;
            normals[triangles[i + 2] * 3] = n.x;
            normals[triangles[i + 2] * 3 + 1] = n.y;
            normals[triangles[i + 2] * 3 + 2] = n.z;
        }
    }

    /**
     * Calculate and set the normals of the Mesh for a smooth shading appearance.
     * Hard edges are lost.
     */
    public void calculateSmoothShadingNormals() {
        normals = new float[vertexpos.length];
        for (int i = 0; i < triangles.length; i += 3) {
            Vec3f p1 = new Vec3f(vertexpos[triangles[i] * 3], vertexpos[triangles[i] * 3 + 1], vertexpos[triangles[i] * 3 + 2]);
            Vec3f p2 = new Vec3f(vertexpos[triangles[i + 1] * 3], vertexpos[triangles[i + 1] * 3 + 1], vertexpos[triangles[i + 1] * 3 + 2]);
            Vec3f p3 = new Vec3f(vertexpos[triangles[i + 2] * 3], vertexpos[triangles[i + 2] * 3 + 1], vertexpos[triangles[i + 2] * 3 + 2]);
            Vec3f v1 = new Vec3f();
            v1.setSub(p3, p1);
            Vec3f v2 = new Vec3f();
            v2.setSub(p3, p2);
            Vec3f n = new Vec3f();
            n.setCrossProduct(v1, v2);
            float a1 = calcAngle(p1, p2, p3);
            float a2 = calcAngle(p2, p3, p1);
            float a3 = calcAngle(p3, p1, p2);
            Vec3f n1 = n.scale(a1);
            Vec3f n2 = n.scale(a2);
            Vec3f n3 = n.scale(a3);
            normals[triangles[i] * 3] += n1.x;
            normals[triangles[i] * 3 + 1] += n1.y;
            normals[triangles[i] * 3 + 2] += n1.z;
            normals[triangles[i + 1] * 3] += n2.x;
            normals[triangles[i + 1] * 3 + 1] += n2.y;
            normals[triangles[i + 1] * 3 + 2] += n2.z;
            normals[triangles[i + 2] * 3] += n3.x;
            normals[triangles[i + 2] * 3 + 1] += n3.y;
            normals[triangles[i + 2] * 3 + 2] += n3.z;
        }
        for (int i = 0; i < normals.length; i += 3) {
            Vec3f n = new Vec3f(normals[i], normals[i + 1], normals[i + 2]);
            n.normalize();
            normals[i] = n.x;
            normals[i + 1] = n.y;
            normals[i + 2] = n.z;
        }
    }

    /**
     * Calculate and returns an angle between the vector p1p2 and p1p3.
     * @param p1 - the common point of the 2 vectors
     * @param p2 - extremity of the first vector
     * @param p3 - extremity of the second vector
     * @return an angle in radian.
     */
    private float calcAngle(Vec3f p1, Vec3f p2, Vec3f p3) {
        Vec3f v1 = new Vec3f();
        v1.setSub(p2, p1);
        Vec3f v2 = new Vec3f();
        v2.setSub(p3, p1);
        return (float) Math.acos(v1.dotProduct(v2) / (v1.length() * v2.length()));
    }

    /**
     * Calculate and returns the orthogonal vector between the p1p2 and p1p3 vectors.
     * @param p1 - the common point of the 2 vectors
     * @param p2 - extremity of the first vector
     * @param p3 - extremity of the second vector
     * @return the normal to a plan defined by the vector p1p2 and p1p3
     */
    Vec3f getNormal(Vec3f p1, Vec3f p2, Vec3f p3) {
        Vec3f v1 = new Vec3f();
        v1.setSub(p2, p1);
        Vec3f v2 = new Vec3f();
        v2.setSub(p3, p1);
        Vec3f n = new Vec3f();
        n.setCrossProduct(v1, v2);
        n.normalize();
        return n;
    }


    /**
     * Initialize the buffers on the GPU.
     */
    public void initGraphics() {
        //est nécessaire car les coordonnées de textures ne sont pas implantées pour tous les maillages utilisés
        if (texturesCoord == null)
            texturesCoord = new float[vertexpos.length / 3 * 2];
        /*
         * Buffer des sommets
         */
        ByteBuffer posbytebuf = ByteBuffer.allocateDirect(vertexpos.length * Float.BYTES);
        posbytebuf.order(ByteOrder.nativeOrder());
        FloatBuffer posbuffer = posbytebuf.asFloatBuffer();
        posbuffer.put(vertexpos);
        posbuffer.position(0);


        /*
         * Buffer des triangles
         */
        ByteBuffer trianglesbutebuf = ByteBuffer.allocateDirect(triangles.length * Integer.BYTES);
        trianglesbutebuf.order(ByteOrder.nativeOrder());
        IntBuffer trianglesbuf = trianglesbutebuf.asIntBuffer();
        trianglesbuf.put(triangles);
        trianglesbuf.position(0);

        /*
         * Buffer des normals
         */
        ByteBuffer normalbytebuf = ByteBuffer.allocateDirect(normals.length * Float.BYTES);
        normalbytebuf.order(ByteOrder.nativeOrder());
        FloatBuffer normalbuffer = normalbytebuf.asFloatBuffer();
        normalbuffer.put(normals);
        normalbuffer.position(0);

        /*
         * Buffer des textures
         */
        ByteBuffer texturebytebuf = ByteBuffer.allocateDirect(texturesCoord.length * Float.BYTES);
        texturebytebuf.order(ByteOrder.nativeOrder());
        FloatBuffer texturebuffer = texturebytebuf.asFloatBuffer();
        texturebuffer.put(texturesCoord);
        texturebuffer.position(0);


        int[] buffers = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        glposbuffer = buffers[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexpos.length * Float.BYTES, posbuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        int[] trianglesbuffers = new int[1];
        GLES20.glGenBuffers(1, trianglesbuffers, 0);

        gltrianglesbuffer = trianglesbuffers[0];

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gltrianglesbuffer);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, triangles.length * Integer.BYTES, trianglesbuf, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        int[] normalsbuffers = new int[1];
        GLES20.glGenBuffers(1, normalsbuffers, 0);

        glnormalbuffer = normalsbuffers[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glnormalbuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normals.length * Float.BYTES, normalbuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        int[] texturebuffers = new int[1];
        GLES20.glGenBuffers(1, texturebuffers, 0);

        gltexturebuffer = texturebuffers[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gltexturebuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texturesCoord.length * Float.BYTES, texturebuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Draw the mesh as triangles
     * @param shaders - shader which receive the buffers as attribute variables to draw the mesh according to its corresponding glsl.
     */
    public void draw(final MultipleLightingShaders shaders) {

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer);
        shaders.setPositionsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glnormalbuffer);
        shaders.setNormalsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gltexturebuffer);
        shaders.setTexturePointer(2, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gltrianglesbuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    /**
     * Draw the shadow of the mesh.
     * @param shaders - shader which receive the buffers as attribute variables to draw the mesh according to its corresponding glsl.
     */
    public void draw(final DepthShader shaders) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer);
        shaders.setPositionsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gltrianglesbuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Draw the mesh as triangles and wireframe.
     * @param shaders - shader which receive the buffers as attribute variables to draw the mesh according to its corresponding glsl.
     */
    public void drawWithLines(final MultipleLightingShaders shaders) {
        GLES20.glPolygonOffset(2.F, 4.F);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer);
        shaders.setPositionsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glnormalbuffer);
        shaders.setNormalsPointer(3, GLES20.GL_FLOAT);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gltrianglesbuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_INT, 0);

        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setMaterialColor(MyGLRenderer.black);

        for (int i = 0; i < triangles.length; i += 3)
            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_INT, i * Integer.BYTES);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


    }

    /**
     * Draw the wireframe of the mesh
     * @param shaders - shader which receive the buffers as attribute variables to draw the mesh according to its corresponding glsl.
     */
    public void drawLinesOnly(final MultipleLightingShaders shaders) {

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer);
        shaders.setPositionsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glnormalbuffer);
        shaders.setNormalsPointer(3, GLES20.GL_FLOAT);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gltrianglesbuffer);

        shaders.setMaterialColor(MyGLRenderer.black);

        for (int i = 0; i < triangles.length; i += 3)
            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_INT, i * Integer.BYTES);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }
}
