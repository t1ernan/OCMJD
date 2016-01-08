package suncertify.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Converter.
 */
public class Utils {

	/** The Constant CHARACTER_ENCODING. */
	public static final String CHARACTER_ENCODING = "US-ASCII";

	public static boolean isEightDigits(final String number) {
		return number.matches("[0-9]{8}");
	}
	
	public static int[] convertIntegerListToIntArray(List<Integer> list) {
		final int[] intArray = new int[list.size()];
		for (int index = 0; index < list.size(); index++) {
			intArray[index] = list.get(index);
		}
		return intArray;
	}

	public static String convertBytesToString(byte[] valueBytes) throws IOException {
		return new String(valueBytes, Charset.forName(CHARACTER_ENCODING));
	}

	public static byte[] convertStringToBytes(String message) {
		return message.getBytes(Charset.forName(CHARACTER_ENCODING));
	}
}
