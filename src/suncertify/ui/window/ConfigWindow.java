package suncertify.ui.window;

import static suncertify.util.Constants.MAIN_WINDOW_LENGTH;
import static suncertify.util.Constants.MAIN_WINDOW_WIDTH;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;

import suncertify.util.Config;

public abstract class ConfigWindow extends JFrame implements ConfigLaunchManager {

	protected final Config config = Config.getInstance();
	protected final JButton confirmButton = new JButton("Confirm");
	protected final GridBagConstraints constraints = new GridBagConstraints();

	public ConfigWindow(String title) {
		super(title);
		this.setSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_LENGTH);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
