/*
 * RmiServer.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business.rmi;

import static suncertify.util.Constants.RMI_ID;

import suncertify.business.BasicContractorService;
import suncertify.db.DBMainExtended;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Subclass of {@link BasicContractorService} and implements {@link RmiService}. Can be used to
 * start an RMI server on a specified port number and book/find contractors.
 */
public class RmiServer extends BasicContractorService implements RmiService {

  /**
   * Constructs a new RMI server instance with the specified data access object.
   *
   * @param data
   *          the data access object used to interact with the database.
   * @throws RemoteException
   *           if an RMI communication-related exception occurs.
   */
  public RmiServer(final DBMainExtended data) throws RemoteException {
    super(data);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startServer(final int port) throws RemoteException {
    final RmiService impl = (RmiService) UnicastRemoteObject.exportObject(this, 0);
    final Registry registry = LocateRegistry.createRegistry(port);
    registry.rebind(RMI_ID, impl);
  }
}
