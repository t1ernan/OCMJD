package suncertify.ui.panel;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientConfigPanel extends ConfigurationPanel {

	private final JLabel serverIPAddressLabel = new JLabel("IP address of remote server: ");
	private final JTextField serverIPAddressField = new JTextField(config.getServerIPAddress());

	public ClientConfigPanel() {
		addServerIPAddressLabel();
		addServerIPAddressTextField();
		addPortNumberLabel();
		addPortNumberTextField();
		addConfirmButton();
	}

	public void addServerIPAddressLabel() {
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(serverIPAddressLabel, constraints);
	}

	public void addServerIPAddressTextField() {
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(serverIPAddressField, constraints);
	}

	@Override
	public void addConfirmButton() {
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		confirmButton.addActionListener(action -> {
			final String ipAddress = serverIPAddressField.getText();
			final String portNumber = getPortNumberField().getText();
			config.setServerIPAddress(ipAddress);
			config.setPortNumber(Integer.parseInt(portNumber));
			config.saveConfigValues();
		});
		this.add(confirmButton, constraints);
	}
}
