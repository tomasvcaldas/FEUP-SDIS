package Data;

import java.io.Serializable;

/**
 * Created by joao on 4/8/17.
 */
public class ChunkInfo implements Serializable{
    private int ID;
    private int rep_deg;

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
}
