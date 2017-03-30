package Channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;



public class Channel{
	Thread thread;
	protected MulticastSocket multicastsocket;
	protected InetAddress address;
	protected int port;


	Channel(String address, String port) {
		try{
			this.address = InetAddress.getByName(address);
			this.port = Integer.parseInt(port);
			this.multicastsocket = new MulticastSocket(this.port);
			this.multicastsocket.setTimeToLive(1);
		
		} catch(Exception e){
			e.printStackTrace();
		}

	}


	public void startThread(){
		this.thread.start();
	}

	public DatagramPacket getMulticastData() throws IOException{
			byte[] buf = new byte[64000];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			multicastsocket.receive(packet);
			return packet;
	}

	


	public MulticastSocket getSocket(){
		return multicastsocket;
	}

	public InetAddress getAdress(){
		return address;
	}



}
