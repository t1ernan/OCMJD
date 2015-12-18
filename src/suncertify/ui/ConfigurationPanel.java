package suncertify.ui;

import static suncertify.util.Constants.CONFIGURATION_PANEL_TITLE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class ConfigurationPanel extends JPanel {

	protected GridBagConstraints constraints = new GridBagConstraints();

	public ConfigurationPanel() {
		this.setBorder(BorderFactory.createTitledBorder(CONFIGURATION_PANEL_TITLE));
		this.setLayout(new GridBagLayout());
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
	}

}
