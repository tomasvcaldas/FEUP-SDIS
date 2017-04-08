package peer;

import Channels.BackupChannel;
import Channels.ControlChannel;
import Data.FileData;
import Data.FileInfo;
import Data.Metadata;
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
    //private RestoreChannel mdr;

    private DatagramSocket socket;

    private Thread dataThread;

    private static Peer peer;

    public Peer(String[] args) throws IOException{
        this.serverID = args[0];
        new File("Peer_" + this.serverID).mkdir();

        loadData();

        this.mdb = new BackupChannel(args[3], args[4], this);
        this.mc = new ControlChannel(args[1],args[2],this);
        //creating directory with Peer id

        peer = this;
        this.mdb.startThread();
        this.mc.startThread();
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


            System.out.println("started");
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
        System.out.println("inside processInfo");
        System.out.println(type);
        switch(type){
            case "BACKUP":
                backup(TestAppArgs);
                System.out.println("Backup required");
                break;
            case "RESTORE":
                //restore():
                System.out.println("Restore required");
                break;
            case "DELETE":
                delete(TestAppArgs);
                System.out.println("Delete required");
                break;
        }
    }

    public void restore(){
        //TODO restore function
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
        System.out.println(temp);
        return temp.contains(chunkNo);
    }

    public  BackupChannel getMdb() {
        return mdb;
    }

    public ControlChannel getMc() {
        return mc;
    }

    public FileData getFileData(){
        return this.filedata;
    }
}
