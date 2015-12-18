package suncertify.ui.launch;

import static suncertify.util.Constants.CONFIGURATION_WINDOW_TITLE;
import static suncertify.util.Constants.MAIN_WINDOW_LENGTH;
import static suncertify.util.Constants.MAIN_WINDOW_WIDTH;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import suncertify.ui.StandaloneConfigurationPanel;

public class StandaloneLaunchDialog extends JFrame implements StandaloneLaunchManager {

	private JPanel configurationPanel = new StandaloneConfigurationPanel();

	public StandaloneLaunchDialog() {
		super(CONFIGURATION_WINDOW_TITLE);
		this.setSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_LENGTH);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		final Container container = getContentPane();
		container.add(configurationPanel, BorderLayout.NORTH);
		setVisible(true);
	}

	@Override
	public void closeWindow() {

	}

	@Override
	public String getDBLocation() {
		return null;
	}

}
