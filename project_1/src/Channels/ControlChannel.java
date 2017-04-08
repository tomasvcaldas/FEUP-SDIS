package Channels;


import Utilities.Header;
import Utilities.Message;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import fileManage.MessageType;
import peer.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

import static Protocols.Delete.deleteChunks;

public class ControlChannel extends Channel {

    private Peer peer;

    public ControlChannel(String controlAddress, String controlPort, Peer peer) throws IOException{
        super(controlAddress,controlPort);
        this.thread = new ControlThread();
        this.peer = peer;
    }

    public class ControlThread extends Thread{
        public void run(){
            System.out.println("inside control channel ...");
            while(true){

                try{

                    DatagramPacket receiveDatagram = getMulticastData();

                    Message message = Message.getMessage(receiveDatagram);

                    Header headerArgs = message.getHeader();


                    if(headerArgs.getType() == MessageType.DELETE){
                        System.out.println("DELETE received, starting the handle...");
                        deleteChunks(headerArgs.getFileId());
                    }

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



}
