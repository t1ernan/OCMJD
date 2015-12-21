package suncertify.ui.window;

import static suncertify.util.Constants.CONFIGURATION_WINDOW_TITLE;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.ui.panel.StandaloneConfigPanel;

public class StandaloneConfigWindow extends ConfigWindow {

	final JPanel configurationPanel = new StandaloneConfigPanel();
	private final JLabel standaloneDbLocationLabel = new JLabel("Location of database: ");
	private final JTextField standaloneDbLocationField = new JTextField(config.getStandaloneDBLocation());

	public StandaloneConfigWindow() {
		super(CONFIGURATION_WINDOW_TITLE);
		addStandaloneDBLocationLabel();
		addStandaloneDBLocationTextField();
		addConfirmButton();
		this.add(configurationPanel, BorderLayout.NORTH);
		setVisible(true);
	}

	private void addStandaloneDBLocationLabel() {
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 0;
		configurationPanel.add(standaloneDbLocationLabel, constraints);
	}

	private void addStandaloneDBLocationTextField() {
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		configurationPanel.add(standaloneDbLocationField, constraints);
	}

	@Override
	public void addConfirmButton() {
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		confirmButton.addActionListener(action -> closeWindow());
		configurationPanel.add(confirmButton, constraints);
	}

	@Override
	public void saveConfig() {
		final String dbLocation = standaloneDbLocationField.getText();
		config.setStandaloneDBLocation(dbLocation);
		config.saveConfigValues();
	}

	@Override
	public void closeWindow() {
		saveConfig();
		this.dispose();
	}

}
