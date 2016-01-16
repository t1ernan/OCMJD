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

public interface WindowManager {

  void handleFatalException(String errorMessage, final Exception exception);

  void displayMessage(final String message, final String title, final int messageType);
  
  JPanel createContentPanel();

}