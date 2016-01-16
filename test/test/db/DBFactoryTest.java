package test.db;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import suncertify.db.DatabaseFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.Data;
import suncertify.db.DatabaseAccessException;

public class DBFactoryTest {

  @Test
  public void testGetDatabase_CorrectFile() throws DatabaseAccessException {
    final DBMainExtended data = DatabaseFactory
        .getDatabase(System.getProperty("user.dir") + "\\db-2x2.db");
    assertTrue(data instanceof Data);
  }

  @Test(expected = DatabaseAccessException.class)
  public void testGetDatabase_NoFile() throws DatabaseAccessException {
    DatabaseFactory.getDatabase("");
  }

  @Test(expected = DatabaseAccessException.class)
  public void testGetDatabase_WrongFile() throws DatabaseAccessException {
    DatabaseFactory.getDatabase(System.getProperty("user.dir") + "\\sample.txt");
  }

}
