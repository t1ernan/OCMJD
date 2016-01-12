package suncertify.business.rmi;

import suncertify.business.ContractorService;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An extension of {@link ContractorService} and {@link Remote} interfaces. It's the common
 * interface for any classes in the service layer which start an rmi server instance.
 */
public interface RmiService extends ContractorService, Remote {

  /**
   * Start the rmi server on the specified port number.
   *
   * @param portNumber
   *          the number of the port the server will be active on.
   * @throws RemoteException
   *           if an rmi communication-related exception occurs.
   */
  void startServer(int portNumber) throws RemoteException;

}
