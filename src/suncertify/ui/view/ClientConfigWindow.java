package suncertify.ui.view;

import java.awt.BorderLayout;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.business.ContractorService;
import suncertify.business.rmi.RMIClient;
import suncertify.business.rmi.RMIServer;
import suncertify.db.DBFactory;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.util.Config;

public class ClientConfigWindow extends ConfigWindow {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	private JLabel serverIPAddressLabel = new JLabel("Server IP address: ");
	private JLabel serverPortNumberLabel = new JLabel("Server Port: ");
	private JTextField serverIPAddressField = new JTextField(20);
	private JTextField serverPortNumberField = new JTextField(20);

	public ClientConfigWindow() {
		super("Client Configuration Settings");
		this.getContentPane().add(createContentPanel(), BorderLayout.NORTH);
		this.getConfirmButton().addActionListener(x -> {
			saveConfig();
			launch();
		});
		this.pack();
	}

	@Override
	public JPanel createContentPanel() {
		JPanel configPane = new JPanel();
		serverIPAddressField.setText(Config.getServerIPAddress());
		serverIPAddressField.setToolTipText("The IP address of the server you wish to connect to.");
		serverPortNumberField.setText(Config.getClientPortNumber());
		serverPortNumberField.setToolTipText("The port number of the server you wish to connect to.");
		configPane.add(serverIPAddressLabel);
		configPane.add(serverIPAddressField);
		configPane.add(serverPortNumberLabel);
		configPane.add(serverPortNumberField);
		return configPane;
	}

	@Override
	public void saveConfig() {
		try {
			Config.setServerIPAddress(serverIPAddressField.getText().trim());
			Config.setClientPortNumber(serverPortNumberField.getText().trim());
			Config.saveProperties();
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public void launch() {
		try {
			final String ipAddress = Config.getServerIPAddress();
			final int port = Integer.parseInt(Config.getClientPortNumber());
			final ContractorService service = new RMIClient(ipAddress, port);
			new MainWindow(service);
		} catch (RemoteException | NotBoundException e) {
			final String errorMessage = "Failed to launch application: " + e.getMessage();
			JOptionPane.showMessageDialog(this, errorMessage, "System Error", JOptionPane.ERROR_MESSAGE);
		}finally{
			this.dispose();
		}
		
	}
}
