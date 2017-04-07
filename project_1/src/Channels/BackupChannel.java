package Channels;


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
			System.out.println("Inside backup channel...");
			while(true){
				try{
					multicastsocket.joinGroup(address);
					System.out.println("Joined group successfully");
					DatagramPacket newPacket = getMulticastData();
					System.out.println("Before getMessage");
					Message message = Message.getMessage(newPacket);
					System.out.println("After getMessage");
					Header headerArgs = message.getHeader();
					String msgBody = message.getBody();
					byte[] body = msgBody.getBytes();

					System.out.println("message_Sender_id = " + headerArgs.getSenderId() + "; peer_id = " + peer.serverID);

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
					multicastsocket.leaveGroup(address);
				} catch(Exception e){
					//e.printStackTrace();
			}

			}

		}
		
	}

	private void PutchunkReceived(Header header, byte[] body) throws IOException{
		String fileID = header.getFileId();
		int chunkNo = Integer.parseInt(header.getChunkNumber());
		int repDegree = Integer.parseInt(header.getChunkNumber());
		String fileName = createMessage(fileID,chunkNo);


		FileOutputStream newFile = new FileOutputStream(fileName);
		newFile.write(body);
		newFile.close();


	}

	private String createMessage(String id, int no){
		String number = Integer.toString(no);
		String finalMsg = id + number;
		System.out.println(finalMsg);
		return finalMsg;
	}



}