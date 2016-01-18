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

import static suncertify.ui.Messages.BROWSE_BUTTON_TEXT;
import static suncertify.ui.Messages.BROWSE_BUTTON_TOOLTIP_TEXT;
import static suncertify.ui.Messages.CONFIG_PANEL_BORDER_TITLE;
import static suncertify.ui.Messages.CONFIRM_BUTTON_TEXT;
import static suncertify.ui.Messages.CONFIRM_BUTTON_TOOLTIP_TEXT;
import static suncertify.ui.Messages.DATABASE_FILE_LOCATION_LABEL_TEXT;
import static suncertify.ui.Messages.DATABASE_FILE_LOCATION_TOOLTIP_TEXT;
import static suncertify.ui.Messages.INVALID_DATABASE_LOCATION_MESSAGE_TEXT;
import static suncertify.ui.Messages.INVALID_INPUT_MESSAGE_TITLE;
import static suncertify.ui.Messages.STANDALONE_CONFIG_FRAME_TITLE_TEXT;
import static suncertify.ui.Messages.START_FAILURE_MESSAGE;
import static suncertify.util.Constants.DEFAULT_TEXTFIELD_SIZE;

import suncertify.business.BasicContractorService;
import suncertify.business.ContractorService;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseAccessException;
import suncertify.db.DatabaseFactory;
import suncertify.util.Config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class StandaloneConfigWindow extends AbstractWindow implements LaunchManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private final JLabel dbFileLabel = new JLabel(DATABASE_FILE_LOCATION_LABEL_TEXT);
  private final JTextField dbFileField = new JTextField(DEFAULT_TEXTFIELD_SIZE);
  private final JButton browseButton = new JButton(BROWSE_BUTTON_TEXT);
  private final JButton confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
  private final JFileChooser dbFileChooser = new DatabaseFileChooser();
  private JPanel contentPanel;

  public StandaloneConfigWindow() {
    super(STANDALONE_CONFIG_FRAME_TITLE_TEXT);
    setSize(new Dimension(460, 175));
    setMinimumSize(new Dimension(460, 175));
    initializeComponents();
    getContentPane().add(contentPanel);
  }

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
    constraints.gridx = 2;
    constraints.gridy = 1;
    constraints.weighty = 0.8;
    constraints.anchor = GridBagConstraints.LAST_LINE_END;
    panel.add(confirmButton, constraints);
    return panel;
  }

  @Override
  public void initializeComponents() {
    contentPanel = createContentPanel();
    contentPanel.setBorder(BorderFactory.createTitledBorder(CONFIG_PANEL_BORDER_TITLE));
    dbFileField.setText(Config.getAloneDBLocation());
    dbFileField.setToolTipText(DATABASE_FILE_LOCATION_TOOLTIP_TEXT);
    browseButton.setToolTipText(BROWSE_BUTTON_TOOLTIP_TEXT);
    confirmButton.setToolTipText(CONFIRM_BUTTON_TOOLTIP_TEXT);
    browseButton.addActionListener(action -> {
      final int state = dbFileChooser.showOpenDialog(contentPanel);
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
  }

  @Override
  public boolean isConfigValid() {
    boolean isConfigValid = true;
    if (getDbFilePath().isEmpty()) {
      displayMessage(INVALID_DATABASE_LOCATION_MESSAGE_TEXT, INVALID_INPUT_MESSAGE_TITLE);
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
      final JFrame clientWindow = new ClientWindow(service);
      clientWindow.setVisible(true);
      dispose();
    } catch (final DatabaseAccessException exception) {
      handleFatalException(START_FAILURE_MESSAGE + exception.getMessage(), exception);
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
