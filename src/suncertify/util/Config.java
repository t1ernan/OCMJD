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

public class Config {

	private static final File configFile = new File(CONFIGURATION_FILE_NAME);
	private static final Config INSTANCE = new Config();

	private String serverDBLocation;
	private String standaloneDBLocation;
	private String serverIPAddress;
	private int portNumber;

	private Config() {
		try {
			createFileIfNecessary();
			readConfigValues();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFileIfNecessary() throws IOException {
		if (!configFile.exists()) {
			configFile.createNewFile();
			writeDefaultConfigValues();
		}
	}

	private void readConfigValues() throws IOException {
		try (BufferedReader configFileReader = new BufferedReader(new FileReader(configFile));) {
			setServerDBLocation(getConfigValue(configFileReader.readLine()));
			setStandaloneDBLocation(getConfigValue(configFileReader.readLine()));
			setServerIPAddress(getConfigValue(configFileReader.readLine()));
			setPortNumber(Integer.parseInt(getConfigValue(configFileReader.readLine())));
		}

	}

	private String getConfigValue(String line) throws IOException {
		return line.split(EQUALS)[1];
	}

	private void writeDefaultConfigValues() throws FileNotFoundException {
		try (PrintWriter configFileWriter = new PrintWriter(configFile);) {
			configFileWriter.println(DB_LOCATION_SERVER + EQUALS + DEFAULT_DB_LOCATION_SERVER);
			configFileWriter.println(DB_LOCATION_STANDALONE + EQUALS + DEFAULT_DB_LOCATION_STANDALONE);
			configFileWriter.println(SERVER_IPADDRESS + EQUALS + DEFAULT_SERVER_IPADDRESS);
			configFileWriter.println(PORT_NUMBER + EQUALS + DEFAULT_PORT_NUMBER);
		}
	}

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

	public String getServerDBLocation() {
		return serverDBLocation;
	}

	public void setServerDBLocation(String serverDBLocation) {
		this.serverDBLocation = serverDBLocation;
	}

	public String getStandaloneDBLocation() {
		return standaloneDBLocation;
	}

	public void setStandaloneDBLocation(String standaloneDBLocation) {
		this.standaloneDBLocation = standaloneDBLocation;
	}

	public String getServerIPAddress() {
		return serverIPAddress;
	}

	public void setServerIPAddress(String serverIPAddress) {
		this.serverIPAddress = serverIPAddress;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public static Config getInstance() {
		return INSTANCE;
	}
}
