package suncertify.ui.panel;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class StandaloneConfigPanel extends ConfigurationPanel {

	private final JLabel standaloneDbLocationLabel = new JLabel("Location of database in file system: ");
	private final JTextField standaloneDbLocationField = new JTextField(config.getStandaloneDBLocation());

	public StandaloneConfigPanel() {
		addStandaloneDBLocationLabel();
		addStandaloneDBLocationTextField();
		addConfirmButton();
	}

	public void addStandaloneDBLocationLabel() {
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(standaloneDbLocationLabel, constraints);
	}

	public void addStandaloneDBLocationTextField() {
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(standaloneDbLocationField, constraints);
	}

	public void addConfirmButton() {
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		confirmButton.addActionListener(action -> {
			final String dbLocation = standaloneDbLocationField.getText();
			config.setStandaloneDBLocation(dbLocation);
			config.saveConfigValues();
		});
		this.add(confirmButton, constraints);
	}
}
