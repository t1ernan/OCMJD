package test.util;

import java.io.File;

public class Constants {

	/** The Constant DB_FILE_NAME. */
	public static final String DB_FILE_NAME = "db-2x2.db";

	/** The Constant DEFAULT_DB_LOCATION_SERVER. */
	public static final String DEFAULT_DB_LOCATION_SERVER = System.getProperty("user.dir") + File.separator
			+ DB_FILE_NAME;

	/** The Constant DEFAULT_DB_LOCATION_STANDALONE. */
	public static final String DEFAULT_DB_LOCATION_STANDALONE = System.getProperty("user.dir") + File.separator
			+ DB_FILE_NAME;

	/** The Constant DEFAULT_SERVER_IPADDRESS. */
	public static final String DEFAULT_SERVER_IPADDRESS = "localhost";

	/** The Constant DEFAULT_PORT_NUMBER. */
	public static final int DEFAULT_PORT_NUMBER = 1099;
}
