package suncertify.business;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import suncertify.db.DBMainExtended;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.util.ContractorBuilder;
import suncertify.util.Utils;

/**
 * BasicContractorService is a default implementation of
 * {@link ContractorService}. It contains the business logic necessary to
 * implement the business methods defined the ContractorService interface .
 */
public class BasicContractorService implements ContractorService {

	/** The database manager used to interact with the database. */
	private DBMainExtended databaseManager;

	/**
	 * Constructs a new BasicContractorService with the specified {@link }.
	 *
	 * @param data
	 *            the database manager used to interact with the database.
	 */
	public BasicContractorService(final DBMainExtended databaseManager) {
		this.databaseManager = databaseManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void book(final Contractor contractor)
			throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
		final String[] fieldValues = contractor.toStringArray();
		final String[] uniqueId = contractor.getPrimaryKey().toStringArray();
		int recordNumber = -1;
		try {
			recordNumber = databaseManager.find(uniqueId)[0];
			databaseManager.lock(recordNumber);
			checkContractorIsAvailable(recordNumber);
			databaseManager.update(recordNumber, fieldValues);
		} catch (RecordNotFoundException e) {
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		} finally {
			databaseManager.unlock(recordNumber);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, Contractor> find(final ContractorPK contractorPK)
			throws ContractorNotFoundException, RemoteException {
		final Map<Integer, Contractor> matchingRecords = new HashMap<>();
		final String[] searchCriteria = contractorPK.toStringArray();
		try {
			final int[] recordNumbers = databaseManager.find(searchCriteria);
			for (int index = 0; index < recordNumbers.length; index++) {
				final Integer recordNumber = recordNumbers[index];
				final String[] fieldValues = databaseManager.read(recordNumber);
				final Contractor contractor = ContractorBuilder.build(fieldValues);
				matchingRecords.put(recordNumber, contractor);
			}
		} catch (RecordNotFoundException e) {
			throw new ContractorNotFoundException("Contractor could not be found. " + e.getMessage());
		}
		return matchingRecords;
	}

	/**
	 * Check that the contractor with the specified record number is available
	 * for booking. Throws an {@link AlreadyBookedException} if the contractor
	 * has already been booked.
	 *
	 * @param recordNumber
	 *            the number of the contractor record.
	 * @throws AlreadyBookedException
	 *             if the contractor with the specified record number has
	 *             already been booked.
	 * @throws RemoteException
	 *             if an RMI communication-related exception occurs.
	 * @throws RecordNotFoundException
	 *             if the record does not exist or has been marked as deleted in
	 *             the database
	 */
	private void checkContractorIsAvailable(final int recordNumber)
			throws RecordNotFoundException, AlreadyBookedException, RemoteException {
		final String[] fieldValues = databaseManager.read(recordNumber);
		final Contractor contractor = ContractorBuilder.build(fieldValues);
		if (contractor.isBooked()) {
			throw new AlreadyBookedException("Contractor has already been booked.");
		}
	}
}
