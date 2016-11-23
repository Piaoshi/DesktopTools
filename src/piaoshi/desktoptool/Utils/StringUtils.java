package piaoshi.desktoptool.Utils;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Deal String
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class StringUtils {

	// I would re-implement this method, and do not use outer library
	public static String escapeSpecial(String initial) {
		return StringEscapeUtils.escapeJava(initial);
	}
}
