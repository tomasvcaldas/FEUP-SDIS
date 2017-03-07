package sdis_lab2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
	
	private static HashMap<String, String> plates;
	private static int srv_port;
	public static int mcast_port;
	public static InetAddress mcast_addr;
	private static int PLATESIZE = 8;
	private static int REGISTER = 1;
	private static int LOOKUP = 2;

	public static void main(String[] args) throws IOException {
		plates = new HashMap<String, String>();
		String arguments[] = null;
		while(!checkArguments(arguments)){
			arguments = getArguments();
		}
		
		DatagramSocket service_socket = new DatagramSocket(srv_port);

		MulticastSocket mcast_socket = new MulticastSocket(mcast_port);
		mcast_socket.setTimeToLive(1);
		
		DatagramPacket mcast_packet = new DatagramPacket(("" + srv_port).getBytes(), (srv_port + "").getBytes().length, mcast_addr, mcast_port);
		
		
		boolean status = true;
		
		while(status){
			mcast_socket.send(mcast_packet);
			
			//System.out.println("multicast:<" + mcast_addr + "><" + mcast_port + ">:<srvc_addr><" + srv_port + ">");
			
			service_socket.setSoTimeout(1000);
			
			try{
				receive_and_answer(service_socket);
			} catch (SocketTimeoutException timeout_exception){
				
			}
		}
		
		mcast_socket.close();
		service_socket.close();
	}
	
	public static void receive_and_answer(DatagramSocket service_socket) throws IOException{
		byte[] request_buffer = new byte[256];
		
		DatagramPacket service_packet = new DatagramPacket(request_buffer, request_buffer.length);
		
		service_socket.receive(service_packet);
		
		String request = new String(service_packet.getData(), 0, service_packet.getLength());
		
		String answer = getAnswer(request);
		
		request_buffer = new byte[256];
		
		request_buffer = answer.getBytes();
		InetAddress request_address = service_packet.getAddress();
		int request_port = service_packet.getPort();
		service_packet = new DatagramPacket(request_buffer, request_buffer.length, request_address, request_port);
		service_socket.send(service_packet);
		
	}
	
	public static String getAnswer(String request){
		int type = checkType(request);
		
		if(type == REGISTER){
			String newPlate = getNewPlate(request);
			String owner = getOwner(request);
			plates.put(newPlate, owner);
			
			System.out.println("<REGISTER><" + newPlate + "><" + owner + ">");
			
			return (newPlate + " which owner is: " + owner);
		} else if(type == LOOKUP){
			String plate = getPlate(request);
			String owner = getOwner(request);
			if (owner == null){
				System.out.println("<LOOKUP><" + plate + ">::<Car owner not found>");
				return "Car owner not found";
			}
			else{
				System.out.println("<LOOKUP><" + plate + ">::<" + owner + ">");
				return owner;
			}
		} else
			return "Car not found";
	}
	
	private static String getPlate(String request) {
		return request.substring("LOOKUP".length(), PLATESIZE + "LOOKUP".length());
	}

	private static String getOwner(String request) {
		return request.substring(PLATESIZE + "REGISTER".length(), request.length());
	}

	private static String getNewPlate(String request) {
		return request.substring("REGISTER".length(), PLATESIZE + "REGISTER".length());
	}
	
	private static int checkType(String request) {
		String register = "REGISTER";
		String lookUp = "LOOKUP";
		for (int i = 0; i < register.length(); i++) {
			if (register.charAt(i) != request.charAt(i))
				break;
			if(i == (register.length() - 1))
				return 1;
		}
		for (int i = 0; i < register.length(); i++) {
			if (lookUp.charAt(i) != request.charAt(i))
				break;
			if(i == (lookUp.length() - 1))
				return 2;
		}
		return 0;
	}
	
	public static boolean checkArguments(String args[]){
		if (args == null)
			return false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("")) {
				System.out.println("Arguments error, expected: server_port multicast_address multicast_port");
				return false;
			}
		}
		if (args.length != 3) {
			System.out.println("Arguments error, expected: server_port multicast_address multicast_port");
			return false;
		}
		return true;
	}
	
	public static String[] getArguments() throws UnknownHostException{
		System.out.println("Write your arguments as: [<srvc_port> <mcast_addr> <mcast_port>]");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String temp = s.nextLine();
		String[] tempo = temp.split("\\s+");
		
		//Creating and opening socket
		srv_port = Integer.parseInt(tempo[0]);
		mcast_port = Integer.parseInt(tempo[2]);		
		mcast_addr = InetAddress.getByName(tempo[1]);
		return tempo;
	}
}
