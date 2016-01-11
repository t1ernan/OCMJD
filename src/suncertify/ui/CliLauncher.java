package suncertify.ui;

import java.rmi.RemoteException;

import suncertify.business.ServiceException;
import suncertify.ui.view.ClientConfigWindow;
import suncertify.ui.view.ConfigWindow;
import suncertify.ui.view.ServerConfigWindow;
import suncertify.ui.view.StandaloneConfigWindow;

public final class CliLauncher {

	private static final String NETWORKED = "server";
	private static final String STANDALONE = "alone";
	private static final int ZERO = 0;
	private static final int ONE = 1;

	private CliLauncher(){

	}

	public static void main(final String[] args) throws RemoteException, ServiceException {
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

	private static boolean isNetworked(final String mode) {
		return mode.equals(NETWORKED);
	}

	private static boolean isNonNetworked(final String mode) {
		return mode.equals(STANDALONE);
	}

	private static void runClient() {
		final ConfigWindow clientConfig = new ClientConfigWindow();
		clientConfig.setVisible(true);
	}

	private static void runServer(){
		final ConfigWindow clientConfig = new ServerConfigWindow();
		clientConfig.setVisible(true);
	}

	private static void runStandalone() {
		final ConfigWindow standaloneConfig = new StandaloneConfigWindow();
		standaloneConfig.setVisible(true);
	}
}
