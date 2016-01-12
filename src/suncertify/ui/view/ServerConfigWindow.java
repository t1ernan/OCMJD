package suncertify.ui.view;

import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.business.rmi.RmiServer;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.db.DatabaseFactory;
import suncertify.ui.DatabaseFileChooser;
import suncertify.util.Config;

public class ServerConfigWindow extends AbstractConfigWindow {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	private JLabel serverPortNumberLabel = new JLabel("Server Port: ");
	private JLabel dbFileLocationLabel = new JLabel("Database file location: ");
	private JTextField serverPortNumberField = new JTextField(20);
	private JTextField dbFileLocationField = new JTextField(20);
	private JButton browse = new JButton("Browse");
	private JFileChooser dbFileChooser = new DatabaseFileChooser();

	public ServerConfigWindow() {
		super("Server Configuration Settings");
		this.getContentPane().add(createContentPanel(), BorderLayout.NORTH);
		this.getConfirmButton().addActionListener(x -> {
			saveConfig();
			launch();
		});
		this.pack();
	}

	@Override
	public JPanel createContentPanel() {
		final JPanel configPane = new JPanel();
		dbFileLocationField.setText(Config.getServerDBLocation());
		serverPortNumberField.setText(Config.getServerPortNumber());
		dbFileLocationField.setToolTipText("The location of the database file on the file system.");
		serverPortNumberField.setToolTipText("The port number that the server will run on.");
		browse.setToolTipText("Click to browse file system for database file.");
		configPane.add(serverPortNumberLabel);
		configPane.add(serverPortNumberField);
		configPane.add(dbFileLocationLabel);
		configPane.add(dbFileLocationField);
		browse.addActionListener(x -> {
			int state = dbFileChooser.showOpenDialog(configPane);
			if (state == JFileChooser.APPROVE_OPTION) {
				final String fileName = dbFileChooser.getSelectedFile().getAbsolutePath();
				dbFileLocationField.setText(fileName);
			}
		});
		configPane.add(browse);
		return configPane;
	}

	@Override
	public void saveConfig() {
		try {
			Config.setServerDBLocation(dbFileLocationField.getText().trim());
			Config.setServerPortNumber(serverPortNumberField.getText().trim());
			Config.saveProperties();
		} catch (IllegalArgumentException e) {
			displayWarningException(e);
		}
	}

	@Override
	public void launch() {
		try {
			DBMainExtended data = DatabaseFactory.getDatabase(Config.getServerDBLocation());
			int portNumber = Integer.parseInt(Config.getServerPortNumber());
			new RmiServer(data).startServer(portNumber);
		} catch (DatabaseException | RemoteException e) {
			displayFatalException(e);
		}
	}
}
