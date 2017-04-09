package Protocols;

import Utilities.Message;
import peer.Peer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;

import static Utilities.Hash.sha256;
import static Utilities.Message.createDeleteHeader;

/**
 * Created by joao on 4/9/17.
 */
public class Restore {

    private Peer peer;
    private String fileName;
    private String hashed;
    private int nextChunk;

    public static HashMap<String, ArrayList> restores;

    public Restore(String fileName, Peer peer)throws IOException{
        this.peer = peer;
        this.fileName = fileName;
        this.nextChunk = 1;
        this.hashed = sha256(fileName);
        sendGetChunkMessage();
        restores = new HashMap<>();
        restores.put(this.hashed, new ArrayList());
    }

    public void sendGetChunkMessage() throws IOException{
        String message = Message.createGetChunkHeader(this.hashed, this.peer.serverID, this.nextChunk);

        byte file[] = message.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(file);
        byte c[] = outputStream.toByteArray();

        DatagramPacket packet = new DatagramPacket(c, c.length, this.peer.getMc().getAdress(), this.peer.getMc().getPort());

        this.peer.getMc().getSocket().send(packet);
    }

    public static HashMap<String, ArrayList> getRestores() {
        return restores;
    }

    public static boolean chunkExists(String fileID, String chunkNo){
        int chunk  = Integer.parseInt(chunkNo);
        ArrayList temp = restores.get(fileID);
        if (temp.contains(chunk)){
            System.out.println("This chunk already exists... Aborting");
            return true;
        }
        return false;
    }
}
