/*
 * LaunchManager.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

/**
 * LaunchManager defines useful method definitions for launching and configuring the application.
 * Should be implemented by all JFrames which configure and launch the main application.
 */
public interface LaunchManager {

  /**
   * Checks if configuration values specified are valid.
   *
   * @return true, if configuration is valid.
   */
  boolean isConfigValid();

  /**
   * Launches the main application window and disposes of the current window.
   */
  void launch();

  /**
   * Saves configuration properties and launches the main application window if the configuration
   * values entered are valid.
   */
  void saveAndLaunch();

  /**
   * Saves the specified configuration values to the suncertify.properties file.
   */
  void saveConfig();
}
