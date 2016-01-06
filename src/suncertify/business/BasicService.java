package suncertify.business;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;
import suncertify.dto.Contractor;
import suncertify.dto.ContractorPK;
import suncertify.util.ContractorConverter;
import suncertify.util.ContractorPKConverter;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicServiceImpl.
 */
public class BasicService implements ContractorService {

	/** The database manager. */
	private DBMain databaseManager;

	/**
	 * Instantiates a new basic service impl.
	 *
	 * @param data
	 *            the data
	 */
	public BasicService(DBMain data) {
		this.databaseManager = data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void book(final Contractor contractor)
			throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
		final String[] fieldValues = ContractorConverter.toStringArray(contractor);
		final String[] uniqueId = ContractorPKConverter.toStringArray(contractor.getPrimaryKey());
		final int recordNumber;
		try {
			recordNumber = databaseManager.find(uniqueId)[0];
			databaseManager.lock(recordNumber);
			checkContractorIsAvailable(recordNumber);
			databaseManager.update(recordNumber, fieldValues);
			databaseManager.unlock(recordNumber);
		} catch (RecordNotFoundException e) {
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, Contractor> find(final ContractorPK contractorPK)
			throws ContractorNotFoundException, RemoteException {
		final Map<Integer, Contractor> matchingRecords = new HashMap<>();
		final String[] searchCriteria = ContractorPKConverter.toStringArray(contractorPK);
		try {
			final int[] recordNumbers = databaseManager.find(searchCriteria);
			for (int index = 0; index < recordNumbers.length; index++) {
				final Integer recordNumber = recordNumbers[index];
				final String[] fieldValues = databaseManager.read(recordNumbers[index]);
				final Contractor contractor = ContractorConverter.toContractor(fieldValues);
				matchingRecords.put(recordNumber, contractor);
			}
		} catch (RecordNotFoundException e) {
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		}
		return matchingRecords;
	}

	/**
	 * Check that the contractor with the specified record number is available
	 * for booking.
	 *
	 * @param recordNumber
	 *            the number of the contractor record.
	 * @throws ContractorNotFoundException
	 *             if the contractor with the specified record number could not
	 *             be found.
	 * @throws AlreadyBookedException
	 *             if the contractor with the specified record number has
	 *             already been booked.
	 * @throws RemoteException
	 *             if an RMI communication-related exception occurs.
	 * @throws RecordNotFoundException
	 *             the record not found exception
	 */
	private void checkContractorIsAvailable(final int recordNumber)
			throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
		try {
			final String[] fieldValues = databaseManager.read(recordNumber);
			final Contractor contractor = ContractorConverter.toContractor(fieldValues);
			if (!contractor.getCustomerId().isEmpty()) {
				databaseManager.unlock(recordNumber);
				throw new AlreadyBookedException("Contractor has already been booked.");
			}
		} catch (RecordNotFoundException e) {
			try {
				databaseManager.unlock(recordNumber);
			} catch (RecordNotFoundException e1) {
				throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
			}
		}
	}
}
