package Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by joao on 4/8/17.
 */
public class FileInfo implements Serializable{

    private String fileName;
    private String fileID;
    private ArrayList<ChunkInfo> chunks;
    private int rep_deg;
    private int fileSize;

    public FileInfo(String fileName, String fileID, int rep_deg, long size){
        this.fileName = fileName;
        this.fileID = fileID;
        this.chunks = new ArrayList<>();
        this.fileSize = (int) size;
        this.rep_deg = rep_deg;
    }

    public String getFileID() {
        return fileID;
    }

    public int getRep_deg() { return rep_deg; }

    public ArrayList<ChunkInfo> getChunks() {
        return chunks;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileName(){
        return fileName;
    }
}
