package suncertify.ui.view;

import suncertify.business.rmi.RmiServer;
import suncertify.db.DBMainExtended;
import suncertify.db.DatabaseException;
import suncertify.db.DatabaseFactory;
import suncertify.ui.DatabaseFileChooser;
import suncertify.util.Config;

import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class ServerConfigWindow extends AbstractConfigWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final JLabel portLabel = new JLabel("Server Port: ");
  private final JLabel dbLabel = new JLabel("Database file location: ");
  private final JTextField portField = new JTextField(20);
  private final JTextField dbField = new JTextField(20);
  private final JButton browseButton = new JButton("Browse");
  private final JFileChooser dbFileChooser = new DatabaseFileChooser();

  public ServerConfigWindow() {
    super("Server Configuration Settings");
    getContentPane().add(createContentPanel(), BorderLayout.NORTH);
    getConfirmButton().addActionListener(action -> {
      saveConfig();
      launch();
    });
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel configPane = new JPanel();
    dbField.setText(Config.getServerDBLocation());
    portField.setText(Config.getServerPortNumber());
    dbField.setToolTipText("The location of the database file on the file system.");
    portField.setToolTipText("The port number that the server will run on.");
    browseButton.setToolTipText("Click to browseButton file system for database file.");
    configPane.add(portLabel);
    configPane.add(portField);
    configPane.add(dbLabel);
    configPane.add(dbField);
    browseButton.addActionListener(action -> {
      final int state = dbFileChooser.showOpenDialog(configPane);
      if (state == JFileChooser.APPROVE_OPTION) {
        final String fileName = dbFileChooser.getSelectedFile().getAbsolutePath();
        dbField.setText(fileName);
      }
    });
    configPane.add(browseButton);
    return configPane;
  }

  @Override
  public void launch() {
    try {
      final DBMainExtended data = DatabaseFactory.getDatabase(Config.getServerDBLocation());
      final int portNumber = Integer.parseInt(Config.getServerPortNumber());
      new RmiServer(data).startServer(portNumber);
      dispose();
    } catch (DatabaseException | RemoteException e) {
      displayFatalException(e);
    }
  }

  @Override
  public void saveConfig() {
    try {
      Config.setServerDBLocation(dbField.getText().trim());
      Config.setServerPortNumber(portField.getText().trim());
      Config.saveProperties();
    } catch (final IllegalArgumentException e) {
      displayWarningException(e);
    }
  }
}
