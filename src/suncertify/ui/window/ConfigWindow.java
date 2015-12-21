package suncertify.ui.window;

import static suncertify.util.Constants.CONFIGURATION_WINDOW_TITLE;
import static suncertify.util.Constants.MAIN_WINDOW_LENGTH;
import static suncertify.util.Constants.MAIN_WINDOW_WIDTH;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConfigWindow extends JFrame {

	public ConfigWindow(JPanel configurationPanel) {
		super(CONFIGURATION_WINDOW_TITLE);
		this.setSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_LENGTH);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(configurationPanel, BorderLayout.NORTH);
		setVisible(true);
	}

}
