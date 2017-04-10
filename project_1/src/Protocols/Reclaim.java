package Protocols;

import Utilities.Message;
import peer.Peer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by joao on 4/10/17.
 */
public class Reclaim {

    private Peer peer;
    private int storage;

    /*public Reclaim(int storage, Peer peer) throws IOException{
        this.peer = peer;
        this.peer.setMaxStrorage(storage);
        this.storage = storage;

        deleteFromFiledata();
    }

    public void deleteFromFiledata() throws IOException{
        while(storage < this.peer.getFileData().getStoredSize()){

            System.out.println("Storage capacity= " + storage);
            System.out.println("Stored chunks size= " + this.peer.getFileData().getStoredSize());

            String[] removed = this.peer.getFileData().removeChunk();

            String header = Message.createRemovedHeader(this.peer.serverID, removed[0], removed[1]);
            sendRemovedMessage(header);

            String path = "Peer_" + this.peer.serverID + "/" + removed[0] + "/" + removed[1];

            try{
                File file = new File(path);
                if(file.delete()){
                    System.out.println(file.getName() + " is deleted!");
                }else{
                    System.out.println("Delete operation is failed.");
                }
            }catch(Exception e){

                e.printStackTrace();
            }
        }
    }

    public void sendRemovedMessage(String header) throws IOException{
        byte[] message = header.getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length, this.peer.getMc().getAdress(), this.peer.getMc().getPort());
        this.peer.getMc().getSocket().send(packet);
    }*/
}
