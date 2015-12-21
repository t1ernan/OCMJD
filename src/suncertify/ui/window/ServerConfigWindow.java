package suncertify.ui.window;

import static suncertify.util.Constants.CONFIGURATION_WINDOW_TITLE;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import suncertify.ui.panel.ServerConfigPanel;

public class ServerConfigWindow extends ConfigWindow {

	public ServerConfigWindow() {
		super(CONFIGURATION_WINDOW_TITLE);
		final JPanel configurationPanel = new ServerConfigPanel();
		this.add(configurationPanel, BorderLayout.NORTH);
	}

	@Override
	public void addConfirmButton() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeWindow() {
		// TODO Auto-generated method stub

	}

}
