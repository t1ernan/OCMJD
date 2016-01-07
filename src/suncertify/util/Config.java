package suncertify.util;

import static suncertify.util.Constants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * The Class Config.
 */
public abstract class Config {
	
	/** The Constant CONFIGURATION_FILE_NAME. */
	public static final String CONFIGURATION_FILE_NAME = "suncertify.properties";

	/** The Constant DB_LOCATION_SERVER. */
	public static final String SERVER_DB_LOCATION = "server.databaseLocation";
	
	/** The Constant SERVER_IPADDRESS. */
	public static final String SERVER_IP_ADDRESS = "server.ipaddress";
	
	/** The Constant PORT_NUMBER. */
	public static final String SERVER_PORT_NUMBER = "server.port";
	
	/** The Constant DB_LOCATION_STANDALONE. */
	public static final String ALONE_DB_LOCATION = "alone.databaseLocation";
	
	/** The Constant PORT_NUMBER. */
	public static final String CLIENT_PORT_NUMBER = "client.serverPort";
	
	/** The Constant DEFAULT_PORT_NUMBER. */
	public static final String DEFAULT_PORT_NUMBER = "1099";
	
	private static final Properties prop = new Properties();

	static {
		final File configFile = new File(CONFIGURATION_FILE_NAME);
		if (!configFile.exists()) {
			createNewPropertiesFile();
		}else{
			loadPropertiesFile();
		}
	}

	private static void createNewPropertiesFile() {
		prop.setProperty(SERVER_DB_LOCATION, EMPTY_STRING);
		prop.setProperty(SERVER_IP_ADDRESS, EMPTY_STRING);
		prop.setProperty(SERVER_PORT_NUMBER, DEFAULT_PORT_NUMBER);
		prop.setProperty(CLIENT_PORT_NUMBER, DEFAULT_PORT_NUMBER);
		prop.setProperty(ALONE_DB_LOCATION, EMPTY_STRING);
		saveProperties();
	}

	private static void loadPropertiesFile() {
		try (InputStream input = new FileInputStream(CONFIGURATION_FILE_NAME);) {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveProperties() {
		try (OutputStream output = new FileOutputStream(CONFIGURATION_FILE_NAME);) {
			prop.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getServerDBLocation() {
		return prop.getProperty(SERVER_DB_LOCATION);
	}

	public static String getServerIPAddress() {
		return prop.getProperty(SERVER_IP_ADDRESS);
	}

	public static String getServerPortNumber() {
		return prop.getProperty(SERVER_PORT_NUMBER);
	}

	public static String getClientPortNumber() {
		return prop.getProperty(CLIENT_PORT_NUMBER);
	}

	public static String getAloneDBAddress() {
		return prop.getProperty(ALONE_DB_LOCATION);
	}

	public static void saveServerDBLocation(final String serverDBLocation) {
		prop.setProperty(SERVER_DB_LOCATION, serverDBLocation);
		saveProperties();
	}

	public static void saveServerIPAddress(final String serverIPAddress) {
		prop.setProperty(SERVER_IP_ADDRESS, serverIPAddress);
		saveProperties();
	}

	public static void saveServerPortNumber(final String serverPortNumber) {
		prop.setProperty(SERVER_PORT_NUMBER, serverPortNumber);
		saveProperties();
	}

	public static void saveClientPortNumber(final String clientPortNumber) {
		prop.setProperty(CLIENT_PORT_NUMBER, clientPortNumber);
		saveProperties();
	}

	public static void saveAloneDBLocation(final String aloneDBLocation) {
		prop.setProperty(ALONE_DB_LOCATION, aloneDBLocation);
		saveProperties();
	}

}
