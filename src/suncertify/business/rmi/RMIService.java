package suncertify.business.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.business.ContractorService;
import suncertify.business.ServiceException;

public interface RMIService extends ContractorService, Remote {

	public void startServer(int port) throws ServiceException, RemoteException;

}
