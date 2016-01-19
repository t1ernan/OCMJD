/*
 * Data.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.db;

import static suncertify.util.Constants.EMPTY_STRING;
import static suncertify.util.Constants.RECORD_FIELDS;
import static suncertify.util.Utils.readString;
import static suncertify.util.Utils.writeString;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Data is an implementation of the {@link DBMainExtended} which acts as a DAO for a non-relation
 * database file. The database schema information for the expected database file is hard-coded as
 * constants in this class.
 */
public final class Data implements DBMainExtended {

  /** The single instance of Data. */
  private static final Data INSTANCE = new Data();

  /** The names of the record fields, specified in schema information. */
  private static final String[] FIELD_NAMES = { "name", "location", "specialties", "size", "rate",
      "owner" };

  /** The size of the record fields in bytes, specified in schema information. */
  private static final int[] MAX_FIELD_SIZES = { 32, 64, 64, 6, 8, 8 };

  /** The 2 byte value which denotes a valid record, specified in schema information. */
  private static final int VALID_FLAG = 00;

  /** The 2 byte value which denotes a deleted record, specified in schema information. */
  private static final int DELETED_FLAG = 0x8000;

  /** The global logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The record offset, specified in the schema information. */
  private static final int RECORD_OFFSET = 70;

  /**
   * The in-memory cache which stores record numbers and fields values of the corresponding record.
   */
  private final Map<Integer, String[]> recordCache = new HashMap<>();

  /** The set which stores the record numbers of any currently locked records. */
  private final Set<Integer> lockedRecords = new HashSet<>();

  /** The file path of the database file. */
  private String dbFilePath;

  /**
   * Constructs a new Data instance.
   */
  private Data() {
  }

  /**
   * Gets the single instance of Data.
   *
   * @return single instance of Data.
   */
  public static Data getInstance() {
    return INSTANCE;
  }

  /**
   * Compares the primary keys, first two elements, of the two string arrays arguments. Returns true
   * if respective elements are equal.
   *
   * @param existingData
   *          the field values of a record already stored in the database.
   * @param newData
   *          the field values of a new record not already stored in the database.
   * @return true, if primary keys values are equal, ignoring case considerations; false otherwise.
   */
  private static boolean comparePrimaryKeys(final String[] existingData, final String[] newData) {
    final String existingKey = existingData[0] + existingData[1];
    final String newKey = newData[0] + newData[1];
    return existingKey.equals(newKey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized int create(final String[] data)
      throws DuplicateKeyException, IllegalArgumentException {
    validateFields(data);
    checkForDuplicateKey(data);
    final int recordNumber = recordCache.entrySet().stream()
                             .filter(entry -> entry.getValue() == null)
                             .mapToInt(entry -> entry.getKey())
                             .findAny()
                             .orElse(recordCache.size());
    recordCache.put(recordNumber, data);
    return recordNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void delete(final int recNo) {
    recordCache.put(recNo, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized int[] find(final String[] criteria)
      throws RecordNotFoundException, IllegalArgumentException {
    validateFields(criteria);
    final int[] recordNumbers = recordCache.entrySet().stream()
                                .filter(entry -> entry.getValue() != null)
                                .filter(entry -> doFieldsMatchCriteria(entry.getValue(), criteria))
                                .mapToInt(entry -> entry.getKey())
                                .toArray();
    if (recordNumbers.length == 0) {
      throw new RecordNotFoundException(
          "No matching records for selected criteria: " + Arrays.toString(criteria) + ".");
    }
    return recordNumbers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void initialize(final String dbFilePath)
      throws DatabaseAccessException, IllegalArgumentException {
    if (dbFilePath == null) {
      throw new IllegalArgumentException("File path of database file cannot be null.");
    }
    this.dbFilePath = dbFilePath;
    try (RandomAccessFile raf = new RandomAccessFile(dbFilePath, "rwd")) {
      loadCache();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          saveRecords();
        } catch (final IOException e) {
          LOGGER.severe("Could not save data: " + e.getMessage());
        }
      }));
    } catch (final IOException e) {
      throw new DatabaseAccessException(
          "Could not read data from the specified file: " + dbFilePath, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized boolean isLocked(final int recNo) throws RecordNotFoundException {
    if (isInvalidRecord(recNo)) {
      throw new RecordNotFoundException("Record " + recNo + " is not a valid record.");
    }
    return lockedRecords.contains(recNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void lock(final int recNo) throws RecordNotFoundException {
    while (isLocked(recNo)) {
      try {
        wait();
      } catch (final InterruptedException e) {
        LOGGER.warning(e.getMessage());
      }
    }
    lockedRecords.add(recNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized String[] read(final int recNo) throws RecordNotFoundException {
    if (isInvalidRecord(recNo)) {
      throw new RecordNotFoundException("Record " + recNo + " is not a valid record.");
    }
    return recordCache.get(recNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void saveRecords() throws IOException, IllegalStateException {
    if (dbFilePath == null) {
      throw new IllegalStateException(
          "The saveRecords method cannot be invoked before " + this.getClass().getSimpleName()
              + " has been initialized through invoking the initialize method.");
    }
    try (RandomAccessFile raf = new RandomAccessFile(dbFilePath, "rwd")) {
      raf.seek(RECORD_OFFSET);
      raf.setLength(RECORD_OFFSET);
      for (final String[] fieldValues : recordCache.values()) {
        writeRecord(raf, fieldValues);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void unlock(final int recNo) {
    if (lockedRecords.contains(recNo)) {
      lockedRecords.remove(recNo);
      notifyAll();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void update(final int recNo, final String[] data) {
    validateFields(data);
    recordCache.put(recNo, data);
  }

  /**
   * Reads a single record from the database file using a {@link RandomAccessFile} and updates the
   * cache. If the record has been marked as valid, it will store the record in the cache with the
   * specified {@code recordNumber} as the key. Otherwise, it will store {@code null} in the cache
   * for the specified {@code recordNumber}.
   *
   * @param raf
   *          the random access file used to read the database file.
   * @param recordNumber
   *          the record number, used as the record cache key.
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void addRecordToCache(final RandomAccessFile raf, final int recordNumber)
      throws IOException {
    final int flagvalue = raf.readUnsignedShort();
    final String[] fieldValues = new String[RECORD_FIELDS];
    for (int index = 0; index < RECORD_FIELDS; index++) {
      final String fieldValue = readString(raf, MAX_FIELD_SIZES[index]);
      fieldValues[index] = fieldValue;
    }
    if (flagvalue == VALID_FLAG) {
      recordCache.put(recordNumber, fieldValues);
    } else {
      recordCache.put(recordNumber, null);
    }

  }

  /**
   * Check if the primary key of the specified {@code data} already exists in the database. If the
   * key already exists, it will throw a {@link DuplicateKeyException}.
   *
   * @param data
   *          a string array where each element is a record value.
   * @throws DuplicateKeyException
   *           if the primary key of the specified {@code data} already exists in the database.
   */
  private void checkForDuplicateKey(final String[] data) throws DuplicateKeyException {
    final boolean isDuplicate = recordCache.entrySet().stream().map(entry -> entry.getValue())
        .filter(values -> values != null).anyMatch(values -> comparePrimaryKeys(values, data));
    if (isDuplicate) {
      throw new DuplicateKeyException("Record with: [" + FIELD_NAMES[0] + "=" + data[0] + ","
          + FIELD_NAMES[1] + "=" + data[1] + "] already exists.");
    }
  }

  /**
   * Checks if each element of the specified {@code fieldValues} begins with the corresponding
   * element of the specified {@code searchValues}. This is a case insensitive search. Returns true
   * if each element of {@code fieldValues} begins with the corresponding element of
   * {@code searchValues}.
   *
   *
   * @param fieldValues
   *          a string array where each element is a record value
   * @param searchValues
   *          a string array where each element is a search value
   * @return true, if each element of {@code fieldValues} begins with the corresponding element of
   *         {@code searchValues} ignoring case considerations.
   */
  private boolean doFieldsMatchCriteria(final String[] fieldValues, final String[] searchValues) {
    boolean isMatch = true;
    for (int index = 0; index < searchValues.length; index++) {
      final String fieldValue = fieldValues[index].toUpperCase();
      final String searchValue = searchValues[index].toUpperCase();
      if (!fieldValue.startsWith(searchValue)) {
        isMatch = false;
        break;
      }
    }
    return isMatch;
  }

  /**
   * Checks if the record with the specified {@code recNo} is valid. Returns true if the record is
   * not stored in the database or has been marked as deleted. Otherwise, returns false.
   *
   * @param recNo
   *          the record number.
   * @return true, if the record is not stored in the database or has been marked as deleted.
   */
  private boolean isInvalidRecord(final int recNo) {
    return recordCache.entrySet().stream()
           .filter(entry -> entry.getValue() != null)
           .noneMatch(entry -> entry.getKey() == recNo);
  }

  /**
   * Reads the record data from the database file using a {@link RandomAccessFile} object and loads
   * the records into an in-memory cache, implemented as a {@link HashMap}.
   *
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void loadCache() throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(dbFilePath, "rwd")) {
      int recordNumber = 0;
      raf.seek(RECORD_OFFSET);
      while (raf.getFilePointer() < raf.length()) {
        addRecordToCache(raf, recordNumber);
        recordNumber++;
      }
    }
  }

  /**
   * Compares the size of the each element in the specified {@code fieldValues} against the max
   * permitted field size described in the schema description section of the database file for that
   * field. If the number of characters used in a field exceeds the max number of characters
   * permitted for that field, a {@link IllegalArgumentException} will be thrown.
   *
   * @param fieldValues
   *          a string array where each element is a record value.
   * @throws IllegalArgumentException
   *           if the {@code fieldValues} is null, number of elements exceeds record fields or
   *           number of characters used in a field exceeds the max number of characters permitted
   *           for that field
   */
  private void validateFields(final String[] fieldValues) {
    if (fieldValues == null) {
      throw new IllegalArgumentException("Record values cannot be null");
    }
    if (fieldValues.length > RECORD_FIELDS) {
      throw new IllegalArgumentException("Number of elements exceed number of record fields");
    }

    for (int index = 0; index < fieldValues.length; index++) {
      final int fieldSize = fieldValues[index].length();
      final int maxFieldSize = MAX_FIELD_SIZES[index];
      if (fieldSize > maxFieldSize) {
        throw new IllegalArgumentException("'" + fieldValues[index]
            + "' exceeds the maximum number of characters permitted for the field '"
            + FIELD_NAMES[index] + ". Max permitted characters: " + maxFieldSize);
      }
    }
  }

  /**
   * Writes a record to the database using the specified {@link RandomAccessFile},where each element
   * of the specified {@code fieldValues} is a record value. If the specified {@code fieldValues}
   * argument is {@code null}, the record will be filled with blank space ASCII characters.
   *
   * <p>If any value of a field is less that the size specified for that field in the schema
   * information, the remaining bytes of the field will be padded with blank space ASCII characters.
   *
   * @param raf
   *          a {@link RandomAccessFile} file used for writing the records to the database file.
   * @param fieldValues
   *          a string array where each element is a record value
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void writeRecord(final RandomAccessFile raf, final String[] fieldValues)
      throws IOException {
    if (fieldValues == null) {
      raf.writeShort(DELETED_FLAG);
      for (int index = 0; index < RECORD_FIELDS; index++) {
        writeString(raf, EMPTY_STRING, MAX_FIELD_SIZES[index]);
      }
    } else {
      raf.writeShort(VALID_FLAG);
      for (int index = 0; index < RECORD_FIELDS; index++) {
        writeString(raf, fieldValues[index], MAX_FIELD_SIZES[index]);
      }
    }
  }
}
