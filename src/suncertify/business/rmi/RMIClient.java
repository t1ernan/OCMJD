package suncertify.business.rmi;

import static suncertify.util.Constants.RMI_ID;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

/**
 * An RMI client used to connect and request data from a remote RMI server
 * through the business methods defined in {@link RMIService}, which it
 * implements.
 */
public class RMIClient implements ContractorService {

	/** The remote server instance. */
	private final ContractorService service;

	/**
	 * Constructs a new RMI client which will request services from the server
	 * with the specified IP address and port number.
	 *
	 * @param serverIPAddress
	 *            the server ip address
	 * @param portNumber
	 *            the port number
	 * @throws RemoteException
	 *             the remote exception
	 * @throws NotBoundException
	 *             the not bound exception
	 */
	public RMIClient(final String serverIPAddress, final int portNumber) throws RemoteException, NotBoundException {
		final Registry registry = LocateRegistry.getRegistry(serverIPAddress, portNumber);
		service = (ContractorService) registry.lookup(RMI_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void book(Contractor contractor)
			throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
		service.book(contractor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws ContractorNotFoundException, RemoteException {
		return service.find(primaryKey);
	}

}
