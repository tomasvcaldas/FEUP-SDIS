package Channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;



public class Channel{
	private Thread thread;
	private MulticastSocket multicastsocket;
	private InetAddress address;
	private int port;


	Channel(String address, String port) {
		try{
			this.address = InetAddress.getByName(address);
			this.port = Integer.parseInt(port);
			this.multicastsocket = new MulticastSocket(this.port);
			this.multicastsocket.setTimeToLive(1);
			multicastsocket.joinGroup(this.address);
		
		} catch(Exception e){
			e.printStackTrace();
		}
	}


	public void startThread(){
		this.thread.start();
	}

	public DatagramPacket getMulticastData() throws IOException{
			byte[] buf = new byte[65000];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			multicastsocket.receive(packet);
			System.out.println("DASDSADSADSA00");

			return packet;
	}

	


	public MulticastSocket getSocket(){
		return multicastsocket;
	}

	public InetAddress getAdress(){
		return address;
	}

	public int getPort() {
		return port;
	}

	public Thread getThread() { return thread; }

	public void setThread(Thread thread){
		this.thread=thread;
	}
}
