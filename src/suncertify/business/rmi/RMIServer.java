package suncertify.business.rmi;

import static suncertify.util.Constants.RMI_ID;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.business.BasicService;
import suncertify.db.DBMain;
import suncertify.util.Config;

public class RMIServer extends BasicService implements RMIService {

	private final Config config = Config.getInstance();
	
	public RMIServer(DBMain data) throws RemoteException {
		super(data);
	}

	@Override
	public void startServer(int port) throws RemoteException {
		final RMIService impl = (RMIService) UnicastRemoteObject.exportObject(this, 0);
		final Registry registry = LocateRegistry.createRegistry(config.getPortNumber());
		registry.rebind(RMI_ID, impl);
		System.out.println("Server is running on port " + port);
	}
}
