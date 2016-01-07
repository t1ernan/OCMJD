package suncertify.util;

import java.io.File;

/**
 * The Class Constants.
 */
public class Constants {

	/** The Constant VALID_FLAG. */
	public static final int VALID_FLAG = 00;

	/** The Constant DELETED_FLAG. */
	public static final int DELETED_FLAG = 0x8000;

	/** The Constant EXPECTED_MAGIC_COOKIE. */
	public static final int EXPECTED_MAGIC_COOKIE = 514;

	/** The Constant BLANK_SPACE_HEX. */
	public static final int BLANK_SPACE_HEX = 0x20;

	/** The Constant CHARACTER_ENCODING. */
	public static final String CHARACTER_ENCODING = "US-ASCII";

	/** The Constant EMPTY_SPACE. */
	public static final String EMPTY_STRING = "";

	/** The Constant EQUALS. */
	public static final String EQUALS = "=";

	/** The Constant UNDERSCORE. */
	public static final String UNDERSCORE = "_";

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

	/** The Constant DB_FILE_NAME. */
	public static final String DB_FILE_NAME = "db-2x2.db";

	/** The Constant RMI_ID. */
	public static final String RMI_ID = "Remote Server";
	
	/** The Constant DEFAULT_PORT_NUMBER. */
	public static final String DEFAULT_PORT_NUMBER = "1099";

	/** The Constant DEFAULT_DB_LOCATION_SERVER. */
	public static final String DEFAULT_DB_LOCATION_SERVER = System.getProperty("user.dir") + File.separator
			+ DB_FILE_NAME;

	/** The Constant DEFAULT_DB_LOCATION_STANDALONE. */
	public static final String DEFAULT_DB_LOCATION_STANDALONE = System.getProperty("user.dir") + File.separator
			+ DB_FILE_NAME;

	/** The Constant DEFAULT_SERVER_IPADDRESS. */
	public static final String DEFAULT_SERVER_IPADDRESS = "localhost";

}
