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

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.swing.JButton;
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

  public ClientConfigWindow() {
    super("Client Configuration Settings");
    getContentPane().add(createContentPanel());
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel configPane = new JPanel();
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

    configPane.add(ipAddressLabel);
    configPane.add(ipAddressField);
    configPane.add(portLabel);
    configPane.add(portField);
    configPane.add(confirmButton);
    return configPane;
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
      new ClientWindow(service);
      dispose();
    } catch (final RemoteException e) {
      handleFatalException("Failed to launch application", e);
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
}
