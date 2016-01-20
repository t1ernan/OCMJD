/*
 * RmiService.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business.rmi;

import suncertify.business.ContractorService;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An extension of the {@link ContractorService} and {@link Remote} interfaces. It's the common
 * interface for any classes in the service layer which need to start an RMI server instance and
 * query the database.
 */
public interface RmiService extends ContractorService, Remote {

  /**
   * Start the RMI server on the specified port number.
   *
   * @param portNumber
   *          the number of the port the server will be active on.
   * @throws RemoteException
   *           if an RMI communication-related exception occurs.
   */
  void startServer(int portNumber) throws RemoteException;

}
