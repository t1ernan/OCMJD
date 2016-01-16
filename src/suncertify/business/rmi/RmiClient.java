/*
 * RmiClient.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business.rmi;

import static suncertify.util.Constants.RMI_ID;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.logging.Logger;

/**
 * An RMI client used to connect and request data from an RMI server through the business methods
 * defined in {@link RmiService}, which it implements.
 */
public class RmiClient implements ContractorService {

  /** Global Logger. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The ContractorService object used to request a service. */
  private final ContractorService service;

  /**
   * Constructs a new RMI client which will request services from the RMI server with the specified
   * IP address and port number.
   *
   * @param host
   *          the host name or IP address of the RMI server.
   * @param portNumber
   *          the port number of the RMI server.
   * @throws RemoteException
   *           if an RMI communication-related exception occurs.
   */
  public RmiClient(final String host, final int portNumber) throws RemoteException {
    final Registry registry = LocateRegistry.getRegistry(host, portNumber);
    try {
      service = (ContractorService) registry.lookup(RMI_ID);
    } catch (final NotBoundException e) {
      throw new RemoteException(RMI_ID + " is not bound: ", e);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void book(final Contractor contractor) throws ContractorNotFoundException,
      AlreadyBookedException, RemoteException, IllegalArgumentException {
    if (contractor == null) {
      throw new IllegalArgumentException("Contractor cannot be null");
    }
    LOGGER.info(this.getClass().getSimpleName() + ": Attempting to book contractor: " + contractor.toString());
    service.book(contractor);
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Map<Integer, Contractor> find(final ContractorPk primaryKey)
      throws ContractorNotFoundException, RemoteException, IllegalArgumentException {
    if (primaryKey == null) {
      throw new IllegalArgumentException("ContractorPk cannot be null");
    }
    LOGGER.info(this.getClass().getSimpleName() + ": Attempting to find contractors with : " + primaryKey.toString());
    return service.find(primaryKey);
  }
}
