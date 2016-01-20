/*
 * ContractorService.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * ContractorService is the common interface for all classes in the services layer that wish to
 * query the database. The business methods defined in this interface will be exposed to the
 * presentation layer, which will allow users of the application to query the database indirectly.
 *
 * <p>The presentation layer should <i>never</i> have direct access to the database layer under any
 * circumstances. The presentation layer should call the methods defined in the services layer to
 * retrieve data from the database.
 */
public interface ContractorService {

  /**
   * Book the specified {@code contractor}. Updates the contractor record in the database with the
   * new contractor data. The contractor record updated will have the same {@code primaryKey} value
   * as the specified contractor's {@code primaryKey} field.
   *
   * @param contractor
   *          the contractor to book.
   * @throws ContractorNotFoundException
   *           if the specified {@code contractor} could be found.
   * @throws AlreadyBookedException
   *           if the specified {@code contractor} has already been booked.
   * @throws RemoteException
   *           if an RMI communication-related exception occurs.
   * @throws IllegalArgumentException
   *           if {@code contractor} is null.
   */
  void book(final Contractor contractor) throws ContractorNotFoundException, AlreadyBookedException,
      RemoteException, IllegalArgumentException;

  /**
   * Find all contractor records with the specified {@code primaryKey}. This method uses the
   * {@code name} and {@code location} fields in the {@code primaryKey} to find all contractors with
   * a specific name and/or location. Returns all matching contractors in a map, with the record
   * number of the contractor as the key and the contractor record as the value. This is a case
   * sensitive search.<br>
   * <br>
   * <b>Example 1:</b> A {@code primaryKey} with {@code name}="Fred" and {@code location}="Paris"
   * will return all non-deleted records of contractors who's name is "Fred", and location is
   * "Paris".<br>
   * <br>
   * <b>Example 2:</b> A {@code primaryKey} with {@code name}="Fred" and {@code location}="" will
   * return all non-deleted records of contractors who's name is "Fred". <br>
   * <br>
   * <b>Example 3:</b> A {@code primaryKey} with {@code name}="" and {@code location}="Paris" will
   * return all non-deleted records of contractors who's location is "Paris".<br>
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
   * @throws IllegalArgumentException
   *           if {@code ContractorPk} is null.
   */
  Map<Integer, Contractor> find(final ContractorPk primaryKey)
      throws ContractorNotFoundException, RemoteException, IllegalArgumentException;
}
