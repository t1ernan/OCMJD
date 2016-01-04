package suncertify.ui.window;

import static suncertify.util.Constants.EMPTY_SPACE;
import static suncertify.util.Constants.MAIN_WINDOW_TITLE;

import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import suncertify.business.ContractorServices;
import suncertify.business.ServiceException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.ui.controllers.DisplayContractorListener;

public class MainWindow extends JFrame {

	private JTextField name = new JTextField(EMPTY_SPACE);
	private JTextField location = new JTextField(EMPTY_SPACE);

	public MainWindow(ContractorServices service) {
		super(MAIN_WINDOW_TITLE);

		this.setSize(500, 500);
		this.setVisible(true);

		JButton displayContractor = new JButton("Display Contractor(s)");
		ActionListener displayListener = new DisplayContractorListener(this, service);
		displayContractor.addActionListener(displayListener);

		ContractorPK primaryKey = new ContractorPK();
		primaryKey.setName(getContractorName());
		primaryKey.setLocation(getContractorLocation());

		try {
			Map<Integer, Contractor> records = service.find(primaryKey);
			String[] columnNames = new String[] { "Name", "Location", "Specialities", "Size", "Rate", "Owner" };
		} catch (ServiceException e) {
			final String message = "No contractor with the given parameters could be found";
			JOptionPane.showMessageDialog(null, message, "Contractor Booking System", JOptionPane.INFORMATION_MESSAGE);
		} catch (RemoteException e) {
			final String message = "A problem occurred while trying to communicate with remote server";
			JOptionPane.showMessageDialog(null, message, "Contractor Booking System", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public String getContractorName() {
		return name.getText();
	}

	public String getContractorLocation() {
		return location.getText();
	}
}
