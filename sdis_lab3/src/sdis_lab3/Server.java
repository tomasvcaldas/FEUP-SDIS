package sdis_lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
	
	private static HashMap<String, String> plates = new HashMap<String, String>();
	
	private static int port_srv;
	
	public static void main(String[] args) throws IOException {
		String arguments[] = null;
		
		while (!checkArguments(arguments)) {
			arguments = getArguments();
		}
		
		ServerSocket srv_socket = null;
		
		try{
			srv_socket = new ServerSocket(port_srv);
		} catch (Exception e){
			System.out.println("Server socket error" + e.getMessage());
			return;
		}
		
		while(true){
			Socket socket = null;
			socket = srv_socket.accept();
			BufferedReader info_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter info_out = new PrintWriter(socket.getOutputStream(), true);
			
			String[] request = info_in.readLine().split("\\s+");
			String answer = workoutAnswer(request);
			
			info_out.println(answer);
			
			info_out.close();
			info_in.close();
			socket.close();
		}
	}
	
	private static String workoutAnswer(String[] request){
		switch(request[0]){
		case "REGISTER":
			if (plates.containsKey(request[1])){
				System.out.println("You tried to 'REGISTER' with a plate that already exists: " + request[1] + "  " + request[2]);
				return "Plate already exists";
			}
			plates.put(request[1], request[2]);
			System.out.println("New plate added. Car registered with the plate number " + request[1] + " and owner " + request[2]);
			return "Car registered";
			
		case "LOOKUP":
			if (!plates.containsKey(request[1])){
				System.out.println("Car not found with this plate number: " + request[1]);
				return "Car not found";
			}
			System.out.println("The owner of this plate number is " + plates.get(request[1]));
			return plates.get(request[1]);
			
		default:
			break;
		}
		
		return "UKNOWN ERROR";
	}
	
	private static String[] getArguments() throws UnknownHostException {
		System.out.println("Write your arguments as: [<srv_port>");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String temp = s.nextLine();
		String[] tempo = temp.split("\\s+");
		
		//Creating and opening socket
		try{
			port_srv = Integer.parseInt(tempo[0]);
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
		
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
