package suncertify.rmi;

import static suncertify.util.Constants.RMI_HOSTNAME;
import static suncertify.util.Constants.RMI_ID;
import static suncertify.util.Constants.RMI_PORT;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import suncertify.business.ContractorServices;
import suncertify.business.ServicesException;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public class RMIClient implements ContractorServices {

	private ContractorServices service;

	public RMIClient() throws RemoteException, NotBoundException {
		final Registry registry = LocateRegistry.getRegistry(RMI_HOSTNAME, RMI_PORT);
		service = (ContractorServices) registry.lookup(RMI_ID);

	}

	@Override
	public void createBooking(Contractor contractor) throws ServicesException, RemoteException {
		service.createBooking(contractor);

	}

	@Override
	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws ServicesException, RemoteException {
		return service.find(primaryKey);
	}

}
