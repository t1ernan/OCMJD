package suncertify.cli;

import java.rmi.RemoteException;

import suncertify.business.ServiceException;

public class CliLauncher {

	private static final String NETWORKED = "server";
	private static final String STANDALONE = "alone";
	private final static int ZERO = 0;
	private final static int ONE = 1;

	public static void main(String[] args) throws RemoteException, ServiceException {
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
		// new ClientConfigWindow();
	}

	private static void runServer() throws RemoteException, ServiceException {
		// new ServerConfigWindow();
	}

	private static void runStandalone() {
		// new StandaloneConfigWindow();
	}

	private static boolean isNonNetworked(final String mode) {
		return mode.equals(STANDALONE);
	}

	private static boolean isNetworked(final String mode) {
		return mode.equals(NETWORKED);
	}
}
