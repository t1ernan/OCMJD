/*
 * DatabaseFileChooser.java  1.0  13-Jan-2016
 * 
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 * 
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseFileChooser.
 */
public class DatabaseFileChooser extends JFileChooser {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Instantiates a new database file chooser.
   */
  public DatabaseFileChooser() {
    super();
    setCurrentDirectory(new File(System.getProperty("user.dir")));
    setDialogTitle("Choose a database file.");
    setFileFilter(new FileNameExtensionFilter("*.db", "db"));
    setFileSelectionMode(JFileChooser.FILES_ONLY);
  }
}
