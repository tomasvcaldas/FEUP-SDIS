package peer;

import Channels.BackupChannel;

import java.io.IOException;
import java.net.DatagramSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Peer implements PeerInterface{

    private static String serverID;

    private BackupChannel mdb;

    private DatagramSocket socket;

    private Thread dataThread;

    private static Peer peer;

    public Peer(String[] args) throws IOException{
        this.serverID = args[0];
        this.mdb = new BackupChannel(args[1], args[2]);
    }

    public static void main(String[] args){
        try {
            Peer peer = new Peer(args);
            PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(peer, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            //registry.bind("Hello", stub);

            //dataThread = new Thread(mdb);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restore(){
        //TODO restore function
    }

    public void backup(){
        //TODO backup function
    }

    public void delete(){
        //TODO restore function
    }
}
