package Channels;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

import Utilities.Header;
import Utilities.Message;

import fileManage.MessageType;

public class BackupChannel extends Channel {

	public BackupChannel(String backupAddress, String backupPort ) throws IOException{
		super(backupAddress,backupPort);
		this.thread = new BackupThread();
	}

	public class BackupThread extends Thread {
		public void run(){
			System.out.println("Inside backup channel...");
			while(true){
				try{
					multicastsocket.joinGroup(address);
					DatagramPacket newPacket = getMulticastData();
					Message message = Message.getMessage(newPacket);
					Header headerArgs = message.getHeader();
					String msgBody = message.getBody();
					byte[] body = msgBody.getBytes();

					/*if(headerArgs.getSenderId() != /*server id){*/
						if(headerArgs.getType() == MessageType.PUTCHUNK){
							System.out.println("PUTCHUNK received, starting the handle...");
							PutchunkReceived(headerArgs,body);
						}
					//}
					multicastsocket.leaveGroup(address);
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
		String fileName = createMessage(fileID,chunkNo);


		FileOutputStream newFile = new FileOutputStream(fileName);
		newFile.write(body);
		newFile.close();


	}

	private String createMessage(String id, int no){
		String finalMsg = id + no;
		System.out.println(finalMsg);
		return finalMsg;
	}



}