package proj;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class mergeFile {
	
	public static void mergeFiles(List<File> files, File into)
	        throws IOException {
	    try (BufferedOutputStream mergingStream = new BufferedOutputStream(
	            new FileOutputStream(into))) {
	        for (File f : files) {
	            Files.copy(f.toPath(), mergingStream);
	        }
	    }
	}
	
	public static void mergeFiles(File oneOfFiles, File into)
	        throws IOException {
	    mergeFiles(listOfFilesToMerge(oneOfFiles), into);
	}
	
	public static String getDestFileName(File oneOfFiles){
		 String tmpName = oneOfFiles.getName();//{name}.{number}
		 System.out.println("oi" + tmpName);
		 String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));//remove .{number}
		 
		 return destFileName;
	}
	
	public static List<File> listOfFilesToMerge(File oneOfFiles) {
		String tmpName = oneOfFiles.getName();//{name}.{number}
		String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));//remove .{number}
	    File[] files = oneOfFiles.getParentFile().listFiles((File dir, String name) -> name.matches(destFileName + "(.*)(\\d+)(.*)"));
	    Arrays.sort(files);//ensuring order 001, 002, ..., 010, ...
	    return Arrays.asList(files);
	}

	public static void main(String[] args) throws IOException{
		System.out.println("Write the full path to one of the files you want to merge");
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		
		System.out.println("Write the full path of the merged file");
		String new_path = scan.nextLine();
		
		
		mergeFiles(new File(path), new File(new_path));
		
		System.out.println("File merged successfuly!");
	}

}
