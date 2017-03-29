package Channels;


import java.io.IOException;
import java.net.DatagramPacket;

import Utilities.Message;
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



					multicastsocket.leaveGroup(address);
				} catch(Exception e){
					e.printStackTrace();
				}

			}

		}
		
	}


}