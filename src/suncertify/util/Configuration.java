package suncertify.util;

import static suncertify.util.Constants.CONFIGURATION_FILE_NAME;

import java.io.File;
import java.io.IOException;

public class Configuration {

	private static final File configFile = new File(CONFIGURATION_FILE_NAME);

	private static Configuration INSTANCE = new Configuration();

	private Configuration() {
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createFileIfNecessary() {

	}

	public void saveServerDBLocation() {

	}

	public void saveStandaloneDBLocation() {

	}

	public void saveServerIPAddress() {

	}

	public void savePortNumber() {

	}
}
