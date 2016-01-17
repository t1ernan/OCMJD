/*
 * ClientConfigWindow.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class ClientConfigWindow extends AbstractWindow implements LaunchManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private final JLabel ipAddressLabel = new JLabel("Server IP address: ");
  private final JLabel portLabel = new JLabel("Server Port: ");
  private final JTextField ipAddressField = new JTextField(20);
  private final JTextField portField = new JTextField(20);
  private final JButton confirmButton = new JButton("Confirm");
  private JPanel contentPanel;

  public ClientConfigWindow() {
    super("Client Configuration Settings");
    setSize(new Dimension(437, 175));
    setMinimumSize(new Dimension(437, 175));
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

  @Override
  public boolean isConfigValid() {
    boolean isConfigValid = true;
    if (getIpAddress().isEmpty()) {
      displayMessage("The database file location field cannot be blank.", "Invalid Input",
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
      final String ipAddress = Config.getServerIPAddress();
      final int port = Integer.parseInt(Config.getClientPortNumber());
      LOGGER.info("Starting client...");
      final ContractorService service = new RmiClient(ipAddress, port);
      final JFrame clientWindow = new ClientWindow(service);
      clientWindow.setVisible(true);
      dispose();
    } catch (final RemoteException e) {
      handleFatalException("Failed to launch application, cannot contact server", e);
    }
  }

  @Override
  public void saveConfig() {
    Config.setServerIPAddress(getIpAddress());
    Config.setClientPortNumber(getPortNumber());
    Config.saveProperties();
  }

  private String getIpAddress() {
    return ipAddressField.getText().trim();
  }

  private String getPortNumber() {
    return portField.getText().trim();
  }

  @Override
  public void initializeComponents() {
    contentPanel = createContentPanel();
    contentPanel.setBorder(BorderFactory.createTitledBorder("Config Panel"));
    ipAddressField.setText(Config.getServerIPAddress());
    portField.setText(Config.getClientPortNumber());
    ipAddressField.setToolTipText("The IP address of the server you wish to connect to.");
    portField.setToolTipText("The port number of the server you wish to connect to.");
    confirmButton.setToolTipText("Click to save configuration settings and start application");
    confirmButton.addActionListener(action -> {
      if (isConfigValid()) {
        saveConfig();
        launch();
      }
    });

  }
}
