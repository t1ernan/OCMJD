package test.util;

import static org.junit.Assert.assertTrue;
import static suncertify.util.Constants.CONFIGURATION_FILE_NAME;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import suncertify.util.Config;

public class ConfigurationTest {

	private static final File configFile = new File(CONFIGURATION_FILE_NAME);

	@BeforeClass
	public static void setUp() {
		if (configFile.exists()) {
			configFile.delete();
		}
	}

	@Test
	public void test_DefaultFile() {
		final Config config = Config.getInstance();
		assertTrue(configFile.exists());
	}

	@Test
	public void test_CustomFile() {
		final Config config = Config.getInstance();
		config.setPortNumber(111);
		config.saveConfigValues();
		assertTrue(configFile.exists());
	}

}
