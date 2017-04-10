package Data;

import java.io.Serializable;

public class ChunkInfo implements Serializable{
    private int ID;
    private int rep_deg;
    private int size;

    public int getID() {
        return ID;
    }

    public int getRep_deg() {
        return rep_deg;
    }

    public ChunkInfo(int id, int rep_deg){
        this.ID = id;

        this.rep_deg = rep_deg;
    }

    public ChunkInfo(int id, int rep_deg, int size){
        this.ID = id;

        this.rep_deg = rep_deg;

        this.size = size;
    }
}
