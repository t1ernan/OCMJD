package suncertify.util;

import static suncertify.util.Constants.EMPTY_STRING;
import static suncertify.util.Utils.isInvalidPortNumber;

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
public final class Config {

	/** The Constant CONFIGURATION_FILE_NAME. */
	private static final String PROPERTIES_FILE = "suncertify.properties";

	/** The Constant DB_LOCATION_SERVER. */
	private static final String SERVER_DB = "server.databaseLocation";

	/** The Constant SERVER_IPADDRESS. */
	private static final String SERVER_IP = "server.ipaddress";

	/** The Constant PORT_NUMBER. */
	private static final String SERVER_PORT = "server.port";

	/** The Constant DB_LOCATION_STANDALONE. */
	private static final String LOCAL_DB = "alone.databaseLocation";

	/** The Constant PORT_NUMBER. */
	private static final String CLIENT_PORT = "client.serverPort";

	/** The Constant DEFAULT_PORT_NUMBER. */
	private static final String RMI_PORT = "1099";

	private static final Properties PROP = new Properties();

	static {
		final File configFile = new File(PROPERTIES_FILE);
		if (configFile.exists()) {
			loadPropertiesFile();
		}else{
			createNewPropertiesFile();
		}
	}

	private Config(){

	}

	public static String getAloneDBLocation() {
		return PROP.getProperty(LOCAL_DB);
	}

	public static String getClientPortNumber() {
		return PROP.getProperty(CLIENT_PORT);
	}

	public static String getServerDBLocation() {
		return PROP.getProperty(SERVER_DB);
	}

	public static String getServerIPAddress() {
		return PROP.getProperty(SERVER_IP);
	}

	public static String getServerPortNumber() {
		return PROP.getProperty(SERVER_PORT);
	}

	public static void saveProperties() {
		try (OutputStream output = new FileOutputStream(PROPERTIES_FILE);) {
			PROP.store(output, null);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void setAloneDBLocation(final String aloneDBLocation) {
		if(aloneDBLocation.isEmpty()){
			throw new IllegalArgumentException("Database Location field must not be left blank");
		}
		PROP.setProperty(LOCAL_DB, aloneDBLocation);
	}

	public static void setClientPortNumber(final String clientPortNumber) {
		if(isInvalidPortNumber(clientPortNumber)){
			throw new IllegalArgumentException("Port number must contain only digits");
		}
		PROP.setProperty(CLIENT_PORT, clientPortNumber);
	}

	public static void setServerDBLocation(final String serverDBLocation) {
		if(serverDBLocation.isEmpty()){
			throw new IllegalArgumentException("Database Location field must not be left blank");
		}
		PROP.setProperty(SERVER_DB, serverDBLocation);
	}

	public static void setServerIPAddress(final String serverIPAddress) {
		if(serverIPAddress.isEmpty()){
			throw new IllegalArgumentException("IP address field must not be left blank");
		}
		PROP.setProperty(SERVER_IP, serverIPAddress);
	}

	public static void setServerPortNumber(final String serverPortNumber) {
		if(isInvalidPortNumber(serverPortNumber)){
			throw new IllegalArgumentException("Port number must contain only digits");
		}
		PROP.setProperty(SERVER_PORT, serverPortNumber);
	}

	private static void createNewPropertiesFile() {
		PROP.setProperty(SERVER_DB, EMPTY_STRING);
		PROP.setProperty(SERVER_IP, EMPTY_STRING);
		PROP.setProperty(SERVER_PORT, RMI_PORT);
		PROP.setProperty(CLIENT_PORT, RMI_PORT);
		PROP.setProperty(LOCAL_DB, EMPTY_STRING);
		saveProperties();
	}


	private static void loadPropertiesFile() {
		try (InputStream input = new FileInputStream(PROPERTIES_FILE);) {
			PROP.load(input);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}