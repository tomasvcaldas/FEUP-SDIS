package Channels;


import Utilities.Header;
import Utilities.Message;
import fileManage.MessageType;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

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

                    if(headerArgs.getType() == MessageType.GETCHUNK){
                        System.out.println("GETCHUNK received, starting the handle...");
                    }
                } catch(Exception e){
                e.printStackTrace();
            }
            }
        }

    }
}
