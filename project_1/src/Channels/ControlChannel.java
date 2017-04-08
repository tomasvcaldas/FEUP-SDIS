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
                    System.out.println("try....");
                    DatagramPacket receiveDatagram = getMulticastData();
                    System.out.println("try....2");
                    Message message = Message.getMessage(receiveDatagram);
                    System.out.println("try....3");

                    System.out.println("before get header on control");

                    Header headerArgs = message.getHeader();

                    String fileId = headerArgs.getFileId();
                    System.out.println("msg type: " + headerArgs.getType());

                    if(headerArgs.getType() == MessageType.DELETE){
                        System.out.println("Received DELETE ! ");
                        deleteChunks(fileId);
                    }
                    //multicastsocket.leaveGroup(address);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



}
