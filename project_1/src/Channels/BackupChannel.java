package Channels;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;

import Utilities.Header;
import Utilities.Message;

import fileManage.MessageType;
import peer.Peer;

import javax.xml.crypto.Data;

public class BackupChannel extends Channel {

	private Peer peer;
	private boolean end = false;

	public BackupChannel(String backupAddress, String backupPort, Peer peer ) throws IOException{
		super(backupAddress,backupPort);
		setThread(new BackupThread());
		this.peer = peer;
	}

	public class BackupThread extends Thread {

	    Thread handler=null;

		public void run(){
			System.out.println("BACKUP: reading...");

                try {
                    while(true) {
                        DatagramPacket newPacket = getMulticastData();
                        this.handler = new messageHandler(newPacket);
                        handler.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
		
	}

	public class messageHandler extends Thread{

	    DatagramPacket request=null;

	    public messageHandler(DatagramPacket request){
	        this.request=request;
        }

        public void run(){
            try{
                Message message = Message.getMessage(request);

                Header headerArgs = message.getHeader();
                byte[] body = message.getBody();


                if(!headerArgs.getSenderId().equals(peer.serverID)){
                    if(headerArgs.getType() == MessageType.PUTCHUNK){
                        System.out.println("PUTCHUNK received, starting the handle...");
                        PutchunkReceived(headerArgs,body);
                        DatagramPacket storePacket = createStored(headerArgs,body);
                        int delay = ThreadLocalRandom.current().nextInt(0,400);
                        this.sleep(delay);
                        sendStored(storePacket);

                    }
                }
                else{
                    System.out.println("The same peer that sent the chunk is receiving it ...");
                    return;
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

	private void PutchunkReceived(Header header, byte[] body) throws IOException{
		String fileID = header.getFileId();
		int chunkNo = Integer.parseInt(header.getChunkNumber());
		int repDegree = Integer.parseInt(header.getChunkNumber());


		if(peer.chunkExists(fileID, chunkNo)){
			System.out.println("This chunk already exists on the system. Rejecting...");
			return;
		}

		this.peer.getFileData().addChunk(fileID, chunkNo);
        this.peer.getFileData().save(this.peer.getFileData(), this.peer.serverID);


        new File("Peer_" + this.peer.serverID + "/" + fileID).mkdir();

        FileOutputStream newFile = new FileOutputStream("Peer_" + this.peer.serverID + "/" + fileID + "/" + chunkNo);
        newFile.write(body);
        newFile.close();


	}

	public DatagramPacket createStored(Header header, byte[] body) throws IOException {
        String fileID = header.getFileId();
        int chunkNo = Integer.parseInt(header.getChunkNumber());
        String stored = Message.createStoredHeader(this.peer.serverID, fileID, chunkNo);

        byte[] bStored = stored.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bStored);
        byte c[] = outputStream.toByteArray();

        DatagramPacket packet = new DatagramPacket(c, c.length, this.peer.getMc().getAdress(),this.peer.getMc().getPort());
        return packet;
    }

    public void sendStored(DatagramPacket packet) throws IOException {
        this.peer.getMc().getSocket().send(packet);
    }

}