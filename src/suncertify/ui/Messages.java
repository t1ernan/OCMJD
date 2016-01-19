/*
 * Messages.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

/**
 * The class Messages defines all the titles, labels, tooltips, button texts, and any other messages
 * displayed to the user through the GUI. This allows for common messages to displayed across
 * multiple UI windows without duplicating strings by importing them statically where needed.
 */
public final class Messages {

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private Messages() {
  }

  /** The title displayed in the border of the Actions JPanel. */
  public static final String ACTIONS_BORDER_TITLE = "Actions";

  /** The text displayed for the 'Book' button. */
  public static final String BOOK_BUTTON_TEXT = "Book";

  /** The text displayed for the tooltip for the 'Book' button. */
  public static final String BOOK_BUTTON_TOOLTIP_TEXT = "Click to book the currently selected contractor.";

  /** The text displayed for the 'Browse' button. */
  public static final String BROWSE_BUTTON_TEXT = "Browse";

  /** The text displayed for the tooltip for the 'Browse' button. */
  public static final String BROWSE_BUTTON_TOOLTIP_TEXT = "Click to browseButton file system for database file.";

  /** The text displayed for the 'Clear' button. */
  public static final String CLEAR_BUTTON_TEXT = "Clear";

  /** The text displayed for the tooltip for the 'Clear' button. */
  public static final String CLEAR_BUTTON_TOOLTIP_TEXT = "Click to clear search results and display all contractors.";

  /** The title of the client configuration frame. */
  public static final String CLIENT_CONFIG_FRAME_TITLE_TEXT = "Client Configuration Settings";

  /** The message displayed to the user to notify them the system is shutting down. */
  public static final String CLOSING_APPLICATION_MESSAGE = " Closing application.";

  /** The title displayed in the border of the Configuration JPanel. */
  public static final String CONFIG_PANEL_BORDER_TITLE = "Configuration Panel";

  /** The text displayed for the 'Confirm' button. */
  public static final String CONFIRM_BUTTON_TEXT = "Confirm";

  /** The text displayed for the tooltip for the 'Confirm' button. */
  public static final String CONFIRM_BUTTON_TOOLTIP_TEXT = "Click to save configuration settings and start application";

  /** The title displayed in the ContractorAlreadyBookedException dialogue box. */
  public static final String CONTRACTOR_ALREADY_BOOKED_EXCEPTION_MESSAGE_TITLE = "Contractor Not Available";

  /** The title displayed in the contractor booking confirmation dialogue box. */
  public static final String CONTRACTOR_BOOKED_MESSAGE_TITLE = "Booking Confirmation";

  /** The message displayed in the contractor booking confirmation dialogue box. */
  public static final String CONTRACTOR_BOOKED_MESSAGE_TEXT = "Contractor has been successfully booked!";

  /** The title displayed in the ContractorNotFoundException dialogue box. */
  public static final String CONTRACTOR_NOT_FOUND_EXCEPTION_MESSAGE_TITLE = "No Records Available";

  /** The message displayed to the user when they click the 'Book' button on a non-booked record. */
  public static final String CUSTOMER_ID_PROMPT_TEXT = "Please enter the customer ID number.";

  /** The text for the 'Database Location' label. */
  public static final String DATABASE_FILE_LOCATION_LABEL_TEXT = "Database file location: ";

  /** The text displayed for the tooltip for the 'Database Location' textfield. */
  public static final String DATABASE_FILE_LOCATION_TOOLTIP_TEXT = "The file path of the database file.";

  /** The text displayed for the 'Exit' menu item */
  public static final String EXIT_MENU_ITEM_TEXT = "Exit";

  /** The title displayed in the dialogue box for any serious exceptions which occur. */
  public static final String FATAL_EXCEPTION_MESSAGE_TITLE = "Unexpected System Issue";

  /** The message displayed to the user when an incorrect customer ID is entered. */
  public static final String INVALID_CUSTOMER_ID_MESSAGE_TEXT = "Customer ID must be an 8 digit number.";

  /** The message displayed to the user when a blank database location is entered. */
  public static final String INVALID_DATABASE_LOCATION_MESSAGE_TEXT = "The database file location field cannot be blank";

  /** The title displayed in the dialogue box for any invalid user input. */
  public static final String INVALID_INPUT_MESSAGE_TITLE = "Invalid Input";

  /** The message displayed to the user when a blank ip address is entered. */
  public static final String INVALID_IP_MESSAGE_TEXT = "The ip address field cannot be blank.";

  /** The message displayed to the user when a non-numeric port number is entered. */
  public static final String INVALID_PORT_NUMBER_MESSAGE_TEXT = "The port number field must contain numbers only.";

  /** The text for the 'IP Address' label. */
  public static final String IP_ADDRESS_LABEL_TEXT = "Server IP address: ";

  /** The text displayed for the tooltip for the 'IP Address' label. */
  public static final String IP_ADDRESS_TOOLTIP_TEXT = "The IP address of the server you wish to connect to.";

  /** The text for the 'Location' label. */
  public static final String LOCATION_LABEL_TEXT = "Location: ";

  /** The text displayed for the tooltip for the 'Location' textfield. */
  public static final String LOCATION_TOOLTIP_TEXT = "The location of the contractor you wish to search for.";

  /** The text displayed for the 'Options' menu */
  public static final String OPTIONS_MENU_TEXT = "Options";

  /** The text for the 'Name' label. */
  public static final String NAME_LABEL_TEXT = "Name: ";

  /** The text displayed for the tooltip for the 'Name' textfield. */
  public static final String NAME_TOOLTIP_TEXT = "The name of the contractor you wish to search for.";

  /** The text for the 'Port' label. */
  public static final String PORT_NUMBER_LABEL_TEXT = "Server Port: ";

  /** The text displayed for the tooltip for the 'Port' textfield. */
  public static final String PORT_NUMBER_TOOLTIP_TEXT = "The port number of the server you wish to connect to.";

  /** The message displayed to the user in a dialogue box when a RemoteException occurs. */
  public static final String REMOTE_EXCEPTION_MESSAGE_TEXT = "Failure communicating with server.";

  /** The title displayed in the border of the Results JPanel. */
  public static final String RESULTS_BORDER_TITLE = "Results";

  /** The text displayed for the 'Search' button. */
  public static final String SEARCH_BUTTON_TEXT = "Search";

  /** The text displayed for the tooltip for the 'Search' button. */
  public static final String SEARCH_BUTTON_TOOLTIP_TEXT = "Click to display all contractors with the specified name and/or location.";

  /** The title of the server configuration frame. */
  public static final String SERVER_CONFIG_FRAME_TITLE_TEXT = "Server Configuration Settings";

  /** The title of the standalone configuration frame. */
  public static final String STANDALONE_CONFIG_FRAME_TITLE_TEXT = "Standalone Configuration Settings";

  /** The message displayed to the user when a the main application cannot be launched. */
  public static final String START_FAILURE_MESSAGE = "Failed to launch application: ";

  /** The name of the system displayed in the title of the Client window. */
  public static final String SYSTEM_NAME = "Bodgitt & Scarper Booking System";

}
