package suncertify.util;

public class Constants {

	public static final String DB_FILE_NAME = "db-2x2.db";
	public static final String RMI_ID = "Remote Server";
	public static final String EMPTY_SPACE = "";
	public static final String EQUALS = "=";
	public static final String CONFIGURATION_WINDOW_TITLE = "System Configuration Options";
	public static final String CONFIGURATION_PANEL_TITLE = "Configuration Details";
	public static final String MAIN_WINDOW_TITLE = "Bodgitt and Scarper Booking System";
	public static final int MAIN_WINDOW_WIDTH = 750;
	public static final int MAIN_WINDOW_LENGTH = 300;
	public static final int CONFIGURATION_PANEL_WIDTH = 500;
	public static final int CONFIGURATION_PANEL_LENGTH = 430;
	public static final int BUTTON_WIDTH = 50;
	public static final int BUTTON_LENGTH = 20;

	public static final String CONFIGURATION_FILE_NAME = "suncertify.properties";

	public static final String DB_LOCATION_SERVER = "Server_Database_Location";
	public static final String DB_LOCATION_STANDALONE = "Standalone_Database_Location";
	public static final String SERVER_IPADDRESS = "Server_IP_Address";
	public static final String PORT_NUMBER = "Port_Number";

	public static final String DEFAULT_DB_LOCATION_SERVER = System.getProperty("user.dir") + "\\db-2x2.db";
	public static final String DEFAULT_DB_LOCATION_STANDALONE = System.getProperty("user.dir") + "\\db-2x2.db";
	public static final String DEFAULT_SERVER_IPADDRESS = "localhost";
	public static final int DEFAULT_PORT_NUMBER = 222;
	public static final String UNDERSCORE = "_";
	public static final int VALID_FLAG = 00;
	public static final int DELETED_FLAG = 0x8000;
	public static final int EXPECTED_MAGIC_COOKIE = 514;
	public static final String CHARACTER_ENCODING = "US-ASCII";
	public static final int BLANK_SPACE_HEX = 0x20;
}
