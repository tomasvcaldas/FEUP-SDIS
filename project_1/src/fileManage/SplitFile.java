package fileManage;

import Channels.BackupChannel;

import java.io.*;
import java.net.DatagramPacket;
import java.util.Scanner;

import static Utilities.Hash.sha256;
import static Utilities.Message.createPutHeader;

public class SplitFile {
	
	public static void splitFile(String fileName, int repDeg, String serverId, BackupChannel mdb, String peer_id) throws IOException{
		File f = new File(fileName);
		int chunkCounter = 0;
		int serverID = Integer.parseInt(serverId);
		int chunkSize = 64000;
		
		byte[] buffer = new byte[chunkSize];

		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))){

			int tmp = 0;
			while((tmp = bis.read(buffer)) > 0){
				chunkCounter++;

				String hashedFile = sha256(fileName);
				String header = createPutHeader(serverID,hashedFile,chunkCounter, repDeg);
				System.out.println("funciona por favor: " + header);
				byte[] BHeader = header.getBytes();

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				outputStream.write( BHeader );
				outputStream.write( buffer );

				byte c[] = outputStream.toByteArray( );
				System.out.println("split file message size = " + c.length);

				DatagramPacket packet = new DatagramPacket(c, c.length,mdb.getAdress(),mdb.getPort());
				mdb.getSocket().send(packet);

			}
		}
	}

	/*public static void main(String[] args) throws IOException{
		System.out.println("Write the full path to the file you want to split in chunks");
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		splitFile(new File(path));
		System.out.println("File split successfuly!");
	}*/

}
