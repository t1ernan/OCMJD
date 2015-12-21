package suncertify.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import suncertify.business.ContractorServices;
import suncertify.business.ServicesException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.ui.window.MainWindow;

public class DisplayContractorListener implements ActionListener {

	private MainWindow mainWindow;
	private ContractorServices service;

	public DisplayContractorListener(MainWindow mainWindow, ContractorServices service) {
		this.mainWindow = mainWindow;
		this.service = service;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final String name = mainWindow.getContractorName();
		final String location = mainWindow.getContractorLocation();

		ContractorPK primaryKey = new ContractorPK();
		primaryKey.setName(name);
		primaryKey.setLocation(location);

		try {
			Contractor contractor = service.find(primaryKey).get(0);
			displayContractor(contractor);
		} catch (ServicesException e) {
			final String message = "No contractor with the given parameters could be found";
			JOptionPane.showMessageDialog(null, message, "Contractor Booking System", JOptionPane.INFORMATION_MESSAGE);
		} catch (RemoteException e) {
			final String message = "A problem occurred while trying to communicate with remote server";
			JOptionPane.showMessageDialog(null, message, "Contractor Booking System", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void displayContractor(Contractor contractor) {

	}

}
