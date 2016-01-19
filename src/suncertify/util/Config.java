/*
 * Config.java  1.0  19-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class Config is responsible for reading and and storing the application's configuration
 * properties. The first time the application is run it will generate the file
 * "suncertify.properties" with default property values; these values can be edited by the user
 * through the GUI. Each subsequent run will read the contents of the properties file so that the
 * settings last specified by the user will be available without needing to re-enter them.
 */
public final class Config {

  /**
   * The name of the properties file where all the user specified configuration properties are
   * stored between runs.
   */
  private static final String PROPERTIES_FILE = "suncertify.properties";

  /** The key in the properties file for the server's database location. */
  private static final String SERVER_DB = "server.databaseLocation";

  /** The key in the properties file for the server's ip address which the client connects to. */
  private static final String SERVER_IP = "server.ipaddress";

  /** The key in the properties file for the port number the server will run on. */
  private static final String SERVER_PORT = "server.port";

  /**
   * The key in the properties file for the client's database location when run in standalone mode.
   */
  private static final String LOCAL_DB = "alone.databaseLocation";

  /** The key in the properties file for the port number the client connects to. */
  private static final String CLIENT_PORT = "client.serverPort";

  /** The default RMI port number. */
  private static final String RMI_PORT = "1099";

  /** The properties list which stores all the configuration values specified by the user. */
  private static final Properties PROPERTIES = new Properties();

  /** The global logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  static {
    initializeProperties();
  }

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private Config() {

  }

  /**
   * Gets the standalone client's database file location.
   *
   * @return the standalone client's database file location.
   */
  public static String getAloneDbLocation() {
    return PROPERTIES.getProperty(LOCAL_DB);
  }

  /**
   * Gets the client port number.
   *
   * @return the client port number
   */
  public static String getClientPortNumber() {
    return PROPERTIES.getProperty(CLIENT_PORT);
  }

  /**
   * Gets the server's database file location.
   *
   * @return the server's database file location
   */
  public static String getServerDbLocation() {
    return PROPERTIES.getProperty(SERVER_DB);
  }

  /**
   * Gets the server ip address.
   *
   * @return the server ip address
   */
  public static String getServerIpAddress() {
    return PROPERTIES.getProperty(SERVER_IP);
  }

  /**
   * Gets the server port number.
   *
   * @return the server port number
   */
  public static String getServerPortNumber() {
    return PROPERTIES.getProperty(SERVER_PORT);
  }

  /**
   * Writes the current properties list contained in the properties object {@code PROPERTIES} to the
   * suncertify.properties file.
   */
  public static void saveProperties() {
    try (OutputStream output = new FileOutputStream(PROPERTIES_FILE);) {
      PROPERTIES.store(output, null);
    } catch (final IOException exception) {
      LOGGER.log(Level.WARNING, "A problem occurred when attempting to save properties to file. "
          + "Properties may not have been intialized correctly.", exception);
    }
  }

  /**
   * Sets the port number of the server that the client will connect to with the {@code portNumber}.
   * Throws {@link IllegalArgumentException} if the port number contains any non-numeric characters
   * or is left blank.
   *
   * @param portNumber
   *          the new port number that the client will connect to the server on.
   * @throws IllegalArgumentException
   *           if the port number contains any non-numeric characters or is left blank.
   */
  public static void setClientPortNumber(final String portNumber) throws IllegalArgumentException {
    if (isInvalidPortNumber(portNumber)) {
      throw new IllegalArgumentException(
          "Port number cannot contain any non-numeric characters or be left blank.");
    }
    PROPERTIES.setProperty(CLIENT_PORT, portNumber);
  }

  /**
   * Sets the location of the database file on the server with the specified {@code dbFileLocation}.
   * Throws {@link IllegalArgumentException} if the database's file location is left blank.
   *
   * @param dbFileLocation
   *          the new database file location.
   * @throws IllegalArgumentException
   *           if database file location is left blank.
   */
  public static void setServerDbFileLocation(final String dbFileLocation)
      throws IllegalArgumentException {
    if (dbFileLocation.isEmpty()) {
      throw new IllegalArgumentException("Database file location field cannot be left blank.");
    }
    PROPERTIES.setProperty(SERVER_DB, dbFileLocation);
  }

  /**
   * Sets the ip address of the server that the client will connect to with the specified
   * {@code serverIpAddress}. Throws {@link IllegalArgumentException} if the server ip address is
   * left blank.
   *
   * @param serverIpAddress
   *          the new server ip address
   * @throws IllegalArgumentException
   *           if the server ip address is left blank.
   */
  public static void setServerIpAddress(final String serverIpAddress)
      throws IllegalArgumentException {
    if (serverIpAddress.isEmpty()) {
      throw new IllegalArgumentException("IP address field cannot be left blank.");
    }
    PROPERTIES.setProperty(SERVER_IP, serverIpAddress);
  }

  /**
   * Sets the port number the server will run on with the specified {@code portNumber}. Throws
   * {@link IllegalArgumentException} if the port number contains any non-numeric characters or is
   * left blank.
   *
   * @param portNumber
   *          the new port number the server will run on.
   * @throws IllegalArgumentException
   *           if the port number contains any non-numeric characters or is left blank.
   */
  public static void setServerPortNumber(final String portNumber) throws IllegalArgumentException {
    if (isInvalidPortNumber(portNumber)) {
      throw new IllegalArgumentException(
          "Port number cannot contain any non-numeric characters or be left blank.");
    }
    PROPERTIES.setProperty(SERVER_PORT, portNumber);
  }

  /**
   * Sets the location of the database file for the non-networked client with the specified
   * {@code dbFileLocation}. Throws {@link IllegalArgumentException} if the database's file location
   * is left blank.
   *
   * @param dbFileLocation
   *          the new database file location.
   * @throws IllegalArgumentException
   *           if database file location is left blank.
   */
  public static void setStandaloneDbLocation(final String dbFileLocation)
      throws IllegalArgumentException {
    if (dbFileLocation.isEmpty()) {
      throw new IllegalArgumentException("Database file location field cannot be left blank.");
    }
    PROPERTIES.setProperty(LOCAL_DB, dbFileLocation);
  }

  /**
   * Initializes {@code PROPERTIES} with default property values and write these properties to the
   * suncertify.properties file, creating it in the process.
   */
  private static void createNewPropertiesFile() {
    PROPERTIES.setProperty(SERVER_DB, EMPTY_STRING);
    PROPERTIES.setProperty(SERVER_IP, EMPTY_STRING);
    PROPERTIES.setProperty(SERVER_PORT, RMI_PORT);
    PROPERTIES.setProperty(CLIENT_PORT, RMI_PORT);
    PROPERTIES.setProperty(LOCAL_DB, EMPTY_STRING);
    saveProperties();
  }

  /**
   * Initializes {@code PROPERTIES} with the properties read from the suncertify.properties file. If
   * the file doesn't exist, it will initialize {@code PROPERTIES} with default property values and
   * write these properties to the suncertify.properties file, creating it in the process.
   */
  private static void initializeProperties() {
    final File configFile = new File(PROPERTIES_FILE);
    if (configFile.exists()) {
      loadPropertiesFile();
    } else {
      createNewPropertiesFile();
    }
  }

  /**
   * Loads the suncertify.properties file into {@code PROPERTIES}.
   */
  private static void loadPropertiesFile() {
    try (InputStream input = new FileInputStream(PROPERTIES_FILE);) {
      PROPERTIES.load(input);
    } catch (final IOException exception) {
      LOGGER.log(Level.WARNING,
          "A problem occurred when attempting to read properties file. Properties may not have been intialized correctly.",
          exception);
    }
  }
}