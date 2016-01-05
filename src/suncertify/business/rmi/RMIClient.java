package suncertify.business.rmi;

import static suncertify.util.Constants.RMI_ID;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import suncertify.business.ContractorServices;
import suncertify.business.ServiceException;
import suncertify.dto.Contractor;
import suncertify.dto.ContractorPK;
import suncertify.util.Config;

public class RMIClient implements ContractorServices {

	private final ContractorServices service;
	private final Config config = Config.getInstance();

	public RMIClient() throws RemoteException, NotBoundException {
		final Registry registry = LocateRegistry.getRegistry(config.getServerIPAddress(), config.getPortNumber());
		service = (ContractorServices) registry.lookup(RMI_ID);

	}

	@Override
	public void book(Contractor contractor) throws ServiceException, RemoteException {
		service.book(contractor);

	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws ServiceException, RemoteException {
		return service.find(primaryKey);
	}

}
