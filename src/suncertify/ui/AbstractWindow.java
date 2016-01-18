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

public abstract class AbstractWindow extends JFrame implements WindowManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private final JMenuBar menuBar = new JMenuBar();
  private final JMenu menu = new JMenu(OPTIONS_MENU_TEXT);
  private final JMenuItem exitMenuItem = new JMenuItem(EXIT_MENU_ITEM_TEXT);

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

  @Override
  public void displayMessage(final String message, final String title) {
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void handleException(final String errorMessage, final String title,
      final Exception exception) {
    LOGGER.log(Level.WARNING, errorMessage, exception);
    JOptionPane.showMessageDialog(this, errorMessage, title, JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void handleFatalException(final String errorMessage, final Exception exception) {
    LOGGER.log(Level.SEVERE, errorMessage, exception);
    JOptionPane.showMessageDialog(this, errorMessage + CLOSING_APPLICATION_MESSAGE,
        FATAL_EXCEPTION_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
    dispose();
  }

}
