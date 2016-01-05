/*
 * 
 */
package test.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO: Auto-generated Javadoc
/**
 * The Class DBFileReader.
 */
public class DBFileReader {

	/** The Constant CHARACTER_ENCODING. */
	private static final String CHARACTER_ENCODING = "US-ASCII";
	
	/** The Constant DATABASE_FILE_LOCATION. */
	private static final String DATABASE_FILE_LOCATION = "db-2x2.db";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {

		Path databasePath = Paths.get(DATABASE_FILE_LOCATION);

		try {
			ByteBuffer byteBuffer = ByteBuffer.wrap(Files.readAllBytes(databasePath));
			final int magicCookie = byteBuffer.getInt();
			final int recordZeroOffset = byteBuffer.getInt();
			final int numberOfFields = byteBuffer.getShort();
			final int[] fieldValueSizes = new int[numberOfFields];
			final String[] fieldValues = new String[numberOfFields];
			final String[] fieldNames = new String[numberOfFields];

			int recordCount = 0;

			for (int index = 0; index < numberOfFields; index++) {
				final int fieldNameSize = byteBuffer.getShort();
				final String fieldName = readString(byteBuffer, fieldNameSize);
				final int fieldValueSize = byteBuffer.getShort();
				fieldNames[index] = fieldName;
				fieldValueSizes[index] = fieldValueSize;
				System.out.println("fieldValueSize " + index + ": " + fieldValueSize);
			}
			System.out.println("magicCookie: " + magicCookie);
			System.out.println("recordZeroOffset: " + recordZeroOffset);

			while (byteBuffer.hasRemaining()) {
				final int flagvalue = byteBuffer.getShort();
				System.out.println("##################### START RECORD #####################");
				for (int index = 0; index < numberOfFields; index++) {
					final String fieldValue = readString(byteBuffer, fieldValueSizes[index]);
					fieldValues[index] = fieldValue;
				}
				for (int index = 0; index < numberOfFields; index++) {
					System.out.println(fieldNames[index] + ": " + fieldValues[index]);
				}
				System.out.println("Record is " + isRecordValid(flagvalue));
				System.out.println("###################### END RECORD ######################");
				System.out.println();
				recordCount++;
			}
			System.out.println("Total record count: " + recordCount);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is record valid.
	 *
	 * @param flagvalue the flagvalue
	 * @return the string
	 */
	private static String isRecordValid(int flagvalue) {
		if (flagvalue == 00) {
			return "VALID";
		} else {
			return "INVALID";
		}
	}

	/**
	 * Read string.
	 *
	 * @param byteBuffer the byte buffer
	 * @param size the size
	 * @return the string
	 */
	private static String readString(ByteBuffer byteBuffer, int size) {
		final byte[] bytes = new byte[size];
		byteBuffer.get(bytes);
		return new String(bytes, Charset.forName(CHARACTER_ENCODING));
	}

}
