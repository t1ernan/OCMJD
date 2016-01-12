package test.db;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import suncertify.db.DatabaseFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseException;

public class DBFactoryTest {

  @Test
  public void testGetDatabase_CorrectFile() throws DatabaseException {
    final DBMainExtended data = DatabaseFactory
        .getDatabase(System.getProperty("user.dir") + "\\db-2x2.db");
    assertTrue(data instanceof Data);
  }

  @Test(expected = DatabaseException.class)
  public void testGetDatabase_NoFile() throws DatabaseException {
    DatabaseFactory.getDatabase("");
  }

  @Test(expected = DatabaseException.class)
  public void testGetDatabase_WrongFile() throws DatabaseException {
    DatabaseFactory.getDatabase(System.getProperty("user.dir") + "\\sample.txt");
  }

}
