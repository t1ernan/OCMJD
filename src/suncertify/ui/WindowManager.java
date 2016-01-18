/*
 * WindowManager.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import javax.swing.JPanel;

/**
 * WindowManager is the common interface for all UI frames in the application. It defines useful
 * method signatures for displaying messages to the user, handling exceptions, initializing frame
 * components and generating content for the frame the form of a JPanel.
 */
public interface WindowManager {

  /**
   * Generates all the content for the frame inside a JPanel and returns the panel.
   *
   * @return the JPanel containing the frame content.
   */
  JPanel createContentPanel();

  /**
   * Displays the specified {@code message} to the user in a dialog box with the specified
   * {@code title} and an information icon.
   *
   * @param message
   *          the message displayed to the user.
   * @param title
   *          the title of the dialogue box.
   */
  void displayMessage(final String message, final String title);

  /**
   * Logs the specified {@code exception} to the console and displays the specified {@code message}
   * to the user in a dialog box with the specified {@code title}.
   *
   * @param message
   *          the message displayed to the user.
   * @param title
   *          the title of the dialogue box.
   * @param exception
   *          the exception to log.
   */
  void handleException(final String message, final String title, final Exception exception);

  /**
   * Logs the specified {@code exception} to the console and displays the specified
   * {@code errorMessage} to the user in a dialog box before closing the application.
   *
   * @param errorMessage
   *          the error message displayed to the user.
   * @param exception
   *          the exception to log.
   */
  void handleFatalException(final String errorMessage, final Exception exception);

  /**
   * Initializes any window components, and adds any listeners required to the appropriate
   * components.
   */
  void initializeComponents();

}
