package fr.univ_poitiers.dptinfo.algo3d.objimporter;

/**
 * Class to contain the data of a face of an .obj file.
 */
class OBJFace {

    /**
     * First vertex index
     */
    private OBJVertex v1;
    /**
     * Second vertex index
     */
    private OBJVertex v2;
    /**
     * Third vertex index
     */
    private OBJVertex v3;

    /**
     * Constructor.
     * @param data - an array containing the 3 indices
     */
    public OBJFace(String[] data) {
        this.v1 = new OBJVertex(data[1]);
        this.v2 = new OBJVertex(data[2]);
        this.v3 = new OBJVertex(data[3]);
    }

    /**
     * Returns the first vertex index.
     * @return the first vertex index
     */
    public OBJVertex getV1() {
        return v1;
    }

    /**
     * Returns the second vertex index.
     * @return the second vertex index
     */
    public OBJVertex getV2() {
        return v2;
    }

    /**
     * Returns the third vertex index.
     * @return the third vertex index
     */
    public OBJVertex getV3() {
        return v3;
    }
}
