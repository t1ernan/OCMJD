package suncertify.ui.view;

import suncertify.business.ContractorService;
import suncertify.business.rmi.RmiClient;
import suncertify.util.Config;

import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientConfigWindow extends AbstractConfigWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  private final JLabel serverIpAddressLabel = new JLabel("Server IP address: ");
  private final JLabel serverPortNumberLabel = new JLabel("Server Port: ");
  private final JTextField serverIpAddressField = new JTextField(20);
  private final JTextField serverPortNumberField = new JTextField(20);

  public ClientConfigWindow() {
    super("Client Configuration Settings");
    getContentPane().add(createContentPanel(), BorderLayout.NORTH);
    getConfirmButton().addActionListener(x -> {
      saveConfig();
      launch();
    });
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel configPane = new JPanel();
    serverIpAddressField.setText(Config.getServerIPAddress());
    serverIpAddressField.setToolTipText("The IP address of the server you wish to connect to.");
    serverPortNumberField.setText(Config.getClientPortNumber());
    serverPortNumberField.setToolTipText("The port number of the server you wish to connect to.");
    configPane.add(serverIpAddressLabel);
    configPane.add(serverIpAddressField);
    configPane.add(serverPortNumberLabel);
    configPane.add(serverPortNumberField);
    return configPane;
  }

  @Override
  public void launch() {
    try {
      final String ipAddress = Config.getServerIPAddress();
      final int port = Integer.parseInt(Config.getClientPortNumber());
      final ContractorService service = new RmiClient(ipAddress, port);
      new MainWindow(service);
    } catch (final RemoteException e) {
      displayFatalException(e);
    }
  }

  @Override
  public void saveConfig() {
    try {
      Config.setServerIPAddress(serverIpAddressField.getText().trim());
      Config.setClientPortNumber(serverPortNumberField.getText().trim());
      Config.saveProperties();
    } catch (final IllegalArgumentException e) {
      displayWarningException(e);
    }
  }
}
