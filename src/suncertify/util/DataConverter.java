package suncertify.util;

import static suncertify.util.Constants.CHARACTER_ENCODING;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class DataConverter {

	public static int[] convertIntegerListToIntArray(List<Integer> list) {
		final int[] intArray = new int[list.size()];
		for (int index = 0; index < list.size(); index++) {
			intArray[index] = list.get(index);
		}
		return intArray;
	}

	/**
	 * Convert bytes to string.
	 *
	 * @param valueBytes the value bytes
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String convertBytesToString(byte[] valueBytes) throws IOException {
		return new String(valueBytes, Charset.forName(CHARACTER_ENCODING));
	}

	/**
	 * Convert string to bytes.
	 *
	 * @param message the message
	 * @return the byte[]
	 */
	public static  byte[] convertStringToBytes(String message) {
		return message.getBytes(Charset.forName(CHARACTER_ENCODING));
	}
}
