/*
 * BasicContractorService.java  1.0  15-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business;

import suncertify.db.DBMainExtended;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;
import suncertify.util.ContractorBuilder;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * BasicContractorService is the default implementation of {@link ContractorService}. It has an
 * implementation for booking and finding contractors from a specified data object.
 */
public class BasicContractorService implements ContractorService {

  /** Global Logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The data access object used to interact with the database. */
  private final DBMainExtended data;

  /**
   * Constructs a new BasicContractorService with the specified {@code data}.
   *
   * @param data
   *          the data access object used to interact with the database.
   */
  public BasicContractorService(final DBMainExtended data) {
    this.data = data;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void book(final Contractor contractor)
      throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
    LOGGER.info("Attempting to book contractor: " + contractor.toString());
    int recordNumber = -1;
    try {
      final String[] fieldValues = contractor.toStringArray();
      final String[] uniqueId = contractor.getPrimaryKey().toStringArray();
      recordNumber = data.find(uniqueId)[0];
      data.lock(recordNumber);
      checkContractorIsAvailable(recordNumber);
      data.update(recordNumber, fieldValues);
    } catch (final RecordNotFoundException e) {
      throw new ContractorNotFoundException("Contractor could not be found.", e);
    } finally {
      data.unlock(recordNumber);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Map<Integer, Contractor> find(final ContractorPk primaryKey)
      throws ContractorNotFoundException, RemoteException {
    LOGGER.info("Attempting to find contractors with : " + primaryKey.toString());
    final Map<Integer, Contractor> matchingRecords = new HashMap<>();
    try {
      final String[] searchCriteria = primaryKey.toStringArray();
      final int[] recordNumbers = data.find(searchCriteria);
      for (final int recordNumber : recordNumbers) {
        final String[] fieldValues = data.read(recordNumber);
        final Contractor contractor = ContractorBuilder.build(fieldValues);
        matchingRecords.put(recordNumber, contractor);
      }
    } catch (final RecordNotFoundException e) {
      throw new ContractorNotFoundException("Contractor could not be found.", e);
    }
    return matchingRecords;
  }

  /**
   * Check that the contractor with the specified record number is available for booking. Throws an
   * {@link AlreadyBookedException} if the contractor is booked.
   *
   * @param recordNumber
   *          the number of the contractor record
   * @throws AlreadyBookedException
   *           if the contractor with the specified record number has already been booked
   * @throws RemoteException
   *           if an RMI communication-related exception occurs
   * @throws RecordNotFoundException
   *           if the record does not exist or has been marked as deleted in the database
   */
  private void checkContractorIsAvailable(final int recordNumber)
      throws RecordNotFoundException, AlreadyBookedException, RemoteException {
    final String[] fieldValues = data.read(recordNumber);
    final Contractor contractor = ContractorBuilder.build(fieldValues);
    if (contractor.isBooked()) {
      throw new AlreadyBookedException("Contractor has already been booked.");
    }
  }
}
