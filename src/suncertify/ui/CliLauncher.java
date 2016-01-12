package suncertify.ui;

import suncertify.business.ServiceException;
import suncertify.ui.view.ClientConfigWindow;
import suncertify.ui.view.AbstractConfigWindow;
import suncertify.ui.view.ServerConfigWindow;
import suncertify.ui.view.StandaloneConfigWindow;

import java.rmi.RemoteException;

public final class CliLauncher {

  private static final String NETWORKED = "server";
  private static final String STANDALONE = "alone";
  private static final int ZERO = 0;
  private static final int ONE = 1;

  public static void main(final String[] args) throws RemoteException, ServiceException {
    final int numberOfArgs = args.length;

    switch (numberOfArgs) {

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
    final AbstractConfigWindow clientConfig = new ClientConfigWindow();
    clientConfig.setVisible(true);
  }

  private static void runServer() {
    final AbstractConfigWindow clientConfig = new ServerConfigWindow();
    clientConfig.setVisible(true);
  }

  private static void runStandalone() {
    final AbstractConfigWindow standaloneConfig = new StandaloneConfigWindow();
    standaloneConfig.setVisible(true);
  }

  private CliLauncher() {
  }
}
