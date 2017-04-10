package Protocols;

import Channels.BackupChannel;
import peer.Peer;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.util.Scanner;

import static Utilities.Hash.sha256;
import static Utilities.Message.createPutHeader;
import static java.sql.Types.NULL;

public class Backup {

	//http://stackoverflow.com/questions/10864317/how-to-break-a-file-into-pieces-using-java

    /**
     * Splits the file in all the chunks and calls the function to send each one of them
     * @param fileName File name
     * @param repDeg Replication Degree
     * @param serverId Server Id
     * @param mdb Backup Channel
     * @param peer Respective per
     * @throws IOException
     */
	public static void splitFile(String fileName, int repDeg, String serverId, BackupChannel mdb, Peer peer) throws IOException{
		File f = new File(fileName);
		int chunkCounter = 0;
		int serverID = Integer.parseInt(serverId);
		int chunkSize = 64000;
		String fileId = sha256(fileName);
		
		byte[] buffer = new byte[chunkSize];

		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))){

			int tmp = 0;
			while((tmp = bis.read(buffer)) > 0){
				chunkCounter++;
				DatagramPacket packet = createBackupPacket(chunkCounter,fileId,repDeg,buffer,tmp,serverID,mdb);
				mdb.getSocket().send(packet);
				backupChunk(packet,repDeg,fileId,mdb,String.valueOf(chunkCounter), peer);

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    /**
     * Creates the packet with the Header and Body
     * @param chunkCounter chunk number
     * @param fileId fileId
     * @param repDeg Replication degree
     * @param buffer body
     * @param tmp length
     * @param serverID server Id
     * @param mdb BackupChannel
     * @return returns created packet
     * @throws IOException
     */
	public static DatagramPacket createBackupPacket(int chunkCounter,String fileId,int repDeg, byte[] buffer, int tmp, int serverID, BackupChannel mdb) throws IOException {


		String header = createPutHeader(serverID,fileId,chunkCounter, repDeg);

		byte[] BHeader = header.getBytes();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( BHeader );
		outputStream.write( buffer,0,tmp );

		byte c[] = outputStream.toByteArray( );

		DatagramPacket packet = new DatagramPacket(c, c.length,mdb.getAdress(),mdb.getPort());
		return packet;
	}

    /**
     * Tries to send all the putchunk messages( 5 times max )
     * @param packet packet to send
     * @param repDeg replication degree
     * @param fileName file name
     * @param mb BackupChannel
     * @param chunkCounter chunk Number
     * @param peer Respective peer
     * @throws IOException
     * @throws InterruptedException
     */
	public static void backupChunk(DatagramPacket packet, int repDeg,String fileName,BackupChannel mb,String chunkCounter, Peer peer) throws IOException, InterruptedException {
		int receivedSize;

		if(peer.getMc().getReceivedStores().get(fileName) == null || peer.getMc().getReceivedStores().get(fileName).get(chunkCounter) == null)
			receivedSize = 0;
		else
			receivedSize = peer.getMc().getReceivedStores().get(fileName).get(chunkCounter).size();

		long delay = 1000;

		int maxTries = 0;

		mb.getThread().sleep(delay);
		while(receivedSize < repDeg && maxTries < 5){
			maxTries++;
			mb.getSocket().send(packet);
			System.out.println("SENDING PUTCHUNK with CHUNKNUMBER: " + chunkCounter);
			delay = delay * 2;
			if(peer.getMc().getReceivedStores().get(fileName) == null || peer.getMc().getReceivedStores().get(fileName).get(chunkCounter) == null)
				receivedSize = 0;
			else
				receivedSize = peer.getMc().getReceivedStores().get(fileName).get(chunkCounter).size();
			mb.getThread().sleep(delay);
		}

		if(maxTries > 5){
			System.out.println("Aborting backup. Exceeding max timeout...");
		}
	}
}
