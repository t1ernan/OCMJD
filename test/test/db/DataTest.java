package test.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import suncertify.db.Data;
import suncertify.db.DatabaseException;

public class DataTest {

	@Test
	public void test() throws DatabaseException, IOException {
		Data data = new Data("db-2x2.db");
		data.printCache();
		data.saveData();
	}
	
	@Test
	public void test2() throws DatabaseException, IOException {


	}

}
