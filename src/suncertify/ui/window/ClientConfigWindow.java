package suncertify.ui.window;

import static suncertify.util.Constants.CONFIGURATION_WINDOW_TITLE;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import suncertify.ui.panel.ClientConfigPanel;

public class ClientConfigWindow extends ConfigWindow {

	public ClientConfigWindow() {
		super(CONFIGURATION_WINDOW_TITLE);
		final JPanel configurationPanel = new ClientConfigPanel();
		this.add(configurationPanel, BorderLayout.NORTH);
	}

	@Override
	public void saveConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeWindow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addConfirmButton() {
		// TODO Auto-generated method stub

	}

}
