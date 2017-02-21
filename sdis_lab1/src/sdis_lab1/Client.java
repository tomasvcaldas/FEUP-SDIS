package sdis_lab1;


import java.util.Scanner;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client {
		
	private static int port;
	public static void main(String[] args) throws IOException{
		String arguments[] = null;
		
		while(!checkArguments(arguments)){
			arguments = getArguments();
		}
		System.out.println("Valid arguments");
		
		//envio do pacote
		DatagramSocket socket = new DatagramSocket(); //socket usado para enviar e receber o packet 
		byte[] buf = new byte[256];
		buf = createMessage(arguments).getBytes();
		InetAddress address = InetAddress.getByName(arguments[0]);
		port = Integer.parseInt(arguments[1]);
		DatagramPacket packet = new DatagramPacket(buf,buf.length, address,port);
		socket.send(packet);
		
		System.out.println("Packet sent, waiting answer.");
		
		//receber o pacto com a resposta, criar o datagrampacket vazio 
		
		buf =  new byte[256];
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		System.out.println("Packet received.");
		String answer = new String(packet.getData(), 0, packet.getLength());
		System.out.println("Answer is " + answer);
		socket.close();
		System.out.println("Client terminated");
		
		
	}
	
	
	public static boolean checkArguments(String[] args){
		if (args ==  null) return false;
		
		for(int i= 0; i < args.length; i++){
			if(args[i].equals("")){
				System.out.println("Arguments expected as: [<Address> <Port> <Args>] ");
				return false;
			}
		}
		if (args.length < 3){
			System.out.println("Arguments expected as: [<Address> <Port> <Args>] ");
			return false;
		}
		return true;
	}
	
	public static String[] getArguments(){
		System.out.println("Write arguments as: [<Address> <Port> <Args>] ");
		@SuppressWarnings("resource")
		String temp = new Scanner(System.in).nextLine();
		return temp.split("\\s+");
	}
	
	public static String createMessage(String[] args){
		String temp = "";
		for (int i = 2; i < args.length ; i++)
			temp += args[i];
		return temp;
		
	}

}
