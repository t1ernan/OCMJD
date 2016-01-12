package suncertify.business;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * ContractorService is the common interface for all classes that wish to query the database in the
 * Service Layer. The business methods defined in this interface will be exposed to the presentation
 * layer, allowing it to indirectly query the database layer through calling these exposed methods.
 */
public interface ContractorService {

  /**
   * Book the specified {@code contractor}.
   *
   * @param contractor
   *          the contractor to book.
   * @throws ContractorNotFoundException
   *           if the specified {@code contractor} could be found.
   * @throws AlreadyBookedException
   *           if the specified {@code contractor} has already been booked.
   * @throws RemoteException
   *           if an RMI communication-related exception occurs.
   */
  void book(final Contractor contractor)
      throws ContractorNotFoundException, AlreadyBookedException, RemoteException;

  /**
   * Find all contractor records with the specified {@code primaryKey}. This method uses the
   * {@code name} and {@code location} fields in the {@code primaryKey} to find all contractors with
   * a specific name and/or location. Returns all matching contractors in a map, with the record
   * number of the contractor as the key and the contractor record as the value.<br>
   * <br>
   * <b>Example 1:</b> A {@code primaryKey} with {@code name}="Fred" and {@code location}="Paris"
   * will return all non-deleted records of contractors who's name begins with "Fred", and location
   * begins with "Paris".<br>
   * <br>
   * <b>Example 2:</b> A {@code primaryKey} with {@code name}="Fred" and {@code location}="" will
   * return all non-deleted records of contractors who's name begins with "Fred". <br>
   * <br>
   * <b>Example 3:</b> A {@code primaryKey} with {@code name}="" and {@code location}="Paris" will
   * return all non-deleted records of contractors who's location begins with "Paris".<br>
   * <br>
   * <b>Example 4:</b> A {@code primaryKey} with {@code name}="" and {@code location}="" will return
   * all non-deleted contractor records available.
   *
   * @param primaryKey
   *          the primary key
   * @return a map, with the record number of the contractor as the key and the contractor records
   *         the value.
   * @throws ContractorNotFoundException
   *           if no contractor with the specified {@code primaryKey} could be found.
   * @throws RemoteException
   *           if an RMI communication-related exception occurs.
   */
  Map<Integer, Contractor> find(final ContractorPk primaryKey)
      throws ContractorNotFoundException, RemoteException;
}
