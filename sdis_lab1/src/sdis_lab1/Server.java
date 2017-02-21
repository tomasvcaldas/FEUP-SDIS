package sdis_lab1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

	private static HashMap<String, String> plates;
	private static int port;
	
	private static int PLATESIZE = 8;
	private static int REGISTER = 1;
	private static int LOOKUP = 2;
	
	public static void main(String[] args) throws IOException {
		plates = new HashMap<String, String>();
		String arguments[] = null;
		while(!checkArguments(arguments)){
			arguments = getArguments();
		}
		
		System.out.println("Arguments validated");
		
		//create and open socket
		port = Integer.parseInt(arguments[0]);
		DatagramSocket socket = new DatagramSocket(port);
		
		boolean status = true;
		while(status){
			System.out.println("receiving...");
			//receiving
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			socket.receive(packet);
			String request = new String(packet.getData(),0,packet.getLength());
			String answer = workoutAnswer(request);
			if(answer == null)
				answer = "Car not found";
			
			//sending
			buf = answer.getBytes();
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf,buf.length,address,port);
			socket.send(packet);
			System.out.println("Answer sent");
			
		}
		System.out.println("Terminating server...");
		socket.close();
		System.out.println("Server terminated");
	}
	
	
	
	
	
	private static String workoutAnswer(String request) {
		int type = checkIfLookUpOrRegister(request);
		if(type == REGISTER) {
			String newPlate = getNewPlate(request);
			String owner = getOwner(request);
			plates.put(newPlate, owner);
			return (newPlate + " which owner is: " + owner);
		} else if (type == LOOKUP) {
			String plate = getPlate(request);
			return plates.get(plate);
		} else
			return null;
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
	
	
	private static String[] getArguments() {
		System.out.println("Write your arguments as: [<Port>]");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String temp = s.nextLine();
		return temp.split("\\s+");
	}

	private static boolean checkArguments(String[] args) {
		if (args == null)
			return false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("")) {
				System.out.println("Arguments error, expected: [<Port>]");
				return false;
			}
		}
		if (args.length != 1) {
			System.out.println("Arguments error, expected: [<Port>]");
			return false;
		}
		return true;
	}
	
}
