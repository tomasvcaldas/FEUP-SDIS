package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joao on 4/8/17.
 */
public class FileData implements Serializable{
    private HashMap<String, ArrayList> files;

    public FileData(){ this.files = new HashMap<>(); }

    public void addChunk(String hash, int chunkNo){
        if(!files.containsKey(hash)){
            files.put(hash, new ArrayList());
        }

        files.get(hash).add(chunkNo);
    }

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

    public static FileData load(String path){
        FileData ret = null;

        try{
            FileInputStream fi = new FileInputStream(new File(path));
            ObjectInputStream oi = new ObjectInputStream(fi);

            ret = (FileData) oi.readObject();

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

    public HashMap<String, ArrayList> getFiles() {
        return files;
    }
}
