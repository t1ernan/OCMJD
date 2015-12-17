package suncertify.business;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class BasicServiceImpl implements ContractorServices {

	private Data databaseManager;

	public BasicServiceImpl(Data databaseManager) {
		this.databaseManager = databaseManager;
	}

	@Override
	public void book(Contractor contractor) throws ServicesException, RemoteException {
		final String[] fieldValues = ContractorConverter.toFieldValues(contractor);
		final String[] uniqueId = ContractorPKConverter.toSearchCriteria(contractor.getPrimaryKey());
		try {
			final int recordNumber = databaseManager.find(uniqueId)[0];
			databaseManager.lock(recordNumber);
			if (isContractorAvailableForBooking(recordNumber)) {
				databaseManager.update(recordNumber, fieldValues);
				databaseManager.unlock(recordNumber);
			} else {
				databaseManager.unlock(recordNumber);
				throw new AlreadyBookedException("Contractor has already been booked.");
			}
		} catch (RecordNotFoundException e) {
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		}

	}

	private boolean isContractorAvailableForBooking(int recordNumber) throws RecordNotFoundException, RemoteException {
		final String[] fieldValues = databaseManager.read(recordNumber);
		final Contractor contractor = ContractorConverter.toContractor(fieldValues);
		final String bookingReference = contractor.getOwner();
		return bookingReference.isEmpty();
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

}
