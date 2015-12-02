package suncertify.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DBFileReader {

	private static final int INTEGER_SIZE = 4;
	private static final int MAGIC_COOKIE_SIZE = 4;
	private static final int RECORD_ZERO_OFFSET_SIZE = 4;
	private static final int NUMBER_OF_FIELDS_SIZE = 2;

	private static final int FIELD_NAME_SIZE = 2;
	private static final int FIELD_LENGTH_SIZE = 2;

	private static int fieldNameLength;

	private static final int RECORD_FLAG_SIZE = 2;
	private static final int VALID_RECORD_FLAG = 00;
	private static final int INVALID_RECORD_FLAG = 0x8000;

	private static final String CHARACTER_ENCODING = "US-ASCII";
	private static final String DATABASE_FILE_LOCATION = "db-2x2.db";

	public static void main(final String[] args) {

		try (final InputStream in = new BufferedInputStream(new FileInputStream(DATABASE_FILE_LOCATION))) {
			final int magicCookie = getValue(in, MAGIC_COOKIE_SIZE);
			final int recordZeroOffset = getValue(in, RECORD_ZERO_OFFSET_SIZE);
			final int numberOfFields = getValue(in, NUMBER_OF_FIELDS_SIZE);

			for (int index = 0; index < numberOfFields; index++) {

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int getValue(InputStream in, int size) throws IOException {
		if (size == INTEGER_SIZE) {
			return getInt(in, size);
		} else {
			return getShort(in, size);
		}
	}

	private static int getShort(InputStream in, int size) throws IOException {
		return getByteBuffer(in, size).getShort();
	}

	private static int getInt(InputStream in, int size) throws IOException {
		return getByteBuffer(in, size).getInt();
	}

	private static ByteBuffer getByteBuffer(InputStream in, int size) throws IOException {
		final byte[] byteArray = new byte[size];
		in.read(byteArray);
		return ByteBuffer.wrap(byteArray);
	}
}
