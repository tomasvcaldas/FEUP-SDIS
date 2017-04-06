package fileManage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SplitFile {
	
	public static void splitFile(String fileName, int repDeg,String serverId) throws IOException{
		File f = new File(fileName);
		int chunkCounter = 0;
		int serverID = Integer.parseInt(serverId);
		int chunkSize = 512000;
		
		byte[] buffer = new byte[chunkSize];
		
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))){

			int tmp = 0;
			while((tmp = bis.read(buffer)) > 0){
				chunkCounter++;
				//fazer o has
				createPutHeader(fileName,repDeg,chunkCounter, serverID,fileID )

				// enviar o conteudo do ficheiro com o buffer ( cada 1 tem - body )
				// e o header msgtype,version ( 1.0 ),id do peer passado na consola, fileid (fazer a hash), o chunkno (chunkcounter), repdegree
				// criar o datagrampacket com a string com o header os crlfs o body e enviar para o backup ou para o controlo, ainda temos de ver
				//try(FileOutputStream out = new FileOutputStream(newF)){

				//	out.write(buffer, 0, tmp);
				//} //esta parte é para cagar porque nao interessa ter as cenas locais
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
