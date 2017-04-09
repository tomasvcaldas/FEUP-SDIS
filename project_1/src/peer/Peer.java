package peer;

import Channels.BackupChannel;
import Channels.ControlChannel;
import Channels.RestoreChannel;
import Data.FileData;
import Data.FileInfo;
import Data.Metadata;
import Protocols.Delete;
import Protocols.Restore;
import com.sun.xml.internal.fastinfoset.sax.SystemIdResolver;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static Protocols.Delete.Delete;
import static Utilities.Hash.sha256;
import static fileManage.SplitFile.splitFile;




public class Peer implements PeerInterface{

    public static String serverID;
    private Metadata metadata;
    private FileData filedata;

    private static BackupChannel mdb;
    private static ControlChannel mc;
    private static RestoreChannel mdr;

    //private DatagramSocket socket;

    //private Thread dataThread;

    private Peer peer;

    public Peer(String[] args) throws IOException{
        this.serverID = args[0];
        new File("Peer_" + this.serverID).mkdir();

        loadData();

        this.mdb = new BackupChannel(args[3], args[4], this);
        this.mc = new ControlChannel(args[1],args[2],this);
        this.mdr = new RestoreChannel(args[5], args[6], this);
        //creating directory with Peer id

        this.mdb.startThread();
        this.mc.startThread();
        this.mdr.startThread();

        peer = this;
    }

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

            //Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("processInfo", stub);

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void processInfo(String type, String[] TestAppArgs) throws IOException{
        switch(type){
            case "BACKUP":
                System.out.println("Backup required");
                backup(TestAppArgs);
                break;
            case "RESTORE":
                restore(TestAppArgs);
                System.out.println("Restore required");
                //restore():
                break;
            case "DELETE":
                System.out.println("Delete required");
                delete(TestAppArgs);
                break;
        }
    }

    public void restore(String[] args) throws IOException{
        String fileName = args[2];
        Restore rest = new Restore(fileName, this.peer);
    }

    public void backup(String[] args) throws IOException{
      String fileName = args[2];
      File file1 = new File(fileName);
      int repDeg = Integer.parseInt(args[3]);
      System.out.println("Splitting file");
      splitFile(fileName,repDeg,Peer.serverID,this.mdb, this.serverID);
      System.out.println("File splitted");
      FileInfo f = new FileInfo(fileName, sha256(fileName), repDeg, file1.length());
      this.metadata.addFile(f);
      //FileData.save(this.filedata,this.serverID);
      Metadata.save(this.metadata, this.serverID);
    }

    public void delete(String[] args) throws IOException{
        String fileName = args[2];
        Delete(fileName, Peer.serverID, this.mc);
        System.out.println("final delete function");
    }

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
    }

    public static RestoreChannel getMdr() { return mdr; }

    public FileData getFileData(){
        return this.filedata;
    }
}
