package suncertify.ui.view;

import javax.swing.JFrame;

import suncertify.business.ContractorService;

public class MainWindow extends JFrame{
	
	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;
	
	public MainWindow(final ContractorService service){
		super("Bodgitt & Scarper Booking System");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
