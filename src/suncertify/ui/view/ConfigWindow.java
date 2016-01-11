package suncertify.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	
	protected void displayFatalException(Exception e){
		final String errorMessage = "Failed to launch application: " + e.getMessage();
		JOptionPane.showMessageDialog(this, errorMessage, "System Error", JOptionPane.ERROR_MESSAGE);
		this.dispose();
	}
	
	protected void displayWarningException(Exception e){
		JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
	}
	
	protected abstract JPanel createContentPanel();
}
