package Data;

import java.io.*;
import java.util.ArrayList;
/**
 * Created by joao on 4/8/17.
 */
public class Metadata implements Serializable{
    private ArrayList<FileInfo> backupArray;

    public Metadata(){ backupArray = new ArrayList<>();}

    public void addFile(FileInfo f){
        backupArray.add(f);
    }

    public ArrayList<FileInfo> getBackupArray() {
        return backupArray;
    }

    /**
     * Saves the new information in the METADATA file
     * @param m respective metadata
     * @param peer respective peer
     */
    public static void save(Metadata m, String peer){
        try{
            new File("Peer_" + peer + "/data").mkdir();
            FileOutputStream f = new FileOutputStream(new File("Peer_" + peer + "/data/metadata.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(m);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("metadata file not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream on save");
        }

    }

    /**
     * Loads the metadata file
     * @param path path of the metadata file
     * @return Metadata file required
     */
    public static Metadata load(String path){
        Metadata ret = null;

        try{
            FileInputStream fi = new FileInputStream(new File(path));
            ObjectInputStream oi = new ObjectInputStream(fi);

            ret = (Metadata) oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("metadata file not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream on load");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
