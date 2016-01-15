package suncertify.util;

import static suncertify.util.Constants.DEBUG_LOGGER;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Converter.
 */
public final class Utils {

  private static final Logger log = Logger.getLogger(DEBUG_LOGGER);

  /** The Constant CHARACTER_ENCODING. */
  private static final String ENCODING = "US-ASCII";

  private static final int BLANK_SPACE_HEX = 0x20;

  private Utils() {

  }

  /**
   * Copies the specified {@code unpaddedBytes} into a new byte[] of length equal to the specified
   * {@code size} filled with blank space ASCII characters. Returns the new byte[].
   *
   * @param unpaddedBytes
   *          the bytes to copy into the byte[] filled with blank space ASCII characters.
   * @param size
   *          the size of the byte[] filled with blank space ASCII characters.
   * @return the new byte[]
   */
  public static byte[] addPadding(final byte[] unpaddedBytes, final int size) {
    final byte[] paddedBytes = new byte[size];
    Arrays.fill(paddedBytes, (byte) BLANK_SPACE_HEX);
    System.arraycopy(unpaddedBytes, 0, paddedBytes, 0, unpaddedBytes.length);
    return paddedBytes;
  }

  public static String convertBytesToString(final byte[] valueBytes) throws IOException {
    return new String(valueBytes, Charset.forName(ENCODING));
  }

  public static byte[] convertStringToBytes(final String message) {
    return message.getBytes(Charset.forName(ENCODING));
  }

  public static boolean isEightDigits(final String number) {
    return number.matches("[0-9]{8}");
  }
  
  public static boolean isInvalidPortNumber(final String serverPortNumber) {
    return !serverPortNumber.matches("[0-9]+");
  }

  /**
   * Reads the specified number of bytes from the specified {@code dbFile} and returns them as a
   * byte[].
   *
   * @param dbFile
   *          a {@link RandomAccessFile} file for reading and writing to the database file.
   * @param numberOfBytes
   *          the number of bytes which will be read from the file.
   * @return the byte[]
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static byte[] readBytes(final RandomAccessFile dbFile, final int numberOfBytes)
      throws IOException {
    final byte[] valueBytes = new byte[numberOfBytes];
    dbFile.read(valueBytes);
    return valueBytes;
  }

  /**
   * Reads the specified number of bytes from the specified {@code dbFile}, converts them into a
   * String.
   *
   * @param dbFile
   *          a {@link RandomAccessFile} file for reading and writing to the database file.
   * @param numberOfBytes
   *          the number of bytes which will be read from the file and converted into a String
   * @return the string representation of the bytes read
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static String readString(final RandomAccessFile dbFile, final int numberOfBytes)
      throws IOException {
    final byte[] valueBytes = readBytes(dbFile, numberOfBytes);
    final String paddedFieldValue = convertBytesToString(valueBytes);
    return paddedFieldValue.trim();
  }

  /**
   * Converts the specified {@code fieldValue} String into a byte[] and writes it to the specified
   * {@code dbFile}. If the length of the byte[] is less than maximum number of bytes the field
   * should take up in the database, the remaining bytes are filled with blank spaces.
   *
   * @param dbFile
   *          a {@link RandomAccessFile} file for reading and writing to the database file.
   * @param fieldValue
   *          the field value
   * @param fieldSize
   *          the maximum number of bytes the field should take up in the database
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static void writeString(final RandomAccessFile dbFile, final String fieldValue,
      final int fieldSize) throws IOException {
    final byte[] unpaddedBytes = convertStringToBytes(fieldValue);
    final byte[] paddedBytes = addPadding(unpaddedBytes, fieldSize);
    dbFile.write(paddedBytes);
  }

  public static void intializeLogger(Level level) {
    log.setLevel(level);
    log.setUseParentHandlers(false);
    final ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(level);
    log.addHandler(handler);
  }

  public static void log(Level level, String message) {
    log.log(level, message);

  }
}
