package fileManage;

import Channels.BackupChannel;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.util.Scanner;

import static Utilities.Hash.sha256;
import static Utilities.Message.createPutHeader;
import static Channels.ControlChannel.getReceivedStores;
import static java.sql.Types.NULL;

public class SplitFile {

	//http://stackoverflow.com/questions/10864317/how-to-break-a-file-into-pieces-using-java
	
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
				outputStream.write( buffer,0,tmp );

				byte c[] = outputStream.toByteArray( );

				DatagramPacket packet = new DatagramPacket(c, c.length,mdb.getAdress(),mdb.getPort());

				mdb.getSocket().send(packet);
				backupChunk(packet,repDeg,hashedFile,serverId,peer_id,mdb,chunkCounter);

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void backupChunk(DatagramPacket packet, int repDeg,String fileName,String serverId,String peerId,BackupChannel mb,int chunkCounter) throws IOException, InterruptedException {
		int receivedSize;
		System.out.println("ENTROU NO BACKUPCHUNK");
		System.out.println(getReceivedStores().get(fileName));
		if(getReceivedStores().get(fileName) == null || getReceivedStores().get(fileName).get(chunkCounter) == null)
			receivedSize = 0;
		else
			receivedSize = getReceivedStores().get(fileName).get(chunkCounter).size();

		long delay = 1000;
		for (int i = receivedSize; i <= repDeg; i++){
			mb.getThread().sleep(delay);
			mb.getSocket().send(packet);
			System.out.println("SENDING CHUNK: " + chunkCounter);
			delay = delay *2 ;
			}
	}
}
