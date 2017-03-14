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
	private static int MAX_SIZE = 256;	
	
	public static void main(String[] args) throws IOException {
		plates = new HashMap<String, String>();
		String arguments[] = null;
		while (!checkArguments(arguments)) {
			arguments = getArguments();
		}

		// Create service socket
		DatagramSocket service_socket = new DatagramSocket(srv_port);
		// create multicastsocket
		MulticastSocket mcast_socket = new MulticastSocket(mcast_port);
		mcast_socket.setTimeToLive(1);		//set TTL to 1 hop
		// create multicast datagram packet
		DatagramPacket mcast_packet = new DatagramPacket(("" + srv_port).getBytes(), (srv_port + "").getBytes().length,
				mcast_addr, mcast_port);
		
		boolean status = true;		
		
		while(status) {
			mcast_socket.send(mcast_packet);
			
			//System.out.println("multicast:<" + mcast_addr + "><" + mcast_port + ">:<srvc_addr><" + srv_port + ">");
						
			//set a timeout to receive
			service_socket.setSoTimeout(1000);
			try {
				receiveRequestAndAnswerAccordingly(service_socket);
			} catch (SocketTimeoutException timeout_exception) {
				//System.out.println("Socket exception message: " + timeout_exception.getMessage());
			}
		}
		mcast_socket.close();
		service_socket.close();
	}

	private static void receiveRequestAndAnswerAccordingly(DatagramSocket service_socket) throws IOException {
		// prepare to receive request
		byte[] request_buffer = new byte[MAX_SIZE];
		DatagramPacket service_packet = new DatagramPacket(request_buffer, request_buffer.length);
		
		service_socket.receive(service_packet);
		
		String request = new String(service_packet.getData(), 0, service_packet.getLength());
				
		String answer = workoutAnswer(request);
				
		request_buffer = new byte[MAX_SIZE];
		request_buffer = answer.getBytes();
		InetAddress request_address = service_packet.getAddress();
		int request_port = service_packet.getPort();
		service_packet = new DatagramPacket(request_buffer, request_buffer.length, request_address, request_port);
		service_socket.send(service_packet);
	}

	private static String workoutAnswer(String request) {
		int type = checkIfLookUpOrRegister(request);
		if(type == REGISTER) {
			String newPlate = getNewPlate(request);
			String owner = getOwner(request);
			plates.put(newPlate, owner);
			System.out.println("<REGISTER><" + newPlate + "><" + owner + ">");
			return (newPlate + " which owner is: " + owner);
		} else if (type == LOOKUP) {
			String plate = getPlate(request);
			String owner = plates.get(plate);
			if (owner == null) {
				System.out.println("<LOOKUP><" + plate + ">::<Car owner not found>");
				return "Car owner not found";
			}
			System.out.println("<LOOKUP><" + plate + ">::<" + owner + ">");
			return owner;
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

	private static int checkIfLookUpOrRegister(String request) {
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

	private static String[] getArguments() throws UnknownHostException {
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

	private static boolean checkArguments(String[] args) {
		if (args == null)
			return false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("")) {
				System.out.println("Arguments error, expected: [<srvc_port> <mcast_addr> <mcast_port>]");
				return false;
			}
		}
		if (args.length != 3) {
			System.out.println("Arguments error, expected: [<srvc_port> <mcast_addr> <mcast_port>]");
			return false;
		}
		return true;
	}
}