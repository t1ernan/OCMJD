package suncertify.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Converter.
 */
public class Utils {

	/** The Constant CHARACTER_ENCODING. */
	private static final String CHARACTER_ENCODING = "US-ASCII";
	private static final int BLANK_SPACE_HEX = 0x20;

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

	/**
	 * Reads the specified number of bytes from the specified {@code dbFile},
	 * converts them into a String.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param numberOfBytes
	 *            the number of bytes which will be read from the file and
	 *            converted into a String
	 * @return the string representation of the bytes read
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readString(final RandomAccessFile dbFile, final int numberOfBytes) throws IOException {
		final byte[] valueBytes = readBytes(dbFile, numberOfBytes);
		final String paddedFieldValue = convertBytesToString(valueBytes);
		return paddedFieldValue.trim();
	}

	/**
	 * Reads the specified number of bytes from the specified {@code dbFile} and
	 * returns them as a byte[].
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param numberOfBytes
	 *            the number of bytes which will be read from the file.
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] readBytes(final RandomAccessFile dbFile, final int numberOfBytes) throws IOException {
		final byte[] valueBytes = new byte[numberOfBytes];
		dbFile.read(valueBytes);
		return valueBytes;
	}

	/**
	 * Converts the specified {@code fieldValue} String into a byte[] and writes
	 * it to the specified {@code dbFile}. If the length of the byte[] is less
	 * than maximum number of bytes the field should take up in the database,
	 * the remaining bytes are filled with blank spaces.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param fieldValue
	 *            the field value
	 * @param fieldSize
	 *            the maximum number of bytes the field should take up in the
	 *            database
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeString(final RandomAccessFile dbFile, final String fieldValue, final int fieldSize)
			throws IOException {
		final byte[] unpaddedBytes = convertStringToBytes(fieldValue);
		final byte[] paddedBytes = addPadding(unpaddedBytes, fieldSize);
		dbFile.write(paddedBytes);
	}

	/**
	 * Copies the specified {@code unpaddedBytes} into a new byte[] of length
	 * equal to the specified {@code size} filled with blank space ASCII
	 * characters. Returns the new byte[].
	 *
	 * @param unpaddedBytes
	 *            the bytes to copy into the byte[] filled with blank space
	 *            ASCII characters.
	 * @param size
	 *            the size of the byte[] filled with blank space ASCII
	 *            characters.
	 * @return the new byte[]
	 */
	public static byte[] addPadding(final byte[] unpaddedBytes, final int size) {
		final byte[] paddedBytes = new byte[size];
		Arrays.fill(paddedBytes, (byte) BLANK_SPACE_HEX);
		System.arraycopy(unpaddedBytes, 0, paddedBytes, 0, unpaddedBytes.length);
		return paddedBytes;
	}

}
