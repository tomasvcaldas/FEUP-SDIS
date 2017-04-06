package peer;

import Channels.BackupChannel;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static fileManage.SplitFile.splitFile;


public class Peer implements PeerInterface{

    public static String serverID;

    private static BackupChannel mdb;
    //private ControlChannel mc;
    //private RestoreChannel mdr;

    private DatagramSocket socket;

    private Thread dataThread;

    private static Peer peer;

    public Peer(String[] args) throws IOException{
        this.serverID = args[0];

        this.mdb = new BackupChannel(args[1], args[2]);

        //creating directory with Peer id
        new File("Peer_" + this.serverID).mkdir();

        peer = this;
        this.mdb.startThread();
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
            e.printStackTrace();
        }
    }

    public void processInfo(String type, String[] TestAppArgs) throws IOException{
        System.out.println(type);
        switch(type){
            case "backup":
                backup(TestAppArgs);
                System.out.println("Backup required");
                break;
            case "restore":
                //restore();
                System.out.println("Restore required");
                break;
            case "delete":
                //delete();
                System.out.println("Delete required");
                break;
        }
    }

    public void restore(){
        //TODO restore function
    }

    public void backup(String[] args) throws IOException{

      String fileName = args[4];
      int repDeg = Integer.parseInt(args[5]);
      splitFile(fileName,repDeg,Peer.serverID,this.mdb);

    }

    public void delete(){
        //TODO restore function
    }
}
