/*
 * ServerConfigWindow.java  1.0  18-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import static suncertify.ui.Messages.*;
import static suncertify.util.Constants.DEFAULT_TEXTFIELD_SIZE;
import static suncertify.util.Utils.isNonNumeric;

import suncertify.business.rmi.RmiServer;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseAccessException;
import suncertify.db.DatabaseFactory;
import suncertify.util.Config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class NetworkedClientConfigWindow is responsible for configuring and launching the main application
 * window, the {@link ClientWindow}, when the system is run in non-Networked Mode. It acts as a
 * configuration JFrame where the database file location is entered and verified before attempting
 * to launch the main application. It extends {@link AbstractWindow} and implements
 * {@link LaunchManager}.
 */
public final class ServerConfigWindow extends AbstractWindow implements LaunchManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The Global logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The port label. */
  private final JLabel portLabel = new JLabel(PORT_NUMBER_LABEL_TEXT);

  /** The database file path label. */
  private final JLabel dbFileLabel = new JLabel(DATABASE_FILE_LOCATION_LABEL_TEXT);

  /** The port field. */
  private final JTextField portField = new JTextField(DEFAULT_TEXTFIELD_SIZE);

  /** The database file path field. */
  private final JTextField dbFileField = new JTextField(DEFAULT_TEXTFIELD_SIZE);

  /** The browse button. */
  private final JButton browseButton = new JButton(BROWSE_BUTTON_TEXT);

  /** The confirm button. */
  private final JButton confirmButton = new JButton(CONFIRM_BUTTON_TEXT);

  /** The database file chooser. */
  private final JFileChooser dbFileChooser = new DatabaseFileChooser();

  /** The content panel. */
  private JPanel contentPanel;

  /**
   * Constructs a new server configuration window.
   */
  public ServerConfigWindow() {
    super(SERVER_CONFIG_FRAME_TITLE_TEXT);
    setSize(new Dimension(460, 192));
    setMinimumSize(new Dimension(460, 192));
    initializeComponents();
    getContentPane().add(contentPanel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JPanel createContentPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    final GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.ipady = 7;
    constraints.weighty = 0.1;
    constraints.anchor = GridBagConstraints.LINE_END;
    panel.add(dbFileLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    panel.add(dbFileField, constraints);
    constraints.gridx = 2;
    constraints.gridy = 0;
    constraints.ipady = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    panel.add(browseButton, constraints);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.ipady = 7;
    constraints.anchor = GridBagConstraints.LINE_END;
    panel.add(portLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_START;
    panel.add(portField, constraints);
    constraints.gridx = 2;
    constraints.gridy = 2;
    constraints.weighty = 0.8;
    constraints.ipady = 0;
    constraints.anchor = GridBagConstraints.LAST_LINE_END;
    panel.add(confirmButton, constraints);
    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeComponents() {
    contentPanel = createContentPanel();
    contentPanel.setBorder(BorderFactory.createTitledBorder(CONFIG_PANEL_BORDER_TITLE));
    dbFileField.setText(Config.getServerDbLocation());
    dbFileField.setToolTipText(DATABASE_FILE_LOCATION_TOOLTIP_TEXT);
    dbFileField.addActionListener(action -> saveAndLaunch());
    portField.setText(Config.getServerPortNumber());
    portField.setToolTipText(PORT_NUMBER_TOOLTIP_TEXT);
    portField.addActionListener(action -> saveAndLaunch());
    confirmButton.setToolTipText(CONFIRM_BUTTON_TOOLTIP_TEXT);
    confirmButton.addActionListener(action -> saveAndLaunch());
    browseButton.setToolTipText(BROWSE_BUTTON_TOOLTIP_TEXT);
    browseButton.addActionListener(action -> {
      final int state = dbFileChooser.showOpenDialog(contentPanel);
      if (state == JFileChooser.APPROVE_OPTION) {
        final String fileName = dbFileChooser.getSelectedFile().getAbsolutePath();
        dbFileField.setText(fileName);
      }
    });

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConfigValid() {
    boolean isConfigValid = true;
    if (getDbFilePath().isEmpty()) {
      displayMessage(INVALID_DATABASE_LOCATION_MESSAGE_TEXT, INVALID_INPUT_MESSAGE_TITLE);
      isConfigValid = false;
    } else if (isNonNumeric(getPortNumber())) {
      displayMessage(INVALID_PORT_NUMBER_MESSAGE_TEXT, INVALID_INPUT_MESSAGE_TITLE);
      isConfigValid = false;
    }
    return isConfigValid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void launch() {
    try {
      final DBMainExtended data = DatabaseFactory.getDatabase(Config.getServerDbLocation());
      final int portNumber = Integer.parseInt(Config.getServerPortNumber());
      LOGGER.info("Starting server...");
      new RmiServer(data).startServer(portNumber);
      final JFrame serverWindow = new ServerWindow(portNumber);
      serverWindow.setVisible(true);
      dispose();
    } catch (final RemoteException exception) {
      handleFatalException(START_FAILURE_MESSAGE + REMOTE_EXCEPTION_MESSAGE_TEXT, exception);
    } catch (final DatabaseAccessException exception) {
      handleFatalException(START_FAILURE_MESSAGE + exception.getMessage(), exception);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveAndLaunch() {
    if (isConfigValid()) {
      saveConfig();
      launch();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveConfig() {
    Config.setServerDbFileLocation(getDbFilePath());
    Config.setServerPortNumber(getPortNumber());
    Config.saveProperties();
  }

  /**
   * Gets the database file path.
   *
   * @return the database file path
   */
  private String getDbFilePath() {
    return dbFileField.getText().trim();
  }

  /**
   * Gets the port number.
   *
   * @return the port number
   */
  private String getPortNumber() {
    return portField.getText().trim();
  }
}
