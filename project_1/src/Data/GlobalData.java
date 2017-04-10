package Data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by joao on 4/10/17.
 */
public class GlobalData {
    private ConcurrentHashMap<String,ConcurrentHashMap<ChunkInfo,Set>> receivedStores;

    public GlobalData(){
        receivedStores = new ConcurrentHashMap<>();
    }

    public void addFile(String fileID){
        receivedStores.put(fileID, new ConcurrentHashMap<>());
    }

    public void addChunk(String fileID, String chunkNo, String peer_id, String rep_deg){
        ChunkInfo c = new ChunkInfo(Integer.parseInt(chunkNo), Integer.parseInt(rep_deg));
        receivedStores.get(fileID).put(c, new HashSet());
        receivedStores.get(fileID).get(c).add(Integer.parseInt(peer_id));
    }

    public void removeChunk(String fileID, String chunkNo, String peer_id){
        if(receivedStores.get(fileID).get(Integer.parseInt(chunkNo)).contains(Integer.parseInt(peer_id)))
            receivedStores.get(fileID).get(Integer.parseInt(chunkNo)).remove(Integer.parseInt(peer_id));
    }

    public void removeFile(String fileID){
        receivedStores.remove(fileID);
    }

}
