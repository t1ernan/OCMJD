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

/**
 * An rmi client used to connect and request data from a rmi server through the business methods
 * defined in {@link RmiService}, which it implements.
 */
public class RmiClient implements ContractorService {

  /** The remote server instance. */
  private final ContractorService service;

  /**
   * Constructs a new rmi client which will request services from the rmi server with the specified
   * IP address and port number.
   *
   * @param serverIpAddress
   *          the server ip address of the rmi server
   * @param portNumber
   *          the port number of the rmi server
   * @throws RemoteException
   *           if an RMI communication-related exception occurs
   */
  public RmiClient(final String serverIpAddress, final int portNumber) throws RemoteException {
    final Registry registry = LocateRegistry.getRegistry(serverIpAddress, portNumber);
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
  public void book(final Contractor contractor)
      throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
    service.book(contractor);
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Map<Integer, Contractor> find(final ContractorPk primaryKey)
      throws ContractorNotFoundException, RemoteException {
    return service.find(primaryKey);
  }
}
