package suncertify.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public abstract class ConfigWindow extends JFrame implements LaunchManager {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	private final JButton confirm = new JButton("Confirm");

	public ConfigWindow(final String title) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.LIGHT_GRAY);
		confirm.setToolTipText("Click to save configuration settings and start application");
		this.getContentPane().add(confirm, BorderLayout.SOUTH);
	}
	
	protected JButton getConfirmButton(){
		return confirm;
	}
	
	protected abstract JPanel createContentPanel();
}
