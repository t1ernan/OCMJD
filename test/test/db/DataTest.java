package test.db;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static suncertify.util.Constants.DB_FILE_PATH;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import suncertify.db.DAOFactory;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

public class DataTest {

	private Data data;

	private final String[] oldValues = new String[] { "Dogs With Tools", "Smallville", "Roofing", "7", "$35.00", "" };
	private final String[] newValues = new String[] { "Smack my Itch up", "Gotham", "Getting It Done,Horsing It", "12",
			"$79.00", "87654321" };

	private final String[] searchCriteria_ValidName_ValidLocation = new String[] { "Dogs With Tools", "Smallville" };
	private final String[] searchCriteria_ValidName_InvalidLocation = new String[] { "Dogs With Tools", "Gotham" };
	private final String[] searchCriteria_ValidName_NoLocation = new String[] { "Dogs With Tools", "" };
	private final String[] searchCriteria_NoName_ValidLocation = new String[] { "", "Smallville" };
	private final String[] searchCriteria_NoName_InvalidLocation = new String[] { "", "Gotham" };
	private final String[] searchCriteria_NoName_NoLocation = new String[] { "", "" };
	private final String[] searchCriteria_InvalidName_ValidLocation = new String[] { "Smack my Itch up", "Smallville" };
	private final String[] searchCriteria_InvalidName_InvalidLocation = new String[] { "Smack my Itch up", "Gotham" };
	private final String[] searchCriteria_InvalidName_NoLocation = new String[] { "Smack my Itch up", "" };

	private final int VALID_RECORD_NUMBER = 0;
	private final int INVALID_RECORD_NUMBER = 50;

	@Before
	public void setup() throws DatabaseException {
		data = DAOFactory.getDbManager(DB_FILE_PATH);
	}

	@After
	public void teardown() throws DatabaseException {
		data.clear();
		data.load();
	}

	@Test
	public void testLoadCache() {
		assertEquals(28, data.getTotalNumberOfRecords());
	}

	@Test
	public void testSaveData() throws DatabaseException {
		data.save();
		assertEquals(28, data.getTotalNumberOfRecords());
	}

	@Test
	public void testRead_ValidRecord() throws DatabaseException {
		final String[] fieldValues = data.read(VALID_RECORD_NUMBER);
		assertArrayEquals(oldValues, fieldValues);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testRead_InvalidRecord() throws DatabaseException {
		data.read(INVALID_RECORD_NUMBER);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testRead_DeletedRecord() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.read(VALID_RECORD_NUMBER);
	}

	@Test
	public void testUpdate_ValidRecord() throws DatabaseException {
		data.update(VALID_RECORD_NUMBER, newValues);
		final String[] fieldValues = data.read(VALID_RECORD_NUMBER);
		assertArrayEquals(newValues, fieldValues);
		assertEquals(28, data.getTotalNumberOfRecords());
	}

	@Test(expected = RecordNotFoundException.class)
	public void testUpdate_InvalidRecord() throws DatabaseException {
		data.update(INVALID_RECORD_NUMBER, newValues);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testUpdate_DeletedRecord() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.update(VALID_RECORD_NUMBER, newValues);
	}

	@Test
	public void testDelete_ValidRecord() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(new Integer(VALID_RECORD_NUMBER), data.getRecordNumber());
	}

	@Test(expected = RecordNotFoundException.class)
	public void testDelete_InvalidRecord() throws DatabaseException {
		data.delete(INVALID_RECORD_NUMBER);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testDelete_DeletedRecord() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.delete(VALID_RECORD_NUMBER);
	}

	@Test
	public void testCreate_NewKey_NoDeletedRecordsInCache() throws DatabaseException {
		final int newRecordNumber = data.getTotalNumberOfRecords();
		assertEquals(28, newRecordNumber);
		assertEquals(28, data.create(newValues));
		assertEquals(29, data.getTotalNumberOfRecords());
		assertArrayEquals(newValues, data.read(newRecordNumber));
	}

	@Test(expected = DuplicateKeyException.class)
	public void testCreate_ExistingKey_NoDeletedRecordsInCache() throws DatabaseException {
		data.create(oldValues);
	}

	@Test
	public void testCreate_NewKey_DeletedRecordsInCache() throws DatabaseException {
		data.delete(4);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(4, data.create(newValues));
		assertEquals(28, data.getTotalNumberOfRecords());
		assertArrayEquals(newValues, data.read(4));
	}

	@Test(expected = DuplicateKeyException.class)
	public void testCreate_ExistingKey_DeletedRecordsInCache() throws DatabaseException {
		data.delete(4);
		data.create(oldValues);
	}

	@Test
	public void testCreate_DeletedKey_DeletedRecordsInCache() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		assertEquals(28, data.getTotalNumberOfRecords());
		assertEquals(VALID_RECORD_NUMBER, data.create(oldValues));
		assertEquals(28, data.getTotalNumberOfRecords());
		assertArrayEquals(oldValues, data.read(VALID_RECORD_NUMBER));
	}

	@Test
	public void testFind_ValidName_ValidLocation() throws DatabaseException {
		final int[] matchingRecords = data.find(searchCriteria_ValidName_ValidLocation);
		assertEquals(1, matchingRecords.length);
		assertEquals(0, matchingRecords[0]);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_ValidName_ValidLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.find(searchCriteria_ValidName_ValidLocation);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_ValidName_InvalidLocation() throws DatabaseException {
		data.find(searchCriteria_ValidName_InvalidLocation);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_ValidName_InvalidLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.find(searchCriteria_ValidName_InvalidLocation);
	}

	@Test
	public void testFind_ValidName_NoLocation() throws DatabaseException {
		final int[] matchingRecords = data.find(searchCriteria_ValidName_NoLocation);
		assertEquals(6, matchingRecords.length);
		assertEquals(0, matchingRecords[0]);
	}

	@Test
	public void testFind_ValidName_NoLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		final int[] matchingRecords = data.find(searchCriteria_ValidName_NoLocation);
		assertEquals(5, matchingRecords.length);
	}

	@Test
	public void testFind_NoName_ValidLocation() throws DatabaseException {
		final int[] matchingRecords = data.find(searchCriteria_NoName_ValidLocation);
		assertEquals(2, matchingRecords.length);
		assertEquals(0, matchingRecords[0]);
	}

	@Test
	public void testFind_NoName_ValidLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		final int[] matchingRecords = data.find(searchCriteria_NoName_ValidLocation);
		assertEquals(1, matchingRecords.length);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_NoName_InvalidLocation() throws DatabaseException {
		data.find(searchCriteria_NoName_InvalidLocation);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_NoName_InvalidLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.find(searchCriteria_NoName_InvalidLocation);
	}

	@Test
	public void testFind_NoName_NoLocation() throws DatabaseException {
		final int[] matchingRecords = data.find(searchCriteria_NoName_NoLocation);
		assertEquals(28, matchingRecords.length);
		assertEquals(0, matchingRecords[0]);
	}

	@Test
	public void testFind_NoName_NoLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		final int[] matchingRecords = data.find(searchCriteria_NoName_NoLocation);
		assertEquals(27, matchingRecords.length);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_InvalidName_ValidLocation() throws DatabaseException {
		data.find(searchCriteria_InvalidName_ValidLocation);

	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_InvalidName_ValidLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.find(searchCriteria_InvalidName_ValidLocation);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_InvalidName_InvalidLocation() throws DatabaseException {
		data.find(searchCriteria_InvalidName_InvalidLocation);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_InvalidName_InvalidLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.find(searchCriteria_InvalidName_InvalidLocation);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_InvalidName_NoLocation() throws DatabaseException {
		data.find(searchCriteria_InvalidName_NoLocation);

	}

	@Test(expected = RecordNotFoundException.class)
	public void testFind_InvalidName_NoLocation_Deleted() throws DatabaseException {
		data.delete(VALID_RECORD_NUMBER);
		data.find(searchCriteria_InvalidName_NoLocation);
	}

}
