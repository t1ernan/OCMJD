/*
 * AbstractWindow.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import static suncertify.ui.Messages.CLOSING_APPLICATION_MESSAGE;
import static suncertify.ui.Messages.EXIT_MENU_ITEM_TEXT;
import static suncertify.ui.Messages.FATAL_EXCEPTION_MESSAGE_TITLE;
import static suncertify.ui.Messages.OPTIONS_MENU_TEXT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * AbstractWindow is the common superclass of all frames used in the application and a subclass of
 * {@link JFrame}. It ensures that all frames that extends it have the same common behavior and
 * style, ensuring the application's UI feels consistent to the user and making the code more
 * maintainable and reusable.
 */
public abstract class AbstractWindow extends JFrame implements WindowManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The Global Logger used to log exceptions to the console. */
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /** The menu bar. */
  private final JMenuBar menuBar = new JMenuBar();

  /** The menu. */
  private final JMenu menu = new JMenu(OPTIONS_MENU_TEXT);

  /** The exit menu item. */
  private final JMenuItem exitMenuItem = new JMenuItem(EXIT_MENU_ITEM_TEXT);

  /**
   * Sole constructor(For invocation by subclass constructors) with specified the {@code title}.
   *
   * @param title
   *          the title of the JFrame
   */
  public AbstractWindow(final String title) {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY);
    exitMenuItem.addActionListener(action -> dispose());
    menu.add(exitMenuItem);
    menuBar.add(menu);
    setJMenuBar(menuBar);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayMessage(final String message, final String title) {
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleException(final String errorMessage, final String title,
      final Exception exception) {
    LOGGER.log(Level.INFO, errorMessage, exception);
    JOptionPane.showMessageDialog(this, errorMessage, title, JOptionPane.WARNING_MESSAGE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleFatalException(final String errorMessage, final Exception exception) {
    LOGGER.log(Level.SEVERE, errorMessage, exception);
    JOptionPane.showMessageDialog(this, errorMessage + CLOSING_APPLICATION_MESSAGE,
        FATAL_EXCEPTION_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
    dispose();
  }

}
