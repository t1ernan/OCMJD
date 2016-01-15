/*
 * ServerConfigWindow.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

import static suncertify.util.Utils.isInvalidPortNumber;
import static suncertify.util.Utils.log;

import suncertify.business.rmi.RmiServer;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.db.DatabaseFactory;
import suncertify.util.Config;

import java.rmi.RemoteException;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class ServerConfigWindow extends AbstractWindow implements LaunchManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final JLabel portLabel = new JLabel("Server Port: ");
  private final JLabel dbFileLabel = new JLabel("Database file location: ");
  private final JTextField portField = new JTextField(20);
  private final JTextField dbFileField = new JTextField(20);
  private final JButton browseButton = new JButton("Browse");
  private final JButton confirmButton = new JButton("Confirm");
  private final JFileChooser dbFileChooser = new DatabaseFileChooser();

  public ServerConfigWindow() {
    super("Server Configuration Settings");
    getContentPane().add(createContentPanel());
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel configPane = new JPanel();
    dbFileField.setText(Config.getServerDBLocation());
    portField.setText(Config.getServerPortNumber());

    dbFileField.setToolTipText("The location of the database file on the file system.");
    portField.setToolTipText("The port number that the server will run on.");
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

    configPane.add(portLabel);
    configPane.add(portField);
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
    } else if (isInvalidPortNumber(getPortNumber())) {
      displayMessage("The port number field must contain numbers only.", "Invalid Input",
          JOptionPane.WARNING_MESSAGE);
      isConfigValid = false;
    }
    return isConfigValid;
  }

  @Override
  public void launch() {
    try {
      final DBMainExtended data = DatabaseFactory.getDatabase(Config.getServerDBLocation());
      final int portNumber = Integer.parseInt(Config.getServerPortNumber());
      log(Level.INFO, "Starting server...");
      new RmiServer(data).startServer(portNumber);
      dispose();
    } catch (DatabaseException | RemoteException e) {
      displayFatalException(e);
    }
  }

  @Override
  public void saveConfig() {
    Config.setServerDBLocation(getDbFilePath());
    Config.setServerPortNumber(getPortNumber());
    Config.saveProperties();
  }

  private String getDbFilePath() {
    return dbFileField.getText().trim();
  }

  private String getPortNumber() {
    return portField.getText().trim();
  }
}
