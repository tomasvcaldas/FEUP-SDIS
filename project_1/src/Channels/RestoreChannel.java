package Channels;


import Protocols.Restore;
import Utilities.Header;
import Utilities.Message;
import fileManage.MessageType;
import peer.Peer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;

import static fileManage.mergeFile.mergeFiles;

public class RestoreChannel extends Channel {
    private Peer peer;

    public RestoreChannel(String restoreAddress, String restorePort,Peer peer) throws IOException{
        super(restoreAddress,restorePort);
        setThread(new RestoreThread());
        this.peer = peer;
    }

    public class RestoreThread extends Thread{
        public void run(){
            System.out.println("RESTORE: reading...");
            while(true){
                try{
                    DatagramPacket newPacket = getMulticastData();

                    Message message = Message.getMessage(newPacket);

                    Header headerArgs = message.getHeader();

                    byte[] body = message.getBody();

                    if(headerArgs.getType() == MessageType.CHUNK){
                        System.out.println("CHUNK received, starting the handle..." + headerArgs.getSenderId());
                        peer.waitingForChunk = false;
                        processMessage(headerArgs.getChunkNumber(), body);
                    }
                } catch(Exception e){
                e.printStackTrace();
            }
            }
        }

    }

    public void processMessage(String chunk, byte[] body) throws IOException{
        System.out.println();
        System.out.println("Received CHUNK" + chunk);
        System.out.println();
        peer.getRestored().putByte(chunk, body);
        if(peer.getRestored() != null) {
            if (body.length >= 64000) {
                peer.getRestored().sendGetChunkMessage();
            } else {
                new File("data/restores").mkdir();
                File f = new File("data/restores/" + peer.getRestored().getFileName());
                mergeFiles(peer.getRestored().getBytes(), f);
            }
        }
    }
}
