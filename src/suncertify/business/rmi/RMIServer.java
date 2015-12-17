package suncertify.business.rmi;

import static suncertify.util.Constants.RMI_ID;
import static suncertify.util.Constants.RMI_PORT;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.business.BasicServiceImpl;
import suncertify.db.Data;

public class RMIServer extends BasicServiceImpl implements RMIServices {

	public RMIServer(Data databaseManager) throws RemoteException {
		super(databaseManager);
	}

	@Override
	public void startServer(int port) throws RemoteException {
		final RMIServices impl = (RMIServices) UnicastRemoteObject.exportObject(this, 0);
		final Registry registry = LocateRegistry.createRegistry(RMI_PORT);
		registry.rebind(RMI_ID, impl);
		System.out.println("Server is running on port " + port);
	}
}
