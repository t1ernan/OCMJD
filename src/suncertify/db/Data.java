package suncertify.db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Data implements DBMainExtended {

	private static final int VALID_FLAG = 00;
	private static final int DELETED_FLAG = 0x8000;
	private static final int EXPECTED_MAGIC_COOKIE = 514;
	private static final Map<Long, String[]> cache = new HashMap<>();

	private static final String CHARACTER_ENCODING = "US-ASCII";
	private static String databasePath;

	public Data(String databasePath) throws DatabaseException {
		super();
		this.databasePath = databasePath;
		readData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void update(int recNo, String[] data) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void delete(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized int[] find(String[] criteria) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void saveData() throws DatabaseException {

		try (RandomAccessFile dbFile = new RandomAccessFile(databasePath, "rwd")) {
			final int magicCookie = dbFile.readInt();
			if (magicCookie != EXPECTED_MAGIC_COOKIE) {
				throw new DatabaseException("Invalid datafile: Invalid Magic Cookie");
			}
			final int recordOffset = dbFile.readInt();
			final int numberOfFields = dbFile.readShort();
			dbFile.seek(recordOffset);
			dbFile.setLength(recordOffset);

			for (Entry<Long, String[]> entry : cache.entrySet()) {
				final Long key = entry.getKey();
				final String[] value = entry.getValue();
				if (value == null) {
					dbFile.writeShort(DELETED_FLAG);
				} else {
					dbFile.writeShort(VALID_FLAG);
				}
				for (int index = 0; index < numberOfFields; index++) {
					dbFile.write(value[index].getBytes(Charset.forName(CHARACTER_ENCODING)));
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void readData() throws DatabaseException {
		long recordNumber = 0;
		try (RandomAccessFile dbFile = new RandomAccessFile(databasePath, "rwd")) {
			final int magicCookie = dbFile.readInt();
			if (magicCookie != EXPECTED_MAGIC_COOKIE) {
				throw new DatabaseException("Invalid datafile: Invalid Magic Cookie");
			}
			final int recordOffset = dbFile.readInt();
			final int numberOfFields = dbFile.readShort();
			final int[] fieldValueSizes = new int[numberOfFields];
			final String[] fieldValues = new String[numberOfFields];
			final String[] fieldNames = new String[numberOfFields];

			for (int index = 0; index < numberOfFields; index++) {
				final int fieldNameSize = dbFile.readShort();
				final String fieldName = readString(dbFile, fieldNameSize);
				final int fieldValueSize = dbFile.readShort();
				fieldNames[index] = fieldName;
				fieldValueSizes[index] = fieldValueSize;
			}

			if (recordOffset != dbFile.getFilePointer()) {
				throw new DatabaseException("Invalid datafile: Invalid Record Offset");
			}

			while (dbFile.getFilePointer() < dbFile.length()) {
				final int flagvalue = dbFile.readShort();
				for (int index = 0; index < numberOfFields; index++) {
					final String fieldValue = readString(dbFile, fieldValueSizes[index]);
					fieldValues[index] = fieldValue;
				}
				cache.put(recordNumber, fieldValues);
				recordNumber++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String readString(RandomAccessFile database, int fieldLength) throws IOException {
		byte[] messageBytes = new byte[fieldLength];
		database.read(messageBytes);
		return new String(messageBytes, Charset.forName(CHARACTER_ENCODING));
	}
}
