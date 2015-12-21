package suncertify.ui.panel;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class ServerConfigPanel extends ConfigurationPanel {

	private final JLabel serverDbLocationLabel = new JLabel("Location of database on the remote server: ");
	private final JTextField serverDbLocationField = new JTextField(config.getServerDBLocation());

	public ServerConfigPanel() {
		addServerDBLocationLabel();
		addServerDBLocationTextField();
		addPortNumberLabel();
		addPortNumberTextField();
		addConfirmButton();
	}

	public void addServerDBLocationLabel() {
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(serverDbLocationLabel, constraints);
	}

	public void addServerDBLocationTextField() {
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(serverDbLocationField, constraints);
	}

	@Override
	public void addConfirmButton() {
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		confirmButton.addActionListener(action -> {
			final String dbLocation = serverDbLocationField.getText();
			final String portNumber = getPortNumberField().getText();
			config.setServerDBLocation(dbLocation);
			config.setPortNumber(Integer.parseInt(portNumber));
			config.saveConfigValues();
		});
		this.add(confirmButton, constraints);
	}
}
