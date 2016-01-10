package suncertify.ui.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.business.ContractorService;
import suncertify.ui.DatabaseFileChooser;
import suncertify.util.Config;

public class StandaloneConfigWindow extends ConfigWindow {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	private JLabel dbFileLocationLabel = new JLabel("Database file location: ");
	private JTextField dbFileLocationField = new JTextField(20);
	private JButton browse = new JButton("Browse");
	private JFileChooser dbFileChooser = new DatabaseFileChooser();

	public StandaloneConfigWindow() {
		super("Standalone Configuration Settings");
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
		dbFileLocationField.setText(Config.getAloneDBLocation());
		dbFileLocationField.setToolTipText("The location of the database file on the file system.");
		browse.setToolTipText("Click to browse file system for database file.");
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
			Config.setAloneDBLocation(dbFileLocationField.getText().trim());
			Config.saveProperties();
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public ContractorService getService() {
		return null;
	}
	
	@Override
	public void launch() {
		new MainWindow(getService());
		this.dispose();
	}
}
