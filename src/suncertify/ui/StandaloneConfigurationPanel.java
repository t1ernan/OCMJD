package suncertify.ui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class StandaloneConfigurationPanel extends ConfigurationPanel {

	private final JLabel dbLocationLabel = new JLabel("Location of database in file system: ");
	private final JTextField dbLocationField = new JTextField(System.getProperty("user.dir"));
	private final JButton confirmButton = new JButton("Confirm");

	public StandaloneConfigurationPanel() {
		addDBLocationLabel();
		addDBLocationTextField();
		addConfirmButton();
	}

	private void addDBLocationLabel() {
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(dbLocationLabel, constraints);
	}

	private void addDBLocationTextField() {
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(dbLocationField, constraints);
	}

	private void addConfirmButton() {
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(dbLocationField.getText());

			}
		});
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(confirmButton, constraints);
	}

}
