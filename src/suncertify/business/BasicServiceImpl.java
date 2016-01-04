package suncertify.business;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import suncertify.db.DBMain;
import suncertify.db.DatabaseException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class BasicServiceImpl implements ContractorServices {

	private DBMain databaseManager;

	public BasicServiceImpl(DBMain data) {
		this.databaseManager = data;
	}

	@Override
	public void book(Contractor contractor)
			throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
		final String[] fieldValues = ContractorConverter.toFieldValues(contractor);
		final String[] uniqueId = ContractorPKConverter.toSearchCriteria(contractor.getPrimaryKey());
		final int recordNumber;
		try {
			recordNumber = databaseManager.find(uniqueId)[0];
			databaseManager.lock(recordNumber);
			validateContractor(recordNumber);
			databaseManager.update(recordNumber, fieldValues);
			databaseManager.unlock(recordNumber);
		} catch (RecordNotFoundException e) {
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		}
	}

	private void validateContractor(int recordNumber)
			throws ContractorNotFoundException, AlreadyBookedException, RemoteException, RecordNotFoundException {
		try {
			final String[] fieldValues = databaseManager.read(recordNumber);
			final Contractor contractor = ContractorConverter.toContractor(fieldValues);
			if (!contractor.getOwner().isEmpty()) {
				databaseManager.unlock(recordNumber);
				throw new AlreadyBookedException("Contractor has already been booked.");
			}
		} catch (RecordNotFoundException e) {
			databaseManager.unlock(recordNumber);
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		}
	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK contractorPK) throws ServiceException, RemoteException {
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
