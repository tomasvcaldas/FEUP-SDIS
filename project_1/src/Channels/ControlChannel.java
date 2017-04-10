package Channels;


import Protocols.Restore;
import Utilities.Header;
import Utilities.Message;
import fileManage.MessageType;
import peer.Peer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ControlChannel extends Channel {
    public ConcurrentHashMap<String,ConcurrentHashMap<String,Set<String>>> receivedStores;

    private Peer peer;

    public ControlChannel(String controlAddress, String controlPort, Peer peer) throws IOException{
        super(controlAddress,controlPort);
        setThread(new ControlThread());
        this.peer = peer;
        this.receivedStores = new ConcurrentHashMap<>();
    }

    public class ControlThread extends Thread{
        public void run(){
            System.out.println("CONTROL: reading...");
            while(true){

                try{

                    DatagramPacket receiveDatagram = getMulticastData();

                    Message message = Message.getMessage(receiveDatagram);

                    Header headerArgs = message.getHeader();

                    if(headerArgs.getType() == MessageType.DELETE){
                        System.out.println("RECEIVED DELETE with NUMBER: " + headerArgs.getChunkNumber());
                        peer.deleteChunks(headerArgs.getFileId());

                    }
                    else if(headerArgs.getType() == MessageType.STORED){
                        System.out.println("RECEIVED STORE with NUMBER: " + headerArgs.getChunkNumber());
                        if(!receivedStores.containsKey(headerArgs.getFileId()))
                            receivedStores.put(headerArgs.getFileId(), new ConcurrentHashMap<>());
                        if(!receivedStores.get(headerArgs.getFileId()).containsKey(headerArgs.getChunkNumber()))
                            receivedStores.get(headerArgs.getFileId()).put(headerArgs.getChunkNumber(), new HashSet<>());
                        receivedStores.get(headerArgs.getFileId()).get(headerArgs.getChunkNumber()).add(headerArgs.getSenderId());
                    }
                    else if(headerArgs.getType() == MessageType.GETCHUNK){
                        if(!headerArgs.getSenderId().equals(peer.serverID)){
                            if(peer.getFileData().hasChunk(headerArgs.getFileId(), headerArgs.getChunkNumber())) {
                                peer.waitingForChunk = true;
                                tryToSendChunk(headerArgs.getFileId(), headerArgs.getChunkNumber());
                            } else
                                System.out.println("This peer doesn't have the chunk...");
                        }
                    }

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * Creates and sends the message with the GETCHUNK for the RESTORE protocol.
     * @param fileID fileId of the chunk
     * @param chunkNo chunk Numer of the chunk
     * @throws IOException
     * @throws InterruptedException
     */
    public void tryToSendChunk(String fileID, String chunkNo) throws IOException, InterruptedException {
        File f = new File("Peer_" + peer.serverID + "/" + fileID + "/" + chunkNo);
        byte[] body = Files.readAllBytes(f.toPath());
        String header = Message.createChunkHeader(peer.serverID, fileID, chunkNo);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write( header.getBytes() );
        outputStream.write( body );

        byte c[] = outputStream.toByteArray( );

        getThread().sleep(new Random().nextInt(400));

        if(peer.waitingForChunk){
            DatagramPacket packet = new DatagramPacket(c, c.length,peer.getMdr().getAdress(),peer.getMdr().getPort());
            peer.getMdr().getSocket().send(packet);
        }
    }

    public ConcurrentHashMap<String,ConcurrentHashMap<String,Set<String>>> getReceivedStores(){
        return receivedStores;
    }


}
