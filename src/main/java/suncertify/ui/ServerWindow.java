/*
 * ServerWindow.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import static suncertify.ui.Messages.*;

import suncertify.util.Config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class ServerWindow is direct subclass of {@link AbstractWindow} and provides a very simple
 * GUI for the RMI server. A user can use this GUI to shutdown the server if need be.
 */
public final class ServerWindow extends AbstractWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final int portNumber;
  
  /** The content panel. */
  private JPanel contentPanel;

  /** The button used to stop server. */
  private final JButton stopButton = new JButton(STOP_BUTTON_TEXT);

  /** The label displaying the status of the server. */
  private final JLabel serverStatusLabel = new JLabel();

  /**
   * Constructs a new server window.
   * 
   * @param portNumber
   *          the port number the server is listening on.
   */
  public ServerWindow(int portNumber) {
    super(SERVER_WINDOW_TITLE);
    this.portNumber=portNumber;
    setSize(new Dimension(460, 192));
    setMinimumSize(new Dimension(460, 192));
    initializeComponents();
    getContentPane().add(contentPanel);
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    final GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.ipady = 7;
    constraints.weighty = 0.1;
    constraints.anchor = GridBagConstraints.CENTER;
    panel.add(serverStatusLabel, constraints);
    constraints.gridx = 0;
    constraints.gridy = 1;
    panel.add(stopButton, constraints);
    return panel;
  }

  @Override
  public void initializeComponents() {
    contentPanel = createContentPanel();
    contentPanel.setBorder(BorderFactory.createTitledBorder(SERVER_PANEL_TITLE));
    final String serverStatusMessage = SERVER_STATUS_MESSAGE_FORMAT + portNumber;
    serverStatusLabel.setText(serverStatusMessage);
    stopButton.setToolTipText(CONFIRM_BUTTON_TOOLTIP_TEXT);
    stopButton.addActionListener(action -> shutdown());
    exitMenuItem.addActionListener(action -> shutdown());
    this.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        shutdown();
      }
    });
  }

  /**
   * Closes the window and shuts down the server.
   */
  private void shutdown() {
    dispose();
    System.exit(0);
  }

}
