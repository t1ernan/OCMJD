package suncertify.util;

import static suncertify.util.Constants.CONFIGURATION_FILE_NAME;
import static suncertify.util.Constants.DB_LOCATION_SERVER;
import static suncertify.util.Constants.DB_LOCATION_STANDALONE;
import static suncertify.util.Constants.DEFAULT_DB_LOCATION_SERVER;
import static suncertify.util.Constants.DEFAULT_DB_LOCATION_STANDALONE;
import static suncertify.util.Constants.DEFAULT_PORT_NUMBER;
import static suncertify.util.Constants.DEFAULT_SERVER_IPADDRESS;
import static suncertify.util.Constants.EQUALS;
import static suncertify.util.Constants.PORT_NUMBER;
import static suncertify.util.Constants.SERVER_IPADDRESS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */
public class Config {

	/** The Constant configFile. */
	private static final File configFile = new File(CONFIGURATION_FILE_NAME);

	/** The Constant INSTANCE. */
	private static final Config INSTANCE = new Config();

	/** The server db location. */
	private String serverDBLocation;

	/** The standalone db location. */
	private String standaloneDBLocation;

	/** The server ip address. */
	private String serverIPAddress;

	/** The port number. */
	private int portNumber;

	/**
	 * Instantiates a new config.
	 */
	private Config() {
		try {
			createFileIfNecessary();
			readConfigValues();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the file if necessary.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void createFileIfNecessary() throws IOException {
		if (!configFile.exists()) {
			configFile.createNewFile();
			writeDefaultConfigValues();
		}
	}

	/**
	 * Read config values.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readConfigValues() throws IOException {
		try (BufferedReader configFileReader = new BufferedReader(new FileReader(configFile));) {
			setServerDBLocation(getConfigValue(configFileReader.readLine()));
			setStandaloneDBLocation(getConfigValue(configFileReader.readLine()));
			setServerIPAddress(getConfigValue(configFileReader.readLine()));
			setPortNumber(Integer.parseInt(getConfigValue(configFileReader.readLine())));
		}
	}

	/**
	 * Gets the config value.
	 *
	 * @param line
	 *            the line
	 * @return the config value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String getConfigValue(String line) throws IOException {
		return line.split(EQUALS)[1];
	}

	/**
	 * Write default config values.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private void writeDefaultConfigValues() throws FileNotFoundException {
		try (PrintWriter configFileWriter = new PrintWriter(configFile);) {
			configFileWriter.println(DB_LOCATION_SERVER + EQUALS + DEFAULT_DB_LOCATION_SERVER);
			configFileWriter.println(DB_LOCATION_STANDALONE + EQUALS + DEFAULT_DB_LOCATION_STANDALONE);
			configFileWriter.println(SERVER_IPADDRESS + EQUALS + DEFAULT_SERVER_IPADDRESS);
			configFileWriter.println(PORT_NUMBER + EQUALS + DEFAULT_PORT_NUMBER);
		}
	}

	/**
	 * Save config values.
	 */
	public void saveConfigValues() {
		try (PrintWriter configFileWriter = new PrintWriter(configFile);) {
			configFileWriter.println(DB_LOCATION_SERVER + EQUALS + serverDBLocation);
			configFileWriter.println(DB_LOCATION_STANDALONE + EQUALS + standaloneDBLocation);
			configFileWriter.println(SERVER_IPADDRESS + EQUALS + serverIPAddress);
			configFileWriter.println(PORT_NUMBER + EQUALS + portNumber);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the server db location.
	 *
	 * @return the server db location
	 */
	public String getServerDBLocation() {
		return serverDBLocation;
	}

	/**
	 * Sets the server db location.
	 *
	 * @param serverDBLocation
	 *            the new server db location
	 */
	public void setServerDBLocation(String serverDBLocation) {
		this.serverDBLocation = serverDBLocation;
	}

	/**
	 * Gets the standalone db location.
	 *
	 * @return the standalone db location
	 */
	public String getStandaloneDBLocation() {
		return standaloneDBLocation;
	}

	/**
	 * Sets the standalone db location.
	 *
	 * @param standaloneDBLocation
	 *            the new standalone db location
	 */
	public void setStandaloneDBLocation(String standaloneDBLocation) {
		this.standaloneDBLocation = standaloneDBLocation;
	}

	/**
	 * Gets the server ip address.
	 *
	 * @return the server ip address
	 */
	public String getServerIPAddress() {
		return serverIPAddress;
	}

	/**
	 * Sets the server ip address.
	 *
	 * @param serverIPAddress
	 *            the new server ip address
	 */
	public void setServerIPAddress(String serverIPAddress) {
		this.serverIPAddress = serverIPAddress;
	}

	/**
	 * Gets the port number.
	 *
	 * @return the port number
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * Sets the port number.
	 *
	 * @param portNumber
	 *            the new port number
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * Gets the single instance of Config.
	 *
	 * @return single instance of Config
	 */
	public static Config getInstance() {
		return INSTANCE;
	}
}
