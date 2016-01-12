package suncertify.ui.view;

import suncertify.business.ContractorService;
import suncertify.business.rmi.RmiClient;
import suncertify.util.Config;

import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class ClientConfigWindow extends AbstractConfigWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  private final JLabel ipAddressLabel = new JLabel("Server IP address: ");
  private final JLabel portLabel = new JLabel("Server Port: ");
  private final JTextField ipAddressField = new JTextField(20);
  private final JTextField portField = new JTextField(20);

  public ClientConfigWindow() {
    super("Client Configuration Settings");
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
    ipAddressField.setText(Config.getServerIPAddress());
    ipAddressField.setToolTipText("The IP address of the server you wish to connect to.");
    portField.setText(Config.getClientPortNumber());
    portField.setToolTipText("The port number of the server you wish to connect to.");
    configPane.add(ipAddressLabel);
    configPane.add(ipAddressField);
    configPane.add(portLabel);
    configPane.add(portField);
    return configPane;
  }

  @Override
  public void launch() {
    try {
      final String ipAddress = Config.getServerIPAddress();
      final int port = Integer.parseInt(Config.getClientPortNumber());
      final ContractorService service = new RmiClient(ipAddress, port);
      new MainWindow(service);
      dispose();
    } catch (final RemoteException e) {
      displayFatalException(e);
    }
  }

  @Override
  public void saveConfig() {
    try {
      Config.setServerIPAddress(ipAddressField.getText().trim());
      Config.setClientPortNumber(portField.getText().trim());
      Config.saveProperties();
    } catch (final IllegalArgumentException e) {
      displayWarningException(e);
    }
  }
}
