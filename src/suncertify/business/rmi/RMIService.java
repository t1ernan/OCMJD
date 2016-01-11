package suncertify.business.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.business.ContractorService;

/**
 * An extension of {@link ContractorService} and {@link Remote} interfaces. It's
 * the common interface for any classes in the service layer which start an RMI
 * server instance.
 */
public interface RMIService extends ContractorService, Remote {

	/**
	 * Start the RMI server on the specified port number.
	 *
	 * @param portNumber
	 *            the number of the port the server will be active on.
	 * @throws RemoteException
	 *             if an RMI communication-related exception occurs.
	 */
	void startServer(int portNumber) throws RemoteException;

}
