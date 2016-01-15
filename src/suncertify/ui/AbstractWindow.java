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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public abstract class AbstractWindow extends JFrame implements WindowManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;
  
  private final JMenuBar menuBar = new JMenuBar();
  private final JMenu menu = new JMenu("Options");
  private final JMenuItem exitMenuItem = new JMenuItem("Exit");

  public AbstractWindow(final String title) {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY);
    exitMenuItem.addActionListener(action -> dispose());
    menu.add(exitMenuItem);
    menuBar.add(menu);
    this.setJMenuBar(menuBar);
  }

  @Override
  public void displayFatalException(final Exception exception) {
    final String errorMessage = "Failed to launch application: " + exception.getMessage();
    JOptionPane.showMessageDialog(this, errorMessage, "System Error", JOptionPane.ERROR_MESSAGE);
    dispose();
  }

  @Override
  public void displayMessage(final String message, final String title,final int messageType) {
    JOptionPane.showMessageDialog(this, message, title,
        messageType);
  }

}
