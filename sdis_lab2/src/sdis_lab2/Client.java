package sdis_lab2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private static int mcast_port;
	private static InetAddress mcast_addr;
	private static String[] argumentsFromConsole = null;
	
	private static int MAX_SIZE = 256;	
	
	public static void main(String[] args) throws IOException {
		while (!checkArguments()) {
			getArguments();
		}
		
		byte[] buf = new byte[256];
		MulticastSocket client_mcast_socket = new MulticastSocket(mcast_port);
		client_mcast_socket.joinGroup(mcast_addr);
		
		DatagramPacket received_message = new DatagramPacket(buf, buf.length);
		client_mcast_socket.receive(received_message);
			
		String receivedStringMessage = new String(buf, 0, buf.length).trim();		
		
		int service_port = Integer.parseInt(receivedStringMessage);
		
		System.out.println("multicast:<" + mcast_addr + "><" + mcast_port + ">:<" + received_message.getAddress().getHostAddress() + "><" + service_port + ">");

		DatagramSocket request_socket = new DatagramSocket();
		buf = new byte[MAX_SIZE];
		buf = createMessage().getBytes();
		
		InetAddress service_address = received_message.getAddress();
		
		DatagramPacket request_pakcet = new DatagramPacket(buf, buf.length, service_address, service_port);
		request_socket.send(request_pakcet);
		
		buf = new byte[MAX_SIZE];
		request_pakcet = new DatagramPacket(buf, buf.length);
		request_socket.receive(request_pakcet);
		String answer = new String(request_pakcet.getData(), 0, request_pakcet.getLength());

		String requestToPrint = "";
		for (int i = 3; i < argumentsFromConsole.length; i++)
			requestToPrint += argumentsFromConsole[i];
		System.out.println("<" + argumentsFromConsole[2] + "><" + requestToPrint + ">::<" + answer + ">");
		
		request_socket.close();
		client_mcast_socket.close();
	}

	private static String createMessage() {
		String temp = "";
		for(int i = 2; i < argumentsFromConsole.length; i++)
			temp += argumentsFromConsole[i];
		return temp;
	}

	private static void getArguments() throws UnknownHostException {
		System.out.println("Write your arguments as: [<mcast_addr> <mcast_port> <oper> *]");
		@SuppressWarnings("resource")
		String temp = new Scanner(System.in).nextLine();
		argumentsFromConsole = temp.split("\\s+");
		mcast_port = Integer.parseInt(argumentsFromConsole[1]);
		mcast_addr = InetAddress.getByName(argumentsFromConsole[0]);
	}

	private static boolean checkArguments() {
		if (argumentsFromConsole == null)
			return false;
		for (int i = 0; i < argumentsFromConsole.length; i++) {
			if (argumentsFromConsole[i].equals("")) {
				System.out.println("Arguments error, expected: [<mcast_addr> <mcast_port> <oper> *]");
				return false;
			}
		}
		if (argumentsFromConsole.length < 3) {
			System.out.println("Arguments error, expected: [<mcast_addr> <mcast_port> <oper> *]");
			return false;
		}
		return true;
	}
}