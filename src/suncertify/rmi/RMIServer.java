package suncertify.rmi;

import static suncertify.util.Constants.RMI_ID;
import static suncertify.util.Constants.RMI_PORT;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import suncertify.business.ContractorNotFoundException;
import suncertify.business.DuplicateContractorException;
import suncertify.business.ServicesException;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class RMIServer implements RMIServices {

	private Data databaseManager;

	public RMIServer(Data databaseManager) throws RemoteException {
		this.databaseManager = databaseManager;
	}

	@Override
	public void createBooking(Contractor contractor) throws ServicesException, RemoteException {
		final String[] fieldValues = ContractorConverter.toFieldValues(contractor);
		try {
			databaseManager.create(fieldValues);
		} catch (DatabaseException e) {
			throw new DuplicateContractorException("Booking already exists. " + e.getMessage());
		}

	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK contractorPK) throws ServicesException, RemoteException {
		final Map<Integer, Contractor> matchingRecords = new HashMap<>();
		final String[] searchCriteria = ContractorPKConverter.toSearchCriteria(contractorPK);
		try {
			final int[] recordNumbers = databaseManager.find(searchCriteria);
			for (int index = 0; index < recordNumbers.length; index++) {
				final Integer recordNumber = recordNumbers[index];
				final String[] fieldValues = databaseManager.read(recordNumbers[index]);
				final Contractor contractor = ContractorConverter.toContractor(fieldValues);
				matchingRecords.put(recordNumber, contractor);
			}
		} catch (DatabaseException e) {
			throw new ContractorNotFoundException("No such contractor exists. " + e.getMessage());
		}
		return matchingRecords;
	}

	@Override
	public void startServer(int port) throws RemoteException {
		final RMIServices impl = (RMIServices) UnicastRemoteObject.exportObject(this, 0);
		final Registry registry = LocateRegistry.createRegistry(RMI_PORT);
		registry.rebind(RMI_ID, impl);

		// Runtime.getRuntime().addShutdownHook(new Thread() {
		// @Override
		// public void run() {
		// try {
		// databaseManager.save();
		// } catch (DatabaseException e) {
		// System.err.println("Could not save data: " + e.getMessage());
		// }
		// }
		// });

		System.out.println("Server is running on port " + port);
	}
}
