package suncertify.db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;

import suncertify.util.Constants;

import static suncertify.util.Constants.*;
import static suncertify.util.DataConverter.*;

/**
 * The class DBFileAccessManager is responsible for reading and writing to the
 * non-relational database file.
 */
public class DBFileAccessManager {

	/** The db file location. */
	private final String dbFileLocation;

	/** The number of fields. */
	private int numberOfFields;

	/** The field value sizes. */
	private int[] fieldValueSizes;

	/** The field names. */
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
			numberOfFields = dbFile.readShort();
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
			map.put(recordNumber, getRecordValue(flagvalue, fieldValues));
			recordNumber++;
		}
	}

	/**
	 * Read a record from the {@code dbFile} and populates the
	 * {@code fieldValues} parameter with its fields.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param fieldValues
	 *            a String array, each element represents a field of a record
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readRecord(RandomAccessFile dbFile, final String[] fieldValues) throws IOException {
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
	private void readSchemaDescription(RandomAccessFile dbFile) throws IOException {
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
	private void writeDataSection(final Map<Integer, String[]> map, RandomAccessFile dbFile) throws IOException {
		for (String[] fieldValues : map.values()) {
			dbFile.writeShort(getFlag(fieldValues));
			for (int index = 0; index < numberOfFields; index++) {
				writeString(dbFile, fieldValues[index], fieldValueSizes[index]);
			}
		}
	}

	/**
	 * Read string.
	 *
	 * @param dbFile
	 *            the db file
	 * @param numberOfBytes
	 *            the number of bytes
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String readString(RandomAccessFile dbFile, int numberOfBytes) throws IOException {
		final byte[] valueBytes = readBytes(dbFile, numberOfBytes);
		final String paddedFieldValue = convertBytesToString(valueBytes);
		return paddedFieldValue.trim();
	}

	/**
	 * Write string.
	 *
	 * @param dbFile
	 *            the db file
	 * @param fieldValue
	 *            the field value
	 * @param numberOfBytes
	 *            the number of bytes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeString(RandomAccessFile dbFile, String fieldValue, int numberOfBytes) throws IOException {
		final byte[] unpaddedBytes = convertStringToBytes(fieldValue);
		final byte[] paddedBytes = addPadding(unpaddedBytes, numberOfBytes);
		dbFile.write(paddedBytes);
	}

	/**
	 * Read bytes.
	 *
	 * @param dbFile
	 *            the db file
	 * @param numberOfBytes
	 *            the number of bytes
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private byte[] readBytes(RandomAccessFile dbFile, int numberOfBytes) throws IOException {
		final byte[] valueBytes = new byte[numberOfBytes];
		dbFile.read(valueBytes);
		return valueBytes;
	}

	/**
	 * Check record offset.
	 *
	 * @param dbFile
	 *            the db file
	 * @param recordOffset
	 *            the record offset
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DatabaseException
	 *             the database exception
	 */
	private void checkRecordOffset(RandomAccessFile dbFile, final int recordOffset)
			throws IOException, DatabaseException {
		if (recordOffset != dbFile.getFilePointer()) {
			throw new DatabaseException("Invalid datafile: Invalid Record Offset");
		}
	}

	/**
	 * Check magic cookie.
	 *
	 * @param dbFile
	 *            the db file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DatabaseException
	 *             the database exception
	 */
	private void checkMagicCookie(RandomAccessFile dbFile) throws IOException, DatabaseException {
		final int magicCookie = dbFile.readInt();
		if (magicCookie != EXPECTED_MAGIC_COOKIE) {
			throw new DatabaseException("Invalid datafile: Invalid Magic Cookie");
		}
	}

	/**
	 * Adds the padding.
	 *
	 * @param unpaddedBytes
	 *            the unpadded bytes
	 * @param size
	 *            the size
	 * @return the byte[]
	 */
	private byte[] addPadding(byte[] unpaddedBytes, int size) {
		final byte[] paddedBytes = new byte[size];
		Arrays.fill(paddedBytes, (byte) Constants.BLANK_SPACE_HEX);
		System.arraycopy(unpaddedBytes, 0, paddedBytes, 0, unpaddedBytes.length);
		return paddedBytes;
	}

	/**
	 * Gets the flag.
	 *
	 * @param value
	 *            the value
	 * @return the flag
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private int getFlag(final String[] value) throws IOException {
		return (value == null) ? DELETED_FLAG : VALID_FLAG;
	}

	private String[] getRecordValue(int flagvalue, String[] value) throws DatabaseException {
		if (flagvalue == VALID_FLAG) {
			return value;
		} else if (flagvalue == DELETED_FLAG) {
			return null;
		} else {
			throw new DatabaseException("Corrupted flag value: " + flagvalue);
		}
	}

}
