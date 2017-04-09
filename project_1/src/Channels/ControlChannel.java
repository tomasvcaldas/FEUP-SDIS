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
import java.util.ArrayList;

public class ControlChannel extends Channel {
    ArrayList<Message> replies;
    private Peer peer;

    public ControlChannel(String controlAddress, String controlPort, Peer peer) throws IOException{
        super(controlAddress,controlPort);
        setThread(new ControlThread());
        this.peer = peer;
        replies = new ArrayList<>();
    }

    public class ControlThread extends Thread{
        public void run(){
            System.out.println("CONTROL: reading...");
            while(true){

                try{

                    DatagramPacket receiveDatagram = getMulticastData();

                    Message message = Message.getMessage(receiveDatagram);

                    Header headerArgs = message.getHeader();

                    System.out.println("MSG TYPE RECEIVED IN CNTRL CHANNEL: " + headerArgs.getType());
                    if(headerArgs.getType() == MessageType.DELETE){
                        System.out.println("DELETE received, starting the handle...");
                        peer.deleteChunks(headerArgs.getFileId());
                    }
                    else if(headerArgs.getType() == MessageType.STORED){
                        System.out.println("STORED received, starting the handle...");
                        replies.add(message);
                        //TODO codigo do stored, ver a contagem dos chunks que ja recebeu
                        //TODO por causa do replication degree e esperar o tempo necessario
                        //TODO para mandar outra vez
                    }
                    else if(headerArgs.getType() == MessageType.GETCHUNK){
                        if(!headerArgs.getSenderId().equals(peer.serverID)){
                            if(peer.getFileData().hasChunk(headerArgs.getFileId(), headerArgs.getChunkNumber()))
                                tryToSendChunk(headerArgs.getFileId(), headerArgs.getChunkNumber());
                            else
                                System.out.println("This peer doesn't have the chunk...");
                        }
                    }

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void tryToSendChunk(String fileID, String chunkNo) throws IOException{
        File f = new File("Peer_" + peer.serverID + "/" + fileID + "/" + chunkNo);
        byte[] body = Files.readAllBytes(f.toPath());
        String header = Message.createChunkHeader(peer.serverID, fileID, chunkNo);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write( header.getBytes() );
        outputStream.write( body );

        byte c[] = outputStream.toByteArray( );

        //temporario
        DatagramPacket packet = new DatagramPacket(c, c.length,peer.getMdr().getAdress(),peer.getMdr().getPort());
        peer.getMdr().getSocket().send(packet);

        //TODO ver se o chunk ja foi enviado

        /*if(!Restore.chunkExists(chunkNo)){
            DatagramPacket packet = new DatagramPacket(c, c.length,peer.getMdr().getAdress(),peer.getMdr().getPort());
            peer.getMdr().getSocket().send(packet);
        }*/
    }

}
