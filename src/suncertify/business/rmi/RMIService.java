package suncertify.business.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.business.ContractorServices;
import suncertify.business.ServiceException;

public interface RMIServices extends ContractorServices, Remote {

	public void startServer(int port) throws ServiceException, RemoteException;

}
