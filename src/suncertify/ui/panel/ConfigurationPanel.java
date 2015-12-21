package suncertify.ui.panel;

import static suncertify.util.Constants.CONFIGURATION_PANEL_TITLE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.util.Config;

public abstract class ConfigurationPanel extends JPanel {

	protected final Config config = Config.getInstance();
	protected final JButton confirmButton = new JButton("Confirm");
	protected final GridBagConstraints constraints = new GridBagConstraints();

	private final JLabel portNumberLabel = new JLabel("Port Number to listen on: ");
	private final JTextField portNumberField = new JTextField(String.valueOf(config.getPortNumber()));

	public ConfigurationPanel() {
		this.setBorder(BorderFactory.createTitledBorder(CONFIGURATION_PANEL_TITLE));
		this.setLayout(new GridBagLayout());
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
	}

	public abstract void addConfirmButton();

	public void addPortNumberLabel() {
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(portNumberLabel, constraints);
	}

	public void addPortNumberTextField() {
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(portNumberField, constraints);
	}

	public JTextField getPortNumberField() {
		return portNumberField;
	}
}
