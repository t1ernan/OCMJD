package suncertify.business;

import java.util.HashMap;
import java.util.Map;

import suncertify.db.DAOFactory;
import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

public class DefaultContractorServices implements ContractorServices {

	private Data databaseManager = DAOFactory.getDbManager("db-2x2.db");

	@Override
	public void createBooking(Contractor contractor) throws DuplicateKeyException {
		final String[] fieldValues = ContractorConverter.toFieldValues(contractor);
		databaseManager.create(fieldValues);
	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK contractorPK) throws RecordNotFoundException {
		Map<Integer, Contractor> matchingRecords = new HashMap<>();
		final String[] searchCriteria = ContractorPKConverter.toSearchCriteria(contractorPK);
		final int[] recordNumbers = databaseManager.find(searchCriteria);
		for (int index = 0; index < recordNumbers.length; index++) {
			final Integer recordNumber = recordNumbers[index];
			final String[] fieldValues= databaseManager.read(recordNumbers[index]);
			final Contractor contractor = ContractorConverter.toContractor(fieldValues);
			matchingRecords.put(recordNumber, contractor);
		}
		return matchingRecords;
	}

}
