/*
 * ClientConfigWindow.java  1.0  18-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import static suncertify.ui.Messages.CLIENT_CONFIG_FRAME_TITLE_TEXT;
import static suncertify.ui.Messages.CONFIG_PANEL_BORDER_TITLE;
import static suncertify.ui.Messages.CONFIRM_BUTTON_TEXT;
import static suncertify.ui.Messages.CONFIRM_BUTTON_TOOLTIP_TEXT;
import static suncertify.ui.Messages.INVALID_INPUT_MESSAGE_TITLE;
import static suncertify.ui.Messages.INVALID_IP_MESSAGE_TEXT;
import static suncertify.ui.Messages.INVALID_PORT_NUMBER_MESSAGE_TEXT;
import static suncertify.ui.Messages.IP_ADDRESS_LABEL_TEXT;
import static suncertify.ui.Messages.IP_ADDRESS_TOOLTIP_TEXT;
import static suncertify.ui.Messages.PORT_NUMBER_LABEL_TEXT;
import static suncertify.ui.Messages.PORT_NUMBER_TOOLTIP_TEXT;
import static suncertify.ui.Messages.REMOTE_EXCEPTION_MESSAGE_TEXT;
import static suncertify.ui.Messages.START_FAILURE_MESSAGE;
import static suncertify.util.Constants.DEFAULT_TEXTFIELD_SIZE;
import static suncertify.util.Utils.isInvalidPortNumber;

import suncertify.business.ContractorService;
import suncertify.business.rmi.RmiClient;
import suncertify.util.Config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class ClientConfigWindow is responsible for configuring and launching the main application
 * window, the {@link ClientWindow}, when the system is run in Networked Mode. It acts as a
 * configuration JFrame where the client's configuration values, details of the RMI server it wishes
 * to connect to, are entered and verified before attempting to launch the main application. It
 * extends {@link AbstractWindow} and implements {@link LaunchManager}.
 */
public final class ClientConfigWindow extends AbstractWindow implements LaunchManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The Global logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The ip address label. */
  private final JLabel ipAddressLabel = new JLabel(IP_ADDRESS_LABEL_TEXT);

  /** The port number label. */
  private final JLabel portLabel = new JLabel(PORT_NUMBER_LABEL_TEXT);

  /** The ip address field. */
  private final JTextField ipAddressField = new JTextField(DEFAULT_TEXTFIELD_SIZE);

  /** The port number field. */
  private final JTextField portField = new JTextField(DEFAULT_TEXTFIELD_SIZE);

  /** The confirm button. */
  private final JButton confirmButton = new JButton(CONFIRM_BUTTON_TEXT);

  /** The content panel. */
  private JPanel contentPanel;

  /**
   * Constructs a new client configuration window.
   */
  public ClientConfigWindow() {
    super(CLIENT_CONFIG_FRAME_TITLE_TEXT);
    setSize(new Dimension(437, 175));
    setMinimumSize(new Dimension(437, 175));
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
    panel.add(ipAddressLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    panel.add(ipAddressField, constraints);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_END;
    panel.add(portLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_START;
    panel.add(portField, constraints);
    constraints.gridx = 2;
    constraints.gridy = 2;
    constraints.ipady = 0;
    constraints.weighty = 0.8;
    constraints.anchor = GridBagConstraints.LAST_LINE_START;
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
    ipAddressField.setText(Config.getServerIpAddress());
    ipAddressField.addActionListener(action -> saveAndLaunch());
    portField.setText(Config.getClientPortNumber());
    portField.addActionListener(action -> saveAndLaunch());
    ipAddressField.setToolTipText(IP_ADDRESS_TOOLTIP_TEXT);
    portField.setToolTipText(PORT_NUMBER_TOOLTIP_TEXT);
    confirmButton.setToolTipText(CONFIRM_BUTTON_TOOLTIP_TEXT);
    confirmButton.addActionListener(action -> saveAndLaunch());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConfigValid() {
    boolean isConfigValid = true;
    if (getIpAddress().isEmpty()) {
      displayMessage(INVALID_IP_MESSAGE_TEXT, INVALID_INPUT_MESSAGE_TITLE);
      isConfigValid = false;
    } else if (isInvalidPortNumber(getPortNumber())) {
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
      final String ipAddress = Config.getServerIpAddress();
      final int port = Integer.parseInt(Config.getClientPortNumber());
      LOGGER.info("Starting client...");
      final ContractorService service = new RmiClient(ipAddress, port);
      final JFrame clientWindow = new ClientWindow(service);
      clientWindow.setVisible(true);
      dispose();
    } catch (final RemoteException exception) {
      handleFatalException(START_FAILURE_MESSAGE + REMOTE_EXCEPTION_MESSAGE_TEXT, exception);
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
    Config.setServerIpAddress(getIpAddress());
    Config.setClientPortNumber(getPortNumber());
    Config.saveProperties();
  }

  /**
   * Gets the ip address.
   *
   * @return the ip address
   */
  private String getIpAddress() {
    return ipAddressField.getText().trim();
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
