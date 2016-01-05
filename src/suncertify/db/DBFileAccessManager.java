package suncertify.db;

import static suncertify.util.Constants.DELETED_FLAG;
import static suncertify.util.Constants.EXPECTED_MAGIC_COOKIE;
import static suncertify.util.Constants.VALID_FLAG;
import static suncertify.util.DataConverter.convertBytesToString;
import static suncertify.util.DataConverter.convertStringToBytes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;

import suncertify.util.Constants;

/**
 * The class DBFileAccessManager is responsible for reading and writing to the
 * non-relational database file.
 */
public class DBFileAccessManager {

	/** The location of the database on the file system. */
	private final String dbFileLocation;

	/** The number of fields in a record. */
	private int numberOfFields;

	/**
	 * An array containing the number of bytes each record field takes up in the
	 * database.
	 */
	private int[] fieldValueSizes;

	/** An array containing the names of fields in each record. */
	private String[] fieldNames;

	/**
	 * Constructs a new DBFileAccessManager using the specified
	 * {@code dbFileLocation}.
	 *
	 * @param dbFileLocation
	 *            the location of the database on the file system
	 */
	public DBFileAccessManager(final String dbFileLocation) {
		this.dbFileLocation = dbFileLocation;
	}

	/**
	 * Reads the contents of the database file and loads its records into the
	 * specified map.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values
	 * @throws DatabaseException
	 *             if an unexpected IO exception occurs when trying to read file
	 */
	public void readDatabaseIntoCache(final Map<Integer, String[]> map) throws DatabaseException {
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			checkMagicCookie(dbFile);
			final int recordOffset = dbFile.readInt();
			numberOfFields = dbFile.readShort();
			fieldNames = new String[numberOfFields];
			fieldValueSizes = new int[numberOfFields];
			readSchemaDescription(dbFile);
			checkRecordOffset(dbFile, recordOffset);
			readDataSection(map, dbFile);
		} catch (IOException e) {
			throw new DatabaseException("Could not read data: " + e);
		}
	}

	/**
	 * Persists the the elements of the specified map to the database file,
	 * overwriting the existing data section of the database file.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values
	 * @throws DatabaseException
	 *             if an unexpected IO exception occurs when trying to write the
	 *             data to the database file
	 */
	public void persist(final Map<Integer, String[]> map) throws DatabaseException {
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			checkMagicCookie(dbFile);
			final int recordOffset = dbFile.readInt();
			dbFile.seek(recordOffset);
			dbFile.setLength(recordOffset);
			writeDataSection(map, dbFile);
		} catch (IOException e) {
			throw new DatabaseException("Could not save data: " + e);
		}
	}

	/**
	 * Read the data section of the dbFile and load its records into the
	 * supplied map.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values.
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DatabaseException
	 *             if an unexpected IO exception occurs when trying to read from
	 *             the database file
	 */
	private void readDataSection(final Map<Integer, String[]> map, RandomAccessFile dbFile)
			throws IOException, DatabaseException {
		int recordNumber = 0;
		while (dbFile.getFilePointer() < dbFile.length()) {
			final String[] fieldValues = new String[numberOfFields];
			final int flagvalue = dbFile.readUnsignedShort();
			readRecord(dbFile, fieldValues);

			if (flagvalue == VALID_FLAG) {
				map.put(recordNumber, fieldValues);
			} else if (flagvalue == DELETED_FLAG) {
				map.put(recordNumber, null);
			} else {
				throw new DatabaseException("Corrupted flag value: " + flagvalue);
			}
			recordNumber++;
		}
	}

	/**
	 * Read a record from the {@code dbFile} and populates the
	 * {@code fieldValues} parameter with the record's fields.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param fieldValues
	 *            a String array, each element represents a field of a record
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readRecord(final RandomAccessFile dbFile, final String[] fieldValues) throws IOException {
		for (int index = 0; index < numberOfFields; index++) {
			final String fieldValue = readString(dbFile, fieldValueSizes[index]);
			fieldValues[index] = fieldValue;
		}
	}

	/**
	 * Read the schema description section of the database file and sets the
	 * elements of {@code fieldNames} and {@code fieldValueSizes} field
	 * variables with the schema information.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readSchemaDescription(final RandomAccessFile dbFile) throws IOException {
		for (int index = 0; index < numberOfFields; index++) {
			final int fieldNameSize = dbFile.readShort();
			final String fieldName = readString(dbFile, fieldNameSize);
			final int fieldValueSize = dbFile.readShort();
			fieldNames[index] = fieldName;
			fieldValueSizes[index] = fieldValueSize;
		}
	}

	/**
	 * Writes the records stored in the {@code map} parameter to the data
	 * section of the database file, overwriting the existing data section of
	 * the database file.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values.
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeDataSection(final Map<Integer, String[]> map, final RandomAccessFile dbFile) throws IOException {
		for (String[] fieldValues : map.values()) {
			writeFlag(dbFile, fieldValues);
			for (int index = 0; index < numberOfFields; index++) {
				writeString(dbFile, fieldValues[index], fieldValueSizes[index]);
			}
		}
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
	private String readString(final RandomAccessFile dbFile, final int numberOfBytes) throws IOException {
		final byte[] valueBytes = readBytes(dbFile, numberOfBytes);
		final String paddedFieldValue = convertBytesToString(valueBytes);
		return paddedFieldValue.trim();
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
	private void writeString(final RandomAccessFile dbFile, final String fieldValue, final int fieldSize)
			throws IOException {
		final byte[] unpaddedBytes = convertStringToBytes(fieldValue);
		final byte[] paddedBytes = addPadding(unpaddedBytes, fieldSize);
		dbFile.write(paddedBytes);
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
	private byte[] readBytes(final RandomAccessFile dbFile, final int numberOfBytes) throws IOException {
		final byte[] valueBytes = new byte[numberOfBytes];
		dbFile.read(valueBytes);
		return valueBytes;
	}

	/**
	 * Compares the value of the specified record offset to the current offset
	 * of the specified {@code dbFile} and throws a {@link DatabaseException} if
	 * they do not match.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param recordOffset
	 *            the expected record offset value retrieved from the file
	 *            information
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DatabaseException
	 *             if the specified record offset value retrieved from the file
	 *             information does not match the current offset of the file
	 */
	private void checkRecordOffset(final RandomAccessFile dbFile, final int recordOffset)
			throws IOException, DatabaseException {
		if (recordOffset != dbFile.getFilePointer()) {
			throw new DatabaseException("Invalid Record Offset");
		}
	}

	/**
	 * Check magic cookie value specified in the file information of the
	 * specified {@code dbFile} matches the expected magic cookie value for the
	 * file.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DatabaseException
	 *             if the magicCookie value retrieved from the file information
	 *             does not match the expected magicCookie value of the file
	 */
	private void checkMagicCookie(final RandomAccessFile dbFile) throws IOException, DatabaseException {
		final int magicCookie = dbFile.readInt();
		if (magicCookie != EXPECTED_MAGIC_COOKIE) {
			throw new DatabaseException("Invalid Magic Cookie: " + magicCookie);
		}
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
	private byte[] addPadding(final byte[] unpaddedBytes, final int size) {
		final byte[] paddedBytes = new byte[size];
		Arrays.fill(paddedBytes, (byte) Constants.BLANK_SPACE_HEX);
		System.arraycopy(unpaddedBytes, 0, paddedBytes, 0, unpaddedBytes.length);
		return paddedBytes;
	}

	/**
	 * Writes the appropriate flag value to the specified {@code dbFile}. If the
	 * specified {@code value} is null it will write two bytes of value
	 * {@literal 0x8000} to the file. Otherwise, it will write two bytes of
	 * value {@literal 00} to the file.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param value
	 *            the String[] value used to determine the appropriate flag
	 *            value.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeFlag(final RandomAccessFile dbFile, final String[] value) throws IOException {
		if (value == null) {
			dbFile.writeShort(DELETED_FLAG);
		} else {
			dbFile.writeShort(VALID_FLAG);
		}
	}

}
