package fr.univ_poitiers.dptinfo.algo3d.objimporter;

/**
 * Class to parse and contain the data of a vertex of an .obj file.
 * Contains v, vt and vn.
 */
class OBJVertex {
    /**
     * Position index
     */
    private int v;
    /**
     * Texture coordinate index
     */
    private int vt;
    /**
     * Normal index
     */
    private int vn;

    /**
     * Constructor.
     * Parse the v/vt/vn.
     * @param data string in the v/vt/vn format
     */
    public OBJVertex(String data) {
        String[] index = data.split("/");
        this.v = Integer.parseInt(index[0]);
        if (index.length > 1) {
            if (!index[1].isEmpty())
                this.vt = Integer.parseInt(index[1]);
            this.vn = Integer.parseInt(index[2]);
        }
    }

    /**
     * Returns the position index
     * @return the position index
     */
    public int getV() {
        return v;
    }

    /**
     * Returns the texture coordinate index
     * @return the texture coordinate index
     */
    public int getVt() {
        return vt;
    }

    /**
     * Returns the normal index
     * @return the normal index
     */
    public int getVn() {
        return vn;
    }
}
