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
import suncertify.dto.Contractor;
import suncertify.dto.ContractorPK;
import suncertify.util.Config;

public class RMIClient implements ContractorService {

	private final ContractorService service;
	private final Config config = Config.getInstance();

	public RMIClient() throws RemoteException, NotBoundException {
		final Registry registry = LocateRegistry.getRegistry(config.getServerIPAddress(), config.getPortNumber());
		service = (ContractorService) registry.lookup(RMI_ID);
	}

	@Override
	public void book(Contractor contractor) throws ContractorNotFoundException, AlreadyBookedException, RemoteException {
		service.book(contractor);
	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws ContractorNotFoundException, RemoteException {
		return service.find(primaryKey);
	}

}
