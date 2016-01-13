/*
 * CliLauncher.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import suncertify.business.ServiceException;
import suncertify.ui.view.AbstractConfigWindow;
import suncertify.ui.view.ClientConfigWindow;
import suncertify.ui.view.ServerConfigWindow;
import suncertify.ui.view.StandaloneConfigWindow;

import java.rmi.RemoteException;

/**
 * The Class CliLauncher.
 */
public final class CliLauncher {

  /** The Constant NETWORKED. */
  private static final String NETWORKED = "server";

  /** The Constant STANDALONE. */
  private static final String STANDALONE = "alone";

  /** The Constant ZERO. */
  private static final int ZERO = 0;

  /** The Constant ONE. */
  private static final int ONE = 1;

  /**
   * Instantiates a new cli launcher.
   */
  private CliLauncher() {
  }

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws RemoteException the remote exception
   * @throws ServiceException the service exception
   */
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

  /**
   * Checks if is networked.
   *
   * @param mode the mode
   * @return true, if is networked
   */
  private static boolean isNetworked(final String mode) {
    return mode.equals(NETWORKED);
  }

  /**
   * Checks if is non networked.
   *
   * @param mode the mode
   * @return true, if is non networked
   */
  private static boolean isNonNetworked(final String mode) {
    return mode.equals(STANDALONE);
  }

  /**
   * Run client.
   */
  private static void runClient() {
    final AbstractConfigWindow clientConfig = new ClientConfigWindow();
    clientConfig.setVisible(true);
  }

  /**
   * Run server.
   */
  private static void runServer() {
    final AbstractConfigWindow clientConfig = new ServerConfigWindow();
    clientConfig.setVisible(true);
  }

  /**
   * Run standalone.
   */
  private static void runStandalone() {
    final AbstractConfigWindow standaloneConfig = new StandaloneConfigWindow();
    standaloneConfig.setVisible(true);
  }
}
