package test.db;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import suncertify.db.DBFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.util.Config;

public class DBFactoryTest {

	@Test
	public void testGetDatabase_CorrectFile() throws DatabaseException {
		final DBMainExtended data = DBFactory.getDatabase(Config.getServerDBLocation());
		assertTrue(data instanceof Data);
	}

	@Test(expected=DatabaseException.class)
	public void testGetDatabase_NoFile() throws DatabaseException {
		DBFactory.getDatabase("");
	}

	@Test(expected=DatabaseException.class)
	public void testGetDatabase_WrongFile() throws DatabaseException {
		DBFactory.getDatabase("D:\\gitrepos\\ocmp\\sample.txt");
	}

}
