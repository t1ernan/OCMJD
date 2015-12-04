package suncertify.db;

import java.io.IOException;

import org.junit.Test;

public class DataTest {

	@Test
	public void test() throws DatabaseException, IOException {
		Data data = new Data("db-2x2.db");
		data.printCache();
		data.saveData();
	}

}
