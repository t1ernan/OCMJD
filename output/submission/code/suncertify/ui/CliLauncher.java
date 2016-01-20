/*
 * CliLauncher.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import static suncertify.util.Utils.initializeLogger;

import java.util.logging.Level;

/**
 * The class CliLauncher is responsible for launching the application using a command line
 * interface. It contains the only main method in the application which will launch the application
 * in a specific mode depending on the command line argument specified, if it is not left out
 * entirely. Class marked as final to prevent overriding.
 */
public final class CliLauncher {

  /** The mode flag value which indicates the server program should run. */
  private static final String NETWORKED = "server";

  /** The mode flag value which indicates the standalone client and GUI should run. */
  private static final String STANDALONE = "alone";

  /** The constant ZERO. */
  private static final int ZERO = 0;

  /** The constant ONE. */
  private static final int ONE = 1;

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private CliLauncher() {
  }

  /**
   * The application's only main method. It takes either one argument, the mode flag, or no
   * arguments.<ul>
   * <li>If no argument is specified, the network client and GUI will run.</li>
   * <li>If the argument "server" is specified, the server program will run.</li>
   * <li>If the argument "alone" is specified, the client and GUI will run in
   * standalone/non-networked mode.</li></ul>
   *
   * @param args
   *          the arguments
   * @throws IllegalArgumentException
   *           if more than one argument is specified or if the argument specified does not equal
   *           "alone" or "server".
   */
  public static void main(final String[] args) throws IllegalArgumentException {
    initializeLogger(Level.ALL);

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
   * Returns true if is specified {@code modeFlag} equals "server", the argument used to run the
   * application's RMI server in Networked mode.
   *
   * @param modeFlag
   *          the mode the application should run in.
   * @return true, if specified {@code modeFlag} equals "alone"
   */
  private static boolean isNetworked(final String modeFlag) {
    return modeFlag.equals(NETWORKED);
  }

  /**
   * Returns true if is specified {@code modeFlag} equals "alone", the argument used to run the
   * application client in non-Networked mode.
   *
   * @param modeFlag
   *          the mode the application should run in.
   * @return true, if specified {@code modeFlag} equals "alone"
   */
  private static boolean isNonNetworked(final String modeFlag) {
    return modeFlag.equals(STANDALONE);
  }

  /**
   * Launches the application configuration window for the client in Networked Mode.
   */
  private static void runClient() {
    final AbstractWindow clientConfig = new ClientConfigWindow();
    clientConfig.setVisible(true);
  }

  /**
   * Launches the application configuration window for the server in Networked Mode.
   */
  private static void runServer() {
    final AbstractWindow serverConfig = new ServerConfigWindow();
    serverConfig.setVisible(true);
  }

  /**
   * Launches the application configuration window for the client in non-Networked Mode.
   */
  private static void runStandalone() {
    final AbstractWindow standaloneConfig = new StandaloneConfigWindow();
    standaloneConfig.setVisible(true);
  }
}
