package suncertify.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
  public void displayWarningException(final Exception exception, final String title) {
    JOptionPane.showMessageDialog(this, exception.getMessage(), title,
        JOptionPane.WARNING_MESSAGE);
  }

  protected abstract JPanel createContentPanel();

  protected JButton getConfirmButton() {
    return confirmButton;
  }
}
