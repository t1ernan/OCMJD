package suncertify.ui.view;

import static suncertify.util.Utils.isEightDigits;

import suncertify.business.AlreadyBookedException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public final class MainWindow extends JFrame implements DisplayManager {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final ContractorService service;
  // private final ContractorTableModel tableModel;
  private final ContractorTable table;
  private final String[] columnNames = { "Name", "Location", "Specialties", "Size", "Rate",
      "Customer ID" };
  private final JLabel nameLabel = new JLabel("Name: ");
  private final JLabel locationLabel = new JLabel("Location: ");
  private final JTextField nameField = new JTextField(20);
  private final JTextField locationField = new JTextField(20);
  private final JButton bookButton = new JButton("Book");

  public MainWindow(final ContractorService service) {
    super("Bodgitt & Scarper Booking System");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.LIGHT_GRAY);
    this.service = service;
    final ContractorTableModel tableModel = new ContractorTableModel(columnNames,
        recordsToArrayArray(getAllRecords()));
    final JButton searchButton = new JButton("Search");
    table = new ContractorTable(tableModel, bookButton);
    table.setRowSelectionInterval(0, 0);
    final JScrollPane scrollPane = new JScrollPane(table);
    bookButton.addActionListener(action -> bookButtonAction(table));
    searchButton.addActionListener(action -> searchButtonAction(tableModel));
    final JPanel bookPanel = new JPanel();
    bookPanel.add(scrollPane, BorderLayout.WEST);
    bookPanel.add(bookButton, BorderLayout.EAST);
    getContentPane().add(bookPanel, BorderLayout.SOUTH);

    final JPanel searchPanel = new JPanel();
    searchPanel.add(nameLabel, BorderLayout.WEST);
    searchPanel.add(nameField);
    searchPanel.add(locationLabel);
    searchPanel.add(locationField);
    searchPanel.add(searchButton, BorderLayout.EAST);
    getContentPane().add(searchPanel, BorderLayout.NORTH);

    pack();
    setVisible(true);
  }

  @Override
  public void displayFatalException(final Exception exception) {
    JOptionPane.showMessageDialog(this, exception.getMessage(), "System Error",
        JOptionPane.ERROR_MESSAGE);

  }

  @Override
  public void displayWarningException(final Exception exception, final String title) {
    JOptionPane.showMessageDialog(this, exception.getMessage(), title, JOptionPane.WARNING_MESSAGE);

  }

  public String[][] recordsToArrayArray(final Map<Integer, Contractor> records) {
    final List<String[]> list = new ArrayList<>();
    for (final Contractor contractor : records.values()) {
      list.add(contractor.toStringArray());
    }
    final String[][] array = new String[list.size()][];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  public void updateCell(final ContractorTableModel model, final int row, final int column,final String cell){
    model.updateCell(row, column, cell);
  }

  public void updateTable(final ContractorTableModel model, final Map<Integer, Contractor> records) {
    model.updateData(recordsToArrayArray(records));
    if(records.isEmpty()){
      bookButton.setVisible(false);
    }else{
      bookButton.setVisible(true);
      table.setRowSelectionInterval(0, 0);
    }
  }

  private void bookButtonAction(final JTable table) {
    final int rowIndex = table.getSelectedRows()[0];
    final ContractorTableModel model = (ContractorTableModel) table.getModel();
    final String[] fieldValues = model.getRowFields(rowIndex);
    final Optional<String> customerId = Optional
        .ofNullable(JOptionPane.showInputDialog("Please enter the customer ID number."));
    if (customerId.isPresent()) {
      if (isEightDigits(customerId.get())) {
        try {
          final Contractor contractor = ContractorBuilder.build(fieldValues);
          contractor.setCustomerId(customerId.get());
          service.book(contractor);
          JOptionPane.showMessageDialog(this, "Contractor has successfully been booked!",
              "Confirmation", JOptionPane.INFORMATION_MESSAGE);
          updateTable(model, getAllRecords());
        } catch (final RemoteException e) {
          displayFatalException(e);
        } catch (final ContractorNotFoundException e) {
          displayWarningException(e, "No results");
        } catch (final AlreadyBookedException e) {
          displayWarningException(e, "Not available");
        }
      } else {
        JOptionPane.showMessageDialog(this, "Customer ID must be an 8 digit number",
            "Invalid Input", JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  private Map<Integer, Contractor> getAllRecords() {
    try {
      return service.find(new ContractorPk("", ""));
    } catch (final RemoteException e) {
      displayFatalException(e);
    } catch (final ContractorNotFoundException e) {
      displayWarningException(e, "No results");
    }
    return null;
  }

  private void searchButtonAction(final ContractorTableModel model) {
    try {
      final String name = nameField.getText().trim();
      final String location = locationField.getText().trim();
      final ContractorPk primaryKey = new ContractorPk(name, location);
      final Map<Integer, Contractor> records = service.find(primaryKey);
      updateTable(model, records);
    } catch (final RemoteException e) {
      displayFatalException(e);
    } catch (final ContractorNotFoundException e) {
      updateTable(model, new HashMap<Integer, Contractor>());
      displayWarningException(e, "No results");
    }
  }
}
