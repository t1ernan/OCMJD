package suncertify.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DatabaseFileChooser extends JFileChooser{
	
	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;
	
	public DatabaseFileChooser(){
		this.setCurrentDirectory(new File(System.getProperty("user.dir")));
		this.setDialogTitle("Choose a database file.");
		this.setFileFilter(new FileNameExtensionFilter("*.db","db"));
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}
}
