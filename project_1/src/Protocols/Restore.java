package Protocols;

import Utilities.Message;
import peer.Peer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
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

    private HashMap<Integer, byte[]> restores;

    public Restore(String fileName, Peer peer)throws IOException{
        this.peer = peer;
        this.fileName = fileName;
        this.hashed = sha256(fileName);
        this.nextChunk = 1;
        this.restores = new HashMap<>();
    }

    /**
     * Creates the GETCHUNK message and sends it
     * @throws IOException
     */
    public void sendGetChunkMessage() throws IOException{
        String message = Message.createGetChunkHeader(this.hashed, this.peer.serverID, this.nextChunk);

        byte file[] = message.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(file);
        byte c[] = outputStream.toByteArray();

        System.out.println("chunk nº " + this.nextChunk + " c length " + c.length);
        System.out.println(peer);

        DatagramPacket packet = new DatagramPacket(c, c.length, this.peer.getMc().getAdress(), this.peer.getMc().getPort());

        this.peer.getMc().getSocket().send(packet);
        this.nextChunk++;
    }

    public String getFileName() {
        return fileName;
    }

    public void putByte(String chunk, byte[] body){
        this.restores.put(Integer.parseInt(chunk), body);
    }

    /*public boolean chunkExists(String chunkNo){
        /*int chunk  = Integer.parseInt(chunkNo);
        if (chunksArray.contains(chunk)){
            System.out.println("This chunk already exists... Aborting");
            return true;
        }
        return false;
    }*/

    public ArrayList<byte[]> getBytes(){
        ArrayList<byte[]> ret = new ArrayList<>();
        for(byte[] b: this.restores.values()){
            ret.add(b);
        }

        return ret;
    }
}
