package suncertify.business;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public interface RemoteContractorServices extends Remote, ContractorServices {

	public abstract void startServer(int port) throws RemoteException;

	@Override
	public void createBooking(Contractor contractor) throws ServicesException, RemoteException;

	@Override
	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws ServicesException, RemoteException;
}
