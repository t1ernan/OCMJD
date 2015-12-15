package suncertify.business;

import java.rmi.RemoteException;

import suncertify.db.Data;

public class RMIContractorServices extends BasicContractorServices implements RemoteContractorServices {

	public RMIContractorServices(Data databaseManager) {
		super(databaseManager);
	}

	@Override
	public void startServer(int port) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
