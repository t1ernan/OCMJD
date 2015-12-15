package suncertify.business;

import java.util.HashMap;
import java.util.Map;

import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class BasicContractorServices implements ContractorServices {

	private Data databaseManager;

	public BasicContractorServices(Data databaseManager) {
		this.databaseManager = databaseManager;
	}

	@Override
	public void createBooking(Contractor contractor) throws ServicesException {
		final String[] fieldValues = ContractorConverter.toFieldValues(contractor);
		try {
			databaseManager.create(fieldValues);
		} catch (DatabaseException e) {
			throw new DuplicateContractorException("Booking already exists. " + e.getMessage());
		}
	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK contractorPK) throws ServicesException {
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
