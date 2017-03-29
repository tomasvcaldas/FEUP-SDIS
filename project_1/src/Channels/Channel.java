package Channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;


public class Channel {
	Thread thread;
	protected MulticastSocket multicastsocket;
	protected InetAddress address;
	protected int port;


	Channel(String address, String port) throws IOException{
		this.address = InetAddress.getByName(address);
		this.port = Integer.parseInt(port);
		this.multicastsocket = new MulticastSocket(this.port);
		this.multicastsocket.setTimeToLive(1);
		this.multicastsocket.joinGroup(address);

	}





	public MulticastSocket getSocket(){
		return multicastsocket;
	}

	public InetAddress getAdress(){
		return address;
	}



}
