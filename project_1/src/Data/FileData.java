package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static Utilities.Hash.sha256;

/**
 * Created by joao on 4/8/17.
 */
public class FileData implements Serializable{
    private ConcurrentHashMap <String, ArrayList<ChunkInfo>> files;

    public FileData(){ this.files = new ConcurrentHashMap<>(); }

    /**
     * Adds chunk to the respective key
     * @param hash HashMap key
     * @param chunkNo chunk number
     * @param size body length
     * @param rep_deg replication degree
     */
    public void addChunk(String hash, int chunkNo, int size, int rep_deg){
        if(!files.containsKey(hash)){
            files.put(hash, new ArrayList());
        }
        ChunkInfo chunk = new ChunkInfo(chunkNo, rep_deg, size);
        files.get(hash).add(chunk);
    }

    /**
     * Saves the information of the stored chunk in the FILEDATA file.
     * @param f File data
     * @param peer Respective peer
     */
    public static void save(FileData f, String peer){

        try{
            new File("Peer_" + peer + "/data").mkdir();
            FileOutputStream f1 = new FileOutputStream(new File("Peer_" + peer + "/data/filedata.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f1);

            o.writeObject(f);
            o.close();
            f1.close();
        } catch (FileNotFoundException e) {
            System.out.println("Filedata not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream on save");
        }
    }

    /**
     * Loads the information from the filedata
     * @param path File path
     * @return filedata loaded
     */
    public static FileData load(String path){
        FileData ret = null;

        try{
            FileInputStream fi = new FileInputStream(new File(path));
            ObjectInputStream oi = new ObjectInputStream(fi);

            ret = (FileData) oi.readObject();

            //System.out.println(ret.files.get(sha256("photo.jpg")));

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("Filedata file not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream on load");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public ConcurrentHashMap<String, ArrayList<ChunkInfo>> getFiles() {
        return files;
    }

    /**
     * Verifies if the chunk with that chunk number already exists
     * @param fileID File Name
     * @param chunkNo chunk number
     * @return boolean true/false
     */
    public boolean hasChunk(String fileID, String chunkNo){
        boolean found = false;
        if(files.containsKey(fileID)){
            ArrayList<ChunkInfo> temp = files.get(fileID);
            System.out.println(temp);
            for(int i = 0; i < temp.size(); i++){
                if(temp.get(i).getID() == Integer.parseInt(chunkNo))
                    found = true;
            }
        }
        return found;
    }

    public void removeFile(String fileID){
        this.files.remove(sha256(fileID));
    }
}
