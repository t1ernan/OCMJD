package suncertify.ui.view;

import static suncertify.util.Utils.isEightDigits;

import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;
import suncertify.ui.model.ContractorTable;
import suncertify.ui.model.ContractorTableModel;
import suncertify.util.ContractorBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public final class MainWindow extends JFrame {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final ContractorService service;
  // private final ContractorTableModel tableModel;
  // private final JTable table;
  private final String[] columnNames = { "Name", "Location", "Specialties", "Size", "Rate",
      "Customer ID" };
  private final JLabel nameLabel = new JLabel("Name: ");
  private final JLabel locationLabel = new JLabel("Location: ");
  private final JTextField nameField = new JTextField(20);
  private final JTextField locationField = new JTextField(20);

  public MainWindow(final ContractorService service) {
    super("Bodgitt & Scarper Booking System");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY);
    this.service = service;
    final ContractorTableModel tableModel = new ContractorTableModel(columnNames, getAllRecords());
    final JButton bookButton = new JButton("Book");
    final JButton searchButton = new JButton("Search");
    final ContractorTable table = new ContractorTable(tableModel, bookButton);
    final JScrollPane scrollPane = new JScrollPane(table);
    bookButton.addActionListener(action -> bookButtonAction(table));
    searchButton.addActionListener(action -> searchButtonAction(tableModel));
    final JPanel bookPanel = new JPanel();
    bookPanel.add(scrollPane, BorderLayout.NORTH);
    bookPanel.add(bookButton, BorderLayout.SOUTH);
    getContentPane().add(bookPanel, BorderLayout.NORTH);

    final JPanel searchPanel = new JPanel();
    searchPanel.add(nameLabel, BorderLayout.WEST);
    searchPanel.add(nameField);
    searchPanel.add(locationLabel);
    searchPanel.add(locationField);
    searchPanel.add(searchButton, BorderLayout.EAST);
    getContentPane().add(searchPanel, BorderLayout.SOUTH);

    pack();
    setVisible(true);
  }

  public void updateView(final ContractorTableModel model, final Map<Integer, Contractor> records) {
    model.updateData(records);
  }

  private void bookButtonAction(final JTable table) {
    final int rowIndex = table.getSelectedRows()[0];
    final ContractorTableModel model = (ContractorTableModel) table.getModel();
    final String[] fieldValues = model.getRowFields(rowIndex);
    final String customerId = JOptionPane.showInputDialog("Please enter the customer ID number.");
    if (isEightDigits(customerId)) {
      try {
        final Contractor contractor = ContractorBuilder.build(fieldValues);
        contractor.setCustomerId(customerId);
        service.book(contractor);
        updateView(model, getAllRecords());
      } catch (final Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "System Error",
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Invalid customer ID: Must be an eight digit number",
          "Invalid user input", JOptionPane.WARNING_MESSAGE);
    }
  }

  private Map<Integer, Contractor> getAllRecords() {
    try {
      return service.find(new ContractorPk("", ""));
    } catch (final ContractorNotFoundException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "System Error",
          JOptionPane.ERROR_MESSAGE);
    } catch (final RemoteException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "System Error",
          JOptionPane.ERROR_MESSAGE);
    }
    return null;
  }

  private void searchButtonAction(final ContractorTableModel model) {
    try {
      final String name = nameField.getText().trim();
      final String location = locationField.getText().trim();
      final ContractorPk primaryKey = new ContractorPk(name, location);
      final Map<Integer, Contractor> records = service.find(primaryKey);
      updateView(model, records);
    } catch (final Exception e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "System Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
