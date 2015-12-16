package suncertify.cli;

import static suncertify.util.Constants.DB_FILE_PATH;
import static suncertify.util.Constants.RMI_PORT;

import java.rmi.RemoteException;

import suncertify.business.ServicesException;
import suncertify.db.DAOFactory;
import suncertify.rmi.RMIServer;
import suncertify.rmi.RMIServices;

public class CliLauncher {

	private static final String NETWORKED = "server";
	private static final String STANDALONE = "alone";
	private final static int ZERO = 0;
	private final static int ONE = 1;

	public static void main(String[] args) throws RemoteException, ServicesException {

		final int NUM_OF_ARGS = args.length;

		switch (NUM_OF_ARGS) {

		case ZERO:
			runClient();
			break;

		case ONE:
			final String mode = args[0];
			if (isNetworked(mode)) {
				runServer();
			} else if (isNonNetworked(mode)) {
				runStandalone();
			} else {
				throw new IllegalArgumentException("Argument " + mode + " is invalid: [server|alone]");
			}
			break;

		default:
			throw new IllegalArgumentException("Too many arguments, 1 argument maximum");
		}

	}

	private static void runClient() {
		// TODO Auto-generated method stub

	}

	private static void runServer() throws RemoteException, ServicesException {
		RMIServices service = new RMIServer(DAOFactory.getDbManager(DB_FILE_PATH));
		service.startServer(RMI_PORT);
	}

	private static void runStandalone() {
		// TODO Auto-generated method stub

	}

	private static boolean isNonNetworked(final String mode) {
		return mode.equals(STANDALONE);
	}

	private static boolean isNetworked(final String mode) {
		return mode.equals(NETWORKED);
	}
}
