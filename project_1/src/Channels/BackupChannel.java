package Channels;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import Utilities.Header;
import Utilities.Message;


import fileManage.MessageType;
import peer.Peer;

import javax.xml.crypto.Data;

public class BackupChannel extends Channel {

	private Peer peer;


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
                        System.out.println("RECEIVED PUTCHUNK with NUMBER: " + headerArgs.getChunkNumber());
                        PutchunkReceived(headerArgs,body);
                        //REPLY
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

    /**
     * Handles the message of the putchunk recieved and stores the chunk in the peer..
     * @param header Header of the message
     * @param body Body with the chunk content
     * @throws IOException
     */
	private void PutchunkReceived(Header header, byte[] body) throws IOException{
		String fileID = header.getFileId();
		int chunkNo = Integer.parseInt(header.getChunkNumber());
		int repDegree = Integer.parseInt(header.getChunkNumber());


		if(peer.chunkExists(fileID, chunkNo)){
			System.out.println("This chunk already exists on the system. Rejecting...");
			return;
		}

		this.peer.getFileData().addChunk(fileID, chunkNo, body.length/1000, Integer.parseInt(header.getReplicationDegree()));
        this.peer.getFileData().save(this.peer.getFileData(), this.peer.serverID);


        new File("Peer_" + this.peer.serverID + "/" + fileID).mkdir();

        FileOutputStream newFile = new FileOutputStream("Peer_" + this.peer.serverID + "/" + fileID + "/" + chunkNo);
        newFile.write(body);
        newFile.close();


	}

    /**
     * Creates the packet with all the chunk information and the required arguments of the header.
     * @param header Message header
     * @param body Chunk content
     * @return Returns the created packet.
     * @throws IOException
     */
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

    /**
     * Sends the reply with the STORED message after the chunk was created.
     * @param packet Packet with the STORED message
     * @throws IOException
     */
    public void sendStored(DatagramPacket packet) throws IOException {
        this.peer.getMc().getSocket().send(packet);
    }


}