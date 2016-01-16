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

import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class StandaloneConfigWindow extends AbstractWindow implements LaunchManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private final JLabel dbFileLabel = new JLabel("Database file location: ");
  private final JTextField dbFileField = new JTextField(20);
  private final JButton browseButton = new JButton("Browse");
  private final JButton confirmButton = new JButton("Confirm");
  private final JFileChooser dbFileChooser = new DatabaseFileChooser();

  public StandaloneConfigWindow() {
    super("Standalone Configuration Settings");
    getContentPane().add(createContentPanel());
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel configPane = new JPanel();
    dbFileField.setText(Config.getAloneDBLocation());

    dbFileField.setToolTipText("The location of the database file on the file system.");
    browseButton.setToolTipText("Click to browseButton file system for database file.");
    confirmButton.setToolTipText("Click to save configuration settings and start application");

    browseButton.addActionListener(action -> {
      final int state = dbFileChooser.showOpenDialog(configPane);
      if (state == JFileChooser.APPROVE_OPTION) {
        final String fileName = dbFileChooser.getSelectedFile().getAbsolutePath();
        dbFileField.setText(fileName);
      }
    });

    confirmButton.addActionListener(action -> {
      if (isConfigValid()) {
        saveConfig();
        launch();
      }
    });

    configPane.add(dbFileLabel);
    configPane.add(dbFileField);
    configPane.add(browseButton);
    configPane.add(confirmButton);
    return configPane;
  }

  @Override
  public boolean isConfigValid() {
    boolean isConfigValid = true;
    if (getDbFilePath().isEmpty()) {
      displayMessage("The database file location field cannot be blank", "Invalid Input",
          JOptionPane.WARNING_MESSAGE);
      isConfigValid = false;
    }
    return isConfigValid;
  }

  @Override
  public void launch() {
    try {
      final DBMainExtended data = DatabaseFactory.getDatabase(Config.getAloneDBLocation());
      LOGGER.info("Starting standalone...");
      final ContractorService service = new BasicContractorService(data);
      new ClientWindow(service);
      dispose();
    } catch (final DatabaseException e) {
      handleFatalException("Failed to launch application", e);
    }
  }

  @Override
  public void saveConfig() {
    Config.setAloneDBLocation(getDbFilePath());
    Config.saveProperties();
  }

  private String getDbFilePath() {
    return dbFileField.getText().trim();
  }
}
