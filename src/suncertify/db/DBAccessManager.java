package suncertify.db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

public class DBAccessManager {

	private final int VALID_FLAG = 00;
	private final int DELETED_FLAG = 0x8000;
	private final int EXPECTED_MAGIC_COOKIE = 514;
	private final String CHARACTER_ENCODING = "US-ASCII";
	private final String databasePath;
	private int[] fieldValueSizes;

	public DBAccessManager(String databasePath) {
		this.databasePath = databasePath;
	}

	public void read(final Map<Integer, String[]> map) throws DatabaseException {
		try (RandomAccessFile dbFile = new RandomAccessFile(databasePath, "rwd")) {
			checkMagicCookie(dbFile);
			final int recordOffset = dbFile.readInt();
			final int numberOfFields = dbFile.readShort();
			final String[] fieldNames = new String[numberOfFields];
			fieldValueSizes = new int[numberOfFields];
			for (int index = 0; index < numberOfFields; index++) {
				final int fieldNameSize = dbFile.readShort();
				final String fieldName = readString(dbFile, fieldNameSize);
				final int fieldValueSize = dbFile.readShort();
				fieldNames[index] = fieldName;
				fieldValueSizes[index] = fieldValueSize;
			}
			checkRecordOffset(dbFile, recordOffset);
			int recordNumber = 0;
			while (dbFile.getFilePointer() < dbFile.length()) {
				final String[] fieldValues = new String[numberOfFields];
				final int flagvalue = dbFile.readUnsignedShort();
				for (int index = 0; index < numberOfFields; index++) {
					final String fieldValue = readString(dbFile, fieldValueSizes[index]);
					fieldValues[index] = fieldValue;
				}
				addRecordToCache(map, recordNumber, fieldValues, flagvalue);
				recordNumber++;
			}
		} catch (IOException e) {
			throw new DatabaseException("Could not read data: " + e);
		}
	}

	public void persist(final Map<Integer, String[]> map) throws DatabaseException {
		try (RandomAccessFile dbFile = new RandomAccessFile(databasePath, "rwd")) {
			checkMagicCookie(dbFile);
			final int recordOffset = dbFile.readInt();
			final int numberOfFields = dbFile.readShort();
			dbFile.seek(recordOffset);
			dbFile.setLength(recordOffset);
			for (String[] fieldValues : map.values()) {
				dbFile.writeShort(getFlag(fieldValues));
				for (int index = 0; index < numberOfFields; index++) {
					writeString(dbFile, fieldValues[index], fieldValueSizes[index]);
				}
			}
		} catch (IOException e) {
			throw new DatabaseException("Could not save data: " + e);
		}
	}

	private void addRecordToCache(final Map<Integer, String[]> map, int recordNumber, final String[] fieldValues,
			final int flagvalue) throws DatabaseException {
		if (isFlagValid(flagvalue)) {
			map.put(recordNumber, fieldValues);
		} else if (isFlagInvalid(flagvalue)) {
			map.put(recordNumber, null);
		} else {
			throw new DatabaseException("Corrupted flag value: " + flagvalue);
		}
	}

	private String readString(RandomAccessFile dbFile, int numberOfBytes) throws IOException {
		final byte[] valueBytes = readBytes(dbFile, numberOfBytes);
		final String paddedFieldValue = convertBytesToString(valueBytes);
		final String trimmedFieldValue = removePadding(paddedFieldValue);
		return trimmedFieldValue;
	}

	private void writeString(RandomAccessFile dbFile, String fieldValue, int numberOfBytes) throws IOException {
		final byte[] unpaddedBytes = convertStringToBytes(fieldValue);
		final byte[] paddedBytes = addPadding(unpaddedBytes, numberOfBytes);
		dbFile.write(paddedBytes);
	}

	private byte[] readBytes(RandomAccessFile dbFile, int numberOfBytes) throws IOException {
		final byte[] valueBytes = new byte[numberOfBytes];
		dbFile.read(valueBytes);
		return valueBytes;
	}

	private void checkRecordOffset(RandomAccessFile dbFile, final int recordOffset)
			throws IOException, DatabaseException {
		if (recordOffset != dbFile.getFilePointer()) {
			throw new DatabaseException("Invalid datafile: Invalid Record Offset");
		}
	}

	private boolean isFlagValid(final int flagvalue) {
		return flagvalue == VALID_FLAG;
	}

	private boolean isFlagInvalid(final int flagvalue) {
		return flagvalue == DELETED_FLAG;
	}

	private String convertBytesToString(byte[] valueBytes) throws IOException {
		return new String(valueBytes, Charset.forName(CHARACTER_ENCODING));
	}

	private byte[] convertStringToBytes(String message) {
		return message.getBytes(Charset.forName(CHARACTER_ENCODING));

	}

	private byte[] addPadding(byte[] unpaddedBytes, int size) {
		final byte[] paddedBytes = new byte[size];
		Arrays.fill(paddedBytes, (byte) 0x20);
		System.arraycopy(unpaddedBytes, 0, paddedBytes, 0, unpaddedBytes.length);
		return paddedBytes;
	}

	private String removePadding(String message) {
		return message.trim();
	}

	private void checkMagicCookie(RandomAccessFile dbFile) throws IOException, DatabaseException {
		final int magicCookie = dbFile.readInt();
		if (magicCookie != EXPECTED_MAGIC_COOKIE) {
			throw new DatabaseException("Invalid datafile: Invalid Magic Cookie");
		}
	}

	private int getFlag(final String[] value) throws IOException {
		return (value == null) ? DELETED_FLAG : VALID_FLAG;
	}
}
