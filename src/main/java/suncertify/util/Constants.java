/*
 * Constants.java  1.0  19-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.util;

/**
 * Defines some constant values which are common to multiple parts of the application.
 */
public final class Constants {

  /** The constant representing an empty string. */
  public static final String EMPTY_STRING = "";

  /** The default registry identifying name used when setting up an RMI server-client connection. */
  public static final String RMI_ID = "Remote Server";

  /** The constant RECORD_FIELDS specifies the number of fields in the database schema. */
  public static final int RECORD_FIELDS = 6;

  /**
   * The constant SEARCH_KEY_FIELDS specifies the number of fields used for searching operations.
   */
  public static final int SEARCH_KEY_FIELDS = 2;

  /** The default size for JTextfields in the GUI. */
  public static final int DEFAULT_TEXTFIELD_SIZE = 20;

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private Constants() {

  }
}
