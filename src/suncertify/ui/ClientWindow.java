/*
 * ClientWindow.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

import static suncertify.util.Utils.isEightDigits;
import static suncertify.util.Constants.EMPTY_STRING;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;
import suncertify.util.ContractorBuilder;

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public final class ClientWindow extends AbstractWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final ContractorService service;
  private final ContractorTableModel model;
  private final ContractorTable table;
  private final JScrollPane scrollPane;
  private final String[] columnNames = { "Name", "Location", "Specialties", "Size", "Rate",
      "Customer ID" };
  private final JLabel nameLabel = new JLabel("Name: ");
  private final JLabel locationLabel = new JLabel("Location: ");
  private final JTextField nameField = new JTextField(20);
  private final JTextField locationField = new JTextField(20);
  private final JButton bookButton = new JButton("Book");
  private final JButton searchButton = new JButton("Search");
  private final JButton clearButton = new JButton("Clear");

  public ClientWindow(final ContractorService service) {
    super("Bodgitt & Scarper Booking System");
    this.service = service;
    model = new ContractorTableModel(columnNames, recordsToArrayArray(getAllRecords()));
    table = new ContractorTable(this, model);
    scrollPane = new JScrollPane(table);
    bookButton.addActionListener(action -> bookSelectedContractor());
    searchButton.addActionListener(action -> filterTableOnSearchValues());
    clearButton.addActionListener(action -> {
      updateTable(model, getAllRecords());
      nameField.setText(EMPTY_STRING);
      locationField.setText(EMPTY_STRING);
      scrollPane.setVisible(true);
    });
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
    searchPanel.add(clearButton, BorderLayout.EAST);
    getContentPane().add(searchPanel, BorderLayout.NORTH);

    pack();
    setVisible(true);
    bookButton.setVisible(false);
  }

  @Override
  public JPanel createContentPanel() {
    // TODO Auto-generated method stub
    return null;
  }

  public void hideButtonIfBooked() {
    final int rowIndex = table.getSelectedRows()[0];
    final String[] fieldValues = model.getRowFields(rowIndex);
    final Contractor contractor = ContractorBuilder.build(fieldValues);
    if (contractor.isBooked()) {
      bookButton.setVisible(false);
    } else {
      bookButton.setVisible(true);
    }
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

  public void updateTable(final ContractorModel model, final Map<Integer, Contractor> records) {
    model.updateData(recordsToArrayArray(records));
    bookButton.setVisible(false);
  }

  private void bookSelectedContractor() {
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
          updateRow(model, rowIndex, contractor);
          displayMessage("Contractor has been successfully booked!", "Booking Confirmation",
              JOptionPane.INFORMATION_MESSAGE);
        } catch (final RemoteException e) {
          handleFatalException("Could not connect to server. Closing application.", e);
        } catch (final ContractorNotFoundException e) {
          displayMessage("The selected contractor no longer exists.", "No Records Available",
              JOptionPane.WARNING_MESSAGE);
        } catch (final AlreadyBookedException e) {
          updateTable(model, getAllRecords());
          filterTableOnSearchValues();
          displayMessage("The selected contractor has already been booked.",
              "Contractor Not Available", JOptionPane.WARNING_MESSAGE);
        }
      } else {
        displayMessage("Customer ID must be an 8 digit number", "Invalid Input",
            JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  private void filterTableOnSearchValues() {
    final String name = getNameSearchValue();
    final String location = getLocationSearchValue();
    try {
      final ContractorPk primaryKey = new ContractorPk(name, location);
      final Map<Integer, Contractor> records = service.find(primaryKey);
      updateTable(model, records);
      scrollPane.setVisible(true);
    } catch (final RemoteException e) {
      handleFatalException("Could not connect to server. Closing application.", e);
    } catch (final ContractorNotFoundException e) {
      scrollPane.setVisible(false);
      displayMessage("Could not find contractor", "No Records Available", JOptionPane.WARNING_MESSAGE);
    }
  }

  private Map<Integer, Contractor> getAllRecords() {
    try {
      return service.find(new ContractorPk());
    } catch (final RemoteException e) {
      handleFatalException("Could not connect to server. Closing application.", e);
    } catch (final ContractorNotFoundException e) {
      displayMessage("The selected contractor no longer exists.", "No Records Available",
          JOptionPane.WARNING_MESSAGE);
    }
    return null;
  }

  private String getLocationSearchValue() {
    return locationField.getText().trim();
  }

  private String getNameSearchValue() {
    return nameField.getText().trim();
  }

  private void updateRow(final ContractorTableModel model, final int rowIndex,
      final Contractor contractor) {
    model.updateRow(rowIndex, contractor.toStringArray());
    bookButton.setVisible(false);
  }

}
