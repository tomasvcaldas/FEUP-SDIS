package peer;

import Channels.BackupChannel;
import Channels.ControlChannel;
import Channels.RestoreChannel;
import Data.FileData;
import Data.FileInfo;
import Data.Metadata;
import Protocols.Delete;
import Protocols.Restore;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static Protocols.Delete.Delete;
import static Utilities.Hash.sha256;
import static Protocols.Backup.splitFile;




public class Peer implements PeerInterface{

    public static String serverID;
    private Metadata metadata;
    private FileData filedata;
    private Restore restored;
    private static BackupChannel mdb;
    private static ControlChannel mc;
    private static RestoreChannel mdr;
    public boolean waitingForChunk = true;


    private Peer peer;

    public Peer(String[] args) throws IOException{
        this.serverID = args[0];
        new File("Peer_" + this.serverID).mkdir();
        System.out.println("Peer: " + this.serverID);
        loadData();

        this.mdb = new BackupChannel(args[3], args[4], this);
        this.mc = new ControlChannel(args[1],args[2],this);
        this.mdr = new RestoreChannel(args[5], args[6], this);

        this.mdb.startThread();
        this.mc.startThread();
        this.mdr.startThread();

        peer = this;
        this.restored = null;
    }

    /**
     * loads filedata and metadata at the start
     */
    public void loadData(){
        this.metadata = Metadata.load("Peer_" +  this.serverID + "/data/metadata.txt");

        if(this.metadata == null)
            this.metadata = new Metadata();

        this.filedata = FileData.load("Peer_" +  this.serverID + "/data/filedata.txt");

        if(this.filedata == null)
            this.filedata = new FileData();
    }

    public static void main(String[] args){
        try {
            Peer peer = new Peer(args);
            PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(peer, 1099);
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("processInfo", stub);

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Verifies the protocol requested in the message received
     * @param type type of protocol
     * @param TestAppArgs Arguments passed in  testApp
     * @throws IOException
     */
    public void processInfo(String type, String[] TestAppArgs) throws IOException{
        switch(type){
            case "BACKUP":
                System.out.println("Backup required");
                backup(TestAppArgs);
                break;
            case "RESTORE":
                System.out.println("Restore required");
                restore(TestAppArgs);
                break;
            case "DELETE":
                System.out.println("Delete required");
                delete(TestAppArgs);
                break;
        }
    }

    /**
     * main restore function
     * @param args test app args
     * @throws IOException
     */
    public void restore(String[] args) throws IOException{
        String fileName = args[2];
        this.restored = new Restore(fileName, this);
        this.restored.sendGetChunkMessage();
        System.out.println("Final RESTORE function.");
    }

    /**
     * main backup function
     * @param args test app args
     * @throws IOException
     */
    public void backup(String[] args) throws IOException{
      String fileName = args[2];
      File file1 = new File(fileName);
      int repDeg = Integer.parseInt(args[3]);

      splitFile(fileName,repDeg,Peer.serverID,this.mdb,this);
      System.out.println("All CHUNKS were sent SUCCESSFULLY!");

      FileInfo f = new FileInfo(fileName, sha256(fileName), repDeg, file1.length());
      Metadata.save(this.metadata, this.serverID);
      System.out.println("Final BACKUP function.");
    }

    /**
     * main delete function
     * @param args test app args
     * @throws IOException
     */
    public void delete(String[] args) throws IOException{
        String fileName = args[2];
        Delete(fileName, Peer.serverID, this.mc);
        System.out.println("Final DELETE function.");
    }

    /**
     * verifies if the chunk with that number already exists in the filedata
     * @param file
     * @param chunkNo
     * @return
     */
    public boolean chunkExists(String file, int chunkNo){
        ArrayList temp = this.filedata.getFiles().get(file);
        if(temp == null)
            return false;

        return temp.contains(chunkNo);
    }

    public  BackupChannel getMdb() {
        return mdb;
    }

    public ControlChannel getMc() {
        return mc;
    }

    public void deleteChunks(String fileID){
        Delete.deleteChunks(fileID, this.serverID);
        this.filedata.removeFile(fileID);
        this.filedata.save(this.filedata, this.serverID);
    }

    public static RestoreChannel getMdr() { return mdr; }

    public FileData getFileData(){
        return this.filedata;
    }

    public Restore getRestored() {
        return restored;
    }
}
