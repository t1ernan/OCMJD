/*
 * Utils.java  1.0  19-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utils contains a collection of useful helper methods that are common to different parts of the
 * application or have very reusable logic, such as methods for converting data types to another
 * type.
 */
public final class Utils {

  /** The global logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The charset encoding used in the application. */
  private static final String ENCODING = "US-ASCII";

  /** The blank space ASCII character represented as hexadecimal. */
  private static final int BLANK_SPACE_HEX = 0x20;

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private Utils() {

  }

  /**
   * Copies the specified {@code unpaddedBytes} into a new byte[] of length equal to the specified
   * {@code size} filled with blank space ASCII characters. Returns the new padded byte[].
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

  /**
   * Converts the specified {@code value} byte array into a string using {@value #ENCODING}
   * encoding.
   *
   * @param value
   *          the bytes to be converted into a string.
   * @return the string
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static String convertBytesToString(final byte[] value) throws IOException {
    return new String(value, Charset.forName(ENCODING));
  }

  /**
   * Converts the specified {@code message} string to a byte array using {@value #ENCODING}
   * encoding.
   *
   * @param message
   *          the message to be converted to bytes.
   * @return the byte[] containing the message in byte form.
   */
  public static byte[] convertStringToBytes(final String message) {
    return message.getBytes(Charset.forName(ENCODING));
  }

  /**
   * Initializes the global logger to the specified {@code level}. Also configures the logger to
   * output logs to System.err which is displayed in the console.
   *
   * @param level
   *          the level of the messages that will be logged.
   */
  public static void initializeLogger(final Level level) {
    LOGGER.setLevel(level);
    LOGGER.setUseParentHandlers(false);
    final ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(level);
    LOGGER.addHandler(handler);
  }

  /**
   * Checks if the specified {@code number} is exactly eight digits. Returns true if the specified
   * {@code number} is exactly eight digits
   *
   * @param number
   *          the number to check.
   * @return true, if {@code number} is exactly eight digits.
   */
  public static boolean isEightDigits(final String number) {
    return number.matches("[0-9]{8}");
  }

  /**
   * Checks if the specified {@code number} contains non-numeric characters. Returns true if the
   * specified {@code number} is empty or contains any non-numeric characters.
   *
   * @param number
   *          the number to check.
   * @return true, if the specified {@code number} is empty or contains any non-numeric characters.
   */
  public static boolean isNonNumeric(final String number) {
    return !number.matches("[0-9]+");
  }

  /**
   * Reads the specified number of bytes from the specified {@code raf} and returns them as a
   * byte[].
   *
   * @param raf
   *          a {@link RandomAccessFile} object for reading and writing to a file.
   * @param numberOfBytes
   *          the number of bytes which will be read from the file.
   * @return a byte[] containing the bytes read.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static byte[] readBytes(final RandomAccessFile raf, final int numberOfBytes)
      throws IOException {
    final byte[] valueBytes = new byte[numberOfBytes];
    raf.read(valueBytes);
    return valueBytes;
  }

  /**
   * Reads the specified number of bytes from the specified {@code raf}, converts them into a
   * String.
   *
   * @param raf
   *          a {@link RandomAccessFile} object for reading and writing to a file.
   * @param numberOfBytes
   *          the number of bytes which will be read from the file and converted into a String.
   * @return the string representation of the bytes read.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static String readString(final RandomAccessFile raf, final int numberOfBytes)
      throws IOException {
    final byte[] valueBytes = readBytes(raf, numberOfBytes);
    final String paddedFieldValue = convertBytesToString(valueBytes);
    return paddedFieldValue.trim();
  }

  /**
   * Converts the specified {@code fieldValue} String into a byte[] and uses the specified
   * {@code raf} to write the value to its file. If the length of the byte[] is less than the
   * specified {@code fieldSize}, the remaining bytes are filled with blank spaces.
   *
   * @param raf
   *          a {@link RandomAccessFile} object for reading and writing to a file.
   * @param fieldValue
   *          the field value
   * @param fieldSize
   *          the maximum number of bytes the field should take up in the database.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static void writeString(final RandomAccessFile raf, final String fieldValue,
      final int fieldSize) throws IOException {
    final byte[] unpaddedBytes = convertStringToBytes(fieldValue);
    final byte[] paddedBytes = addPadding(unpaddedBytes, fieldSize);
    raf.write(paddedBytes);
  }
}
