/*
 * DatabaseFileChooser.java  1.0  14-Jan-2016
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

/**
 * The class DatabaseFileChooser is a subclass of {@link JFileChooser} responsible for providing the
 * user with an easy, user-friendly way of selecting a database file during configuration of the
 * system on start-up. By default only files with file extensions of {@code .db} can be seen, but
 * this filter can be toggled off by the user if desired.
 */
public class DatabaseFileChooser extends JFileChooser {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new database file chooser.
   */
  public DatabaseFileChooser() {
    super();
    setCurrentDirectory(new File(System.getProperty("user.dir")));
    setDialogTitle("Choose a database file.");
    setFileFilter(new FileNameExtensionFilter("*.db", "db"));
    setFileSelectionMode(JFileChooser.FILES_ONLY);
  }
}
