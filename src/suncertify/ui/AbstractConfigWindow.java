/*
 * AbstractConfigWindow.java  1.0  14-Jan-2016
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class AbstractConfigWindow extends JFrame implements LaunchManager,DisplayManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final JButton confirmButton = new JButton("Confirm");

  public AbstractConfigWindow(final String title) {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY);
    confirmButton.setToolTipText("Click to save configuration settings and start application");
    getContentPane().add(confirmButton, BorderLayout.SOUTH);
  }

  @Override
  public void displayFatalException(final Exception exception) {
    final String errorMessage = "Failed to launch application: " + exception.getMessage();
    JOptionPane.showMessageDialog(this, errorMessage, "System Error", JOptionPane.ERROR_MESSAGE);
    dispose();
  }

  @Override
  public void displayMessage(final String message, final String title) {
    JOptionPane.showMessageDialog(this, message, title,
        JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void exit() {
    // TODO Auto-generated method stub

  }

  protected JButton getConfirmButton() {
    return confirmButton;
  }
}
