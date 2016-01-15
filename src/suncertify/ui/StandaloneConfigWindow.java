/*
 * StandaloneConfigWindow.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

import suncertify.business.BasicContractorService;
import suncertify.business.ContractorService;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.db.DatabaseFactory;
import suncertify.util.Config;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class StandaloneConfigWindow extends AbstractConfigWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final JLabel dbFileLabel = new JLabel("Database file location: ");
  private final JTextField dbFileField = new JTextField(20);
  private final JButton browseButton = new JButton("Browse");
  private final JFileChooser dbFileChooser = new DatabaseFileChooser();

  public StandaloneConfigWindow() {
    super("Standalone Configuration Settings");
    getContentPane().add(createContentPanel(), BorderLayout.NORTH);
    getConfirmButton().addActionListener(action -> {
      saveConfig();
      launch();
    });
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel configPane = new JPanel();
    dbFileField.setText(Config.getAloneDBLocation());
    dbFileField.setToolTipText("The location of the database file on the file system.");
    browseButton.setToolTipText("Click to browseButton file system for database file.");
    configPane.add(dbFileLabel);
    configPane.add(dbFileField);
    browseButton.addActionListener(action -> {
      final int state = dbFileChooser.showOpenDialog(configPane);
      if (state == JFileChooser.APPROVE_OPTION) {
        final String fileName = dbFileChooser.getSelectedFile().getAbsolutePath();
        dbFileField.setText(fileName);
      }
    });
    configPane.add(browseButton);
    return configPane;
  }

  @Override
  public boolean isConfigValid(final String... configValues) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void launch() {
    try {
      final DBMainExtended data = DatabaseFactory.getDatabase(Config.getAloneDBLocation());
      final ContractorService service = new BasicContractorService(data);
      new MainWindow(service);
      dispose();
    } catch (final DatabaseException e) {
      displayFatalException(e);
    }
  }

  @Override
  public void saveConfig() {
      Config.setAloneDBLocation(dbFileField.getText().trim());
      Config.saveProperties();
  }
}