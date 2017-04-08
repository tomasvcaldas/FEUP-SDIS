package Channels;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

import Utilities.Header;
import Utilities.Message;

import fileManage.MessageType;
import peer.Peer;

public class BackupChannel extends Channel {

	private Peer peer;

	public BackupChannel(String backupAddress, String backupPort, Peer peer ) throws IOException{
		super(backupAddress,backupPort);
		this.thread = new BackupThread();
		this.peer = peer;
	}

	public class BackupThread extends Thread {
		public void run(){
			System.out.println("BACKUP: reading...");
			while(true){
				try{
					DatagramPacket newPacket = getMulticastData();

					Message message = Message.getMessage(newPacket);

					Header headerArgs = message.getHeader();
					byte[] body = message.getBody();




					if(!headerArgs.getSenderId().equals(peer.serverID)){
						if(headerArgs.getType() == MessageType.PUTCHUNK){
							System.out.println("PUTCHUNK received, starting the handle...");
							PutchunkReceived(headerArgs,body);
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

		String stored = Message.createStoredHeader(this.peer.serverID, fileID, chunkNo);

		byte[] bStored = stored.getBytes();

		DatagramPacket packet = new DatagramPacket(bStored, bStored.length, this.peer.getMc().getAdress(),this.peer.getMc().getPort());
		this.peer.getMc().getSocket().send(packet);



	}





}