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

import static suncertify.ui.Messages.ACTIONS_BORDER_TITLE;
import static suncertify.ui.Messages.BOOK_BUTTON_TEXT;
import static suncertify.ui.Messages.BOOK_BUTTON_TOOLTIP_TEXT;
import static suncertify.ui.Messages.CLEAR_BUTTON_TEXT;
import static suncertify.ui.Messages.CLEAR_BUTTON_TOOLTIP_TEXT;
import static suncertify.ui.Messages.CONTRACTOR_ALREADY_BOOKED_EXCEPTION_MESSAGE_TITLE;
import static suncertify.ui.Messages.CONTRACTOR_BOOKED_MESSAGE_TEXT;
import static suncertify.ui.Messages.CONTRACTOR_BOOKED_MESSAGE_TITLE;
import static suncertify.ui.Messages.CONTRACTOR_NOT_FOUND_EXCEPTION_MESSAGE_TITLE;
import static suncertify.ui.Messages.CUSTOMER_ID_PROMPT_TEXT;
import static suncertify.ui.Messages.INVALID_CUSTOMER_ID_MESSAGE_TEXT;
import static suncertify.ui.Messages.INVALID_INPUT_MESSAGE_TITLE;
import static suncertify.ui.Messages.LOCATION_LABEL_TEXT;
import static suncertify.ui.Messages.LOCATION_TOOLTIP_TEXT;
import static suncertify.ui.Messages.NAME_LABEL_TEXT;
import static suncertify.ui.Messages.NAME_TOOLTIP_TEXT;
import static suncertify.ui.Messages.REMOTE_EXCEPTION_MESSAGE_TEXT;
import static suncertify.ui.Messages.SEARCH_BUTTON_TEXT;
import static suncertify.ui.Messages.SEARCH_BUTTON_TOOLTIP_TEXT;
import static suncertify.ui.Messages.SYSTEM_NAME;
import static suncertify.util.Constants.DEFAULT_TEXTFIELD_SIZE;
import static suncertify.util.Constants.EMPTY_STRING;
import static suncertify.util.Utils.isEightDigits;

import suncertify.business.AlreadyBookedException;
import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;
import suncertify.util.ContractorBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

public final class ClientWindow extends AbstractWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final ContractorService service;
  private ContractorModel model;
  private JTable table;
  private JScrollPane scrollPane;
  private final String[] columnNames = { "Name", "Location", "Specialties", "Size", "Rate",
      "Customer ID" };
  private final JLabel nameLabel = new JLabel(NAME_LABEL_TEXT);
  private final JLabel locationLabel = new JLabel(LOCATION_LABEL_TEXT);
  private final JTextField nameField = new JTextField(DEFAULT_TEXTFIELD_SIZE);
  private final JTextField locationField = new JTextField(DEFAULT_TEXTFIELD_SIZE);
  private final JButton bookButton = new JButton(BOOK_BUTTON_TEXT);
  private final JButton searchButton = new JButton(SEARCH_BUTTON_TEXT);
  private final JButton clearButton = new JButton(CLEAR_BUTTON_TEXT);
  private JPanel contentPanel;

  public ClientWindow(final ContractorService service) {
    super(SYSTEM_NAME);
    this.service = service;
    initializeComponents();
    setPreferredSize(new Dimension(775, 650));
    setMinimumSize(new Dimension(650, 220));
    getContentPane().add(contentPanel);
    pack();
  }

  @Override
  public JPanel createContentPanel() {
    final JPanel panel = new JPanel(new BorderLayout());
    panel.add(createSearchPanel(), BorderLayout.NORTH);
    panel.add(createActionsPanel(), BorderLayout.WEST);
    panel.add(createResultsPanel(), BorderLayout.CENTER);
    return panel;
  }

  public void enableOrDisableBookButton(final int rowIndex) {
    bookButton.setEnabled(shouldBookButtonBeEnabled(rowIndex));
  }

  public TableModel getTableModel() {
    return model;
  }

  @Override
  public void initializeComponents() {
    model = new ContractorTableModel(columnNames, recordsToArrayArray(getAllRecords()));
    table = new ContractorTable(this);
    scrollPane = new JScrollPane(table);
    bookButton.setToolTipText(BOOK_BUTTON_TOOLTIP_TEXT);
    bookButton.addActionListener(action -> bookSelectedContractor());
    bookButton.setEnabled(false);
    searchButton.setToolTipText(SEARCH_BUTTON_TOOLTIP_TEXT);
    searchButton.addActionListener(action -> filterTableOnSearchValues());
    clearButton.setToolTipText(CLEAR_BUTTON_TOOLTIP_TEXT);
    clearButton.addActionListener(action -> {
      nameField.setText(EMPTY_STRING);
      locationField.setText(EMPTY_STRING);
      refreshTable();
    });
    nameField.addActionListener(action -> filterTableOnSearchValues());
    nameField.setToolTipText(NAME_TOOLTIP_TEXT);
    locationField.addActionListener(action -> filterTableOnSearchValues());
    locationField.setToolTipText(LOCATION_TOOLTIP_TEXT);
    contentPanel = createContentPanel();
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

  public void refreshTable() {
    updateTable(getAllRecords());
    filterTableOnSearchValues();
  }

  public void updateTable(final Map<Integer, Contractor> records) {
    model.updateData(recordsToArrayArray(records));
    bookButton.setEnabled(false);
  }

  private void bookSelectedContractor() {
    final int rowIndex = table.getSelectedRow();
    final String[] fieldValues = model.getRowFields(rowIndex);
    final Optional<String> customerId = Optional
        .ofNullable(JOptionPane.showInputDialog(CUSTOMER_ID_PROMPT_TEXT));
    if (customerId.isPresent()) {
      if (isEightDigits(customerId.get())) {
        try {
          final Contractor contractor = ContractorBuilder.build(fieldValues);
          contractor.setCustomerId(customerId.get());
          service.book(contractor);
          refreshTable();
          displayMessage(CONTRACTOR_BOOKED_MESSAGE_TEXT, CONTRACTOR_BOOKED_MESSAGE_TITLE);
        } catch (final RemoteException exception) {
          handleFatalException(REMOTE_EXCEPTION_MESSAGE_TEXT, exception);
        } catch (final ContractorNotFoundException exception) {
          refreshTable();
          handleException(exception.getMessage(), CONTRACTOR_NOT_FOUND_EXCEPTION_MESSAGE_TITLE,
              exception);
        } catch (final AlreadyBookedException exception) {
          refreshTable();
          handleException(exception.getMessage(), CONTRACTOR_ALREADY_BOOKED_EXCEPTION_MESSAGE_TITLE,
              exception);
        }
      } else {
        displayMessage(INVALID_CUSTOMER_ID_MESSAGE_TEXT, INVALID_INPUT_MESSAGE_TITLE);
      }
    }
  }

  private JPanel createActionsPanel() {
    final JPanel actionPanel = new JPanel();
    actionPanel.setBorder(BorderFactory.createTitledBorder(ACTIONS_BORDER_TITLE));
    actionPanel.add(bookButton);
    return actionPanel;
  }

  private JPanel createResultsPanel() {
    final JPanel resultsPanel = new JPanel(new BorderLayout());
    resultsPanel.setBorder(BorderFactory.createTitledBorder(Messages.RESULTS_BORDER_TITLE));
    resultsPanel.add(scrollPane, BorderLayout.CENTER);
    return resultsPanel;
  }

  private JPanel createSearchPanel() {
    final GridBagConstraints constraints = new GridBagConstraints();
    final JPanel searchPanel = new JPanel(new GridBagLayout());
    searchPanel.setBorder(BorderFactory.createTitledBorder(SEARCH_BUTTON_TEXT));
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.ipady = 7;
    constraints.weighty = 0.1;
    constraints.anchor = GridBagConstraints.LINE_END;
    searchPanel.add(nameLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    searchPanel.add(nameField, constraints);
    constraints.gridx = 2;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_END;
    searchPanel.add(locationLabel, constraints);
    constraints.gridx = 3;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    searchPanel.add(locationField, constraints);
    constraints.gridx = 4;
    constraints.gridy = 0;
    constraints.ipady = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    searchPanel.add(searchButton, constraints);
    constraints.gridx = 4;
    constraints.gridy = 1;
    constraints.ipadx = 11;
    constraints.anchor = GridBagConstraints.LINE_START;
    searchPanel.add(clearButton, constraints);
    return searchPanel;
  }

  private void filterTableOnSearchValues() {
    final String name = getNameSearchValue();
    final String location = getLocationSearchValue();
    try {
      final ContractorPk primaryKey = new ContractorPk(name, location);
      final Map<Integer, Contractor> records = service.find(primaryKey);
      updateTable(records);
      scrollPane.setVisible(true);
    } catch (final RemoteException exception) {
      handleFatalException(REMOTE_EXCEPTION_MESSAGE_TEXT, exception);
    } catch (final ContractorNotFoundException exception) {
      scrollPane.setVisible(false);
      handleException(exception.getMessage(), CONTRACTOR_NOT_FOUND_EXCEPTION_MESSAGE_TITLE,
          exception);
    }
  }

  private Map<Integer, Contractor> getAllRecords() {
    final Map<Integer, Contractor> allRecords = new HashMap<>();
    try {
      allRecords.putAll(service.find(new ContractorPk()));
    } catch (final RemoteException exception) {
      handleFatalException(REMOTE_EXCEPTION_MESSAGE_TEXT, exception);
    } catch (final ContractorNotFoundException exception) {
      scrollPane.setVisible(false);
      handleException(exception.getMessage(), CONTRACTOR_NOT_FOUND_EXCEPTION_MESSAGE_TITLE,
          exception);
    }
    return allRecords;
  }

  private String getLocationSearchValue() {
    return locationField.getText().trim();
  }

  private String getNameSearchValue() {
    return nameField.getText().trim();
  }

  private boolean shouldBookButtonBeEnabled(final int rowIndex) {
    if (rowIndex == -1) {
      return false;
    }
    final String[] fieldValues = model.getRowFields(rowIndex);
    final Contractor contractor = ContractorBuilder.build(fieldValues);
    return !contractor.isBooked();
  }

}
