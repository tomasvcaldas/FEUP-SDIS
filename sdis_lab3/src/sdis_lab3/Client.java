package sdis_lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Client {
	
	private static String[] argumentsFromConsole = null;
	private static InetAddress host_name = null;
	private static int port_number;
	
	public static void main(String[] args) throws IOException{
		/*while(!checkArguments()){
			getArguments();
		}
		
		Socket socket = new Socket(host_name, port_number);
		
		BufferedReader info_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter info_out = new PrintWriter(socket.getOutputStream(), true);
		
		String[] request_send = argumentsFromConsole[2];
		String answer = workoutAnswer(request);
		
		info_out.println(answer);
		
		info_out.close();
		info_in.close();
		socket.close();*/

	}
}
