package fileManage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class mergeFile {

	//http://stackoverflow.com/questions/10864317/how-to-break-a-file-into-pieces-using-java

	public static void mergeFiles(ArrayList<byte[]> body, File into) throws IOException {
		into.getParentFile().mkdirs();
		try (BufferedOutputStream mergingStream = new BufferedOutputStream(
				new FileOutputStream(into))) {
			for (byte[] b : body) {
				mergingStream.write(b);
			}
		}
	}
}
