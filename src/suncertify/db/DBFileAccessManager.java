package suncertify.db;

import static suncertify.util.Constants.EMPTY_STRING;
import static suncertify.util.Utils.convertBytesToString;
import static suncertify.util.Utils.convertStringToBytes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;

/**
 * The class DBFileAccessManager is responsible for reading and writing to the
 * non-relational database file and implements {@link DBAccessManager}.
 */
public class DBFileAccessManager implements DBAccessManager {

	/** The Constant VALID_FLAG. */
	public static final int VALID_FLAG = 00;

	/** The Constant DELETED_FLAG. */
	public static final int DELETED_FLAG = 0x8000;

	/** The Constant BLANK_SPACE_HEX. */
	public static final int BLANK_SPACE_HEX = 0x20;

	/** The Constant EXPECTED_MAGIC_COOKIE. */
	public static final int EXPECTED_MAGIC_COOKIE = 514;

	/** The location of the database on the file system. */
	private String dbFileLocation = null;

	/** The number of fields in a record. */
	private int numberOfFields;

	/** The number of bytes from the start of the file to the first record. */
	private int recordOffset;
	/**
	 * An array containing the number of bytes each record field takes up in the
	 * database.
	 */
	private int[] fieldValueMaxSizes;

	/** An array containing the names of fields in each record. */
	private String[] fieldNames;

	private final static DBFileAccessManager INSTANCE = new DBFileAccessManager();

	/**
	 * Constructs a new default DBFileAccessManager.
	 */
	private DBFileAccessManager() {

	}

	public static DBFileAccessManager getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(final String dbFileLocation) throws DatabaseException {
		this.dbFileLocation = dbFileLocation;
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			checkMagicCookie(dbFile);
			recordOffset = dbFile.readInt();
			numberOfFields = dbFile.readShort();
			fieldNames = new String[numberOfFields];
			fieldValueMaxSizes = new int[numberOfFields];
			readSchemaDescription(dbFile);
			checkRecordOffset(dbFile, recordOffset);
		} catch (IOException e) {
			throw new DatabaseException("Could not read data from the specified file: " + dbFileLocation + ". " + e);
		}
	}

	/**
	 * Reads the data section of the database file and loads its records into
	 * the specified map.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values
	 * @throws DatabaseException
	 *             if an unexpected IO exception occurs when trying to read the
	 *             file
	 */
	@Override
	public void readDatabaseIntoCache(final Map<Integer, String[]> map) throws DatabaseException {
		int recordNumber = 0;
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			dbFile.seek(recordOffset);
			while (dbFile.getFilePointer() < dbFile.length()) {
				final String[] fieldValues = new String[numberOfFields];
				final int flagvalue = dbFile.readUnsignedShort();
				readRecord(dbFile, fieldValues);
				if (flagvalue == VALID_FLAG) {
					map.put(recordNumber, fieldValues);
				} else if (flagvalue == DELETED_FLAG) {
					map.put(recordNumber, null);
				} else {
					throw new DatabaseException(
							"Corrupted database. Flag value does not conform to schema: " + flagvalue);
				}
				recordNumber++;
			}
		} catch (IOException e) {
			throw new DatabaseException("Could not read data from the specified file: " + dbFileLocation + ". " + e);
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
	@Override
	public void persist(final Map<Integer, String[]> map) throws DatabaseException {
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			dbFile.seek(recordOffset);
			dbFile.setLength(recordOffset);
			for (String[] fieldValues : map.values()) {
				if (fieldValues == null) {
					dbFile.writeShort(DELETED_FLAG);
					writeNullRecord(dbFile);
				} else {
					dbFile.writeShort(VALID_FLAG);
					writeRecord(dbFile, fieldValues);
				}
			}
		} catch (IOException e) {
			throw new DatabaseException("Could not save data: " + e);
		}
	}

	/**
	 * Compares the size of the each element in the specified
	 * {@code fieldValues} against the max permitted field size described in the
	 * schema description section of the database file for that field. If the
	 * number of characters used in a field exceeds the max number of characters
	 * permitted for that field, a {@link IllegalArgumentException} will be
	 * thrown.
	 *
	 * @param fieldValues
	 *            a string array where each element is a record value.
	 * @throws IllegalArgumentException
	 *             if the number of characters used in a field exceeds the max
	 *             number of characters permitted for that field
	 */
	@Override
	public void validateFieldsAgainstSchema(final String[] fieldValues) {
		for (int index = 0; index < fieldValues.length; index++) {
			final int fieldSize = fieldValues[index].length();
			final int maxFieldSize = fieldValueMaxSizes[index];
			if (fieldSize > maxFieldSize) {
				throw new IllegalArgumentException("'" + fieldValues[index]
						+ "' exceeds the maximum number of characters permitted for the field '" + fieldNames[index]
						+ ". Max permitted characters: " + maxFieldSize);
			}
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
	 *            a string array where each element is a record value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readRecord(final RandomAccessFile dbFile, final String[] fieldValues) throws IOException {
		for (int index = 0; index < numberOfFields; index++) {
			final String fieldValue = readString(dbFile, fieldValueMaxSizes[index]);
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
			fieldValueMaxSizes[index] = fieldValueSize;
		}
	}

	/**
	 * Writes a null record to the specified {@code dbFile}. The record will be
	 * filled with blank space ASCII characters.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeNullRecord(final RandomAccessFile dbFile) throws IOException {
		for (int index = 0; index < numberOfFields; index++) {
			writeString(dbFile, EMPTY_STRING, fieldValueMaxSizes[index]);
		}
	}

	/**
	 * Writes a record to the specified {@code dbFile}. The record will be have
	 * the field values specified in the elements of the {@code fieldValues}
	 * string array.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param fieldValues
	 *            a string array where each element is a record value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeRecord(final RandomAccessFile dbFile, final String[] fieldValues) throws IOException {
		for (int index = 0; index < numberOfFields; index++) {
			writeString(dbFile, fieldValues[index], fieldValueMaxSizes[index]);
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
			throw new DatabaseException(
					"Invalid Database File. Record Offset value does not match value specifiied in schema.");
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
			throw new DatabaseException(
					"Invalid Database File. Magic Cookie value doesn't match expected value: " + magicCookie);
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
		Arrays.fill(paddedBytes, (byte) BLANK_SPACE_HEX);
		System.arraycopy(unpaddedBytes, 0, paddedBytes, 0, unpaddedBytes.length);
		return paddedBytes;
	}

}
