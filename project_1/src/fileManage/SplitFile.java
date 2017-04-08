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

				byte[] BHeader = header.getBytes();

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				outputStream.write( BHeader );
				outputStream.write( buffer );

				byte c[] = outputStream.toByteArray( );

				DatagramPacket packet = new DatagramPacket(c, c.length,mdb.getAdress(),mdb.getPort());
				mdb.getSocket().send(packet);

			}
		}
	}


}
