package piaoshi.desktoptool.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * deal File
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class FileUtils {
	public static void copy(File srcFile, File destFile) {
		try {
			FileInputStream fileInputStream = new FileInputStream(srcFile);
			FileOutputStream fileOutputStream = new FileOutputStream(destFile);

			byte[] temp = new byte[20];
			int len = 0;
			while (-1 != (len = fileInputStream.read(temp))) {
				fileOutputStream.write(temp, 0, len);
			}

			fileInputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
