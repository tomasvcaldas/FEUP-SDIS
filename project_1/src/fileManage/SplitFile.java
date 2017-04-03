package fileManage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SplitFile {
	
	public static void splitFile(File f) throws IOException{
		int chunkCounter = 0;
		
		int chunckSize = 512000;
		
		byte[] buffer = new byte[chunckSize];
		
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))){
			
			String name = f.getName();
			int tmp = 0;
			while((tmp = bis.read(buffer)) > 0){
				File newF = new File(f.getParent(), name + String.format("%03d", chunkCounter++));

				// enviar o conteudo do ficheiro com o buffer ( cada 1 tem - body )
				// e o header msgtype,version ( 1.0 ),id do peer passado na consola, fileid (fazer a hash), o chunkno (chunkcounter), repdegree
				// criar o datagrampacket com a string com o header os crlfs o body e enviar para o backup ou para o controlo, ainda temos de ver
				try(FileOutputStream out = new FileOutputStream(newF)){

					out.write(buffer, 0, tmp);
				} //esta parte é para cagar porque nao interessa ter as cenas locais
			}
		}
	}

	public static void main(String[] args) throws IOException{
		System.out.println("Write the full path to the file you want to split in chunks");
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		splitFile(new File(path));
		System.out.println("File split successfuly!");
	}

}
