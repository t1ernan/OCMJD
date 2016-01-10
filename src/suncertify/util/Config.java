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
public final class Config {
	
	/** The Constant CONFIGURATION_FILE_NAME. */
	private static final String CONFIGURATION_FILE_NAME = "suncertify.properties";

	/** The Constant DB_LOCATION_SERVER. */
	private static final String SERVER_DB_LOCATION = "server.databaseLocation";
	
	/** The Constant SERVER_IPADDRESS. */
	private static final String SERVER_IP_ADDRESS = "server.ipaddress";
	
	/** The Constant PORT_NUMBER. */
	private static final String SERVER_PORT_NUMBER = "server.port";
	
	/** The Constant DB_LOCATION_STANDALONE. */
	private static final String ALONE_DB_LOCATION = "alone.databaseLocation";
	
	/** The Constant PORT_NUMBER. */
	private static final String CLIENT_PORT_NUMBER = "client.serverPort";
	
	/** The Constant DEFAULT_PORT_NUMBER. */
	private static final String DEFAULT_PORT_NUMBER = "1099";
	
	private static final Properties prop = new Properties();

	static {
		final File configFile = new File(CONFIGURATION_FILE_NAME);
		if (!configFile.exists()) {
			createNewPropertiesFile();
		}else{
			loadPropertiesFile();
		}
	}
	
	private Config(){
		
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

	public static void saveProperties() {
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

	public static String getAloneDBLocation() {
		return prop.getProperty(ALONE_DB_LOCATION);
	}

	public static void setServerDBLocation(final String serverDBLocation) {
		if(serverDBLocation.isEmpty()){
			throw new IllegalArgumentException("Database Location field must not be left blank");
		}
		prop.setProperty(SERVER_DB_LOCATION, serverDBLocation);
	}

	public static void setServerIPAddress(final String serverIPAddress) {
		if(serverIPAddress.isEmpty()){
			throw new IllegalArgumentException("IP address field must not be left blank");
		}
		prop.setProperty(SERVER_IP_ADDRESS, serverIPAddress);
	}

	public static void setServerPortNumber(final String serverPortNumber) {
		if(isInvalidPortNumber(serverPortNumber)){
			throw new IllegalArgumentException("Port number must contain only digits");
		}
		prop.setProperty(SERVER_PORT_NUMBER, serverPortNumber);
	}

	public static void setClientPortNumber(final String clientPortNumber) {
		if(isInvalidPortNumber(clientPortNumber)){
			throw new IllegalArgumentException("Port number must contain only digits");
		}
		prop.setProperty(CLIENT_PORT_NUMBER, clientPortNumber);
	}

	public static void setAloneDBLocation(final String aloneDBLocation) {
		if(aloneDBLocation.isEmpty()){
			throw new IllegalArgumentException("Database Location field must not be left blank");
		}
		prop.setProperty(ALONE_DB_LOCATION, aloneDBLocation);
	}

	private static boolean isInvalidPortNumber(final String serverPortNumber) {
		return !serverPortNumber.matches("[0-9]+");
	}
}