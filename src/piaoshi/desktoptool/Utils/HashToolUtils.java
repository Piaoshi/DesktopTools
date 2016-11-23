package piaoshi.desktoptool.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.io.IOUtils;

/**
 * Hash Method for HashTool.java
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class HashToolUtils {

	public static void closeInputStream(InputStream inputStream) {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String md5Hex(InputStream inputStream) throws IOException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, inputStream);
	}

	public static String sha1Hex(InputStream inputStream) throws IOException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, inputStream);
	}

	public static String sha256Hex(InputStream inputStream) throws IOException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, inputStream);
	}

	public static String sha512Hex(InputStream inputStream) throws IOException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, inputStream);
	}

	public static String md5Hex(String string) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, string);
	}

	public static String sha1Hex(String string) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, string);
	}

	public static String sha256Hex(String string) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, string);
	}

	public static String sha512Hex(String string) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return computeHash(md, string);
	}

	public static String computeHash(MessageDigest md, InputStream inputStream) throws IOException {
		byte[] temp = new byte[2048];
		int len = 0;
		while (-1 != (len = inputStream.read(temp))) {
			md.update(temp, 0, len);
		}
		return byteToHexString(md.digest());
	}

	public static String computeHash(MessageDigest md, String string) {
		md.update(string.getBytes());
		return byteToHexString(md.digest());
	}

	public static String byteToHexString(byte[] tmp) {
		// May lose zero
		// StringBuffer hexString = new StringBuffer();
		// for (int i=0;i<tmp.length;i++) {
		// hexString.append(Integer.toHexString(0xFF & tmp[i]));
		// }

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < tmp.length; i++) {
			hexString.append(Integer.toString((tmp[i] & 0xff) + 0x100, 16).substring(1));
		}

		return hexString.toString();
	}
}
