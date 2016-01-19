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
import java.util.HashMap;
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

/**
 * The Class ClientWindow.
 */
public final class ClientWindow extends AbstractWindow {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The service. */
  private final ContractorService service;
  
  /** The model. */
  private ContractorModel model;
  
  /** The table. */
  private JTable table;
  
  /** The scroll pane. */
  private JScrollPane scrollPane;
  
  /** The column names. */
  private final String[] columnNames = { "Name", "Location", "Specialties", "Size", "Rate",
      "Customer ID" };
  
  /** The name label. */
  private final JLabel nameLabel = new JLabel(NAME_LABEL_TEXT);
  
  /** The location label. */
  private final JLabel locationLabel = new JLabel(LOCATION_LABEL_TEXT);
  
  /** The name field. */
  private final JTextField nameField = new JTextField(DEFAULT_TEXTFIELD_SIZE);
  
  /** The location field. */
  private final JTextField locationField = new JTextField(DEFAULT_TEXTFIELD_SIZE);
  
  /** The book button. */
  private final JButton bookButton = new JButton(BOOK_BUTTON_TEXT);
  
  /** The search button. */
  private final JButton searchButton = new JButton(SEARCH_BUTTON_TEXT);
  
  /** The clear button. */
  private final JButton clearButton = new JButton(CLEAR_BUTTON_TEXT);
  
  /** The content panel. */
  private JPanel contentPanel;

  /**
   * Instantiates a new client window.
   *
   * @param service the service
   */
  public ClientWindow(final ContractorService service) {
    super(SYSTEM_NAME);
    this.service = service;
    initializeComponents();
    setPreferredSize(new Dimension(775, 650));
    setMinimumSize(new Dimension(650, 220));
    getContentPane().add(contentPanel);
    pack();
  }

  /* (non-Javadoc)
   * @see suncertify.ui.WindowManager#createContentPanel()
   */
  @Override
  public JPanel createContentPanel() {
    final JPanel panel = new JPanel(new BorderLayout());
    panel.add(createSearchPanel(), BorderLayout.NORTH);
    panel.add(createActionsPanel(), BorderLayout.WEST);
    panel.add(createResultsPanel(), BorderLayout.CENTER);
    return panel;
  }

  /**
   * Enable or disable book button.
   *
   * @param rowIndex the row index
   */
  public void enableOrDisableBookButton(final int rowIndex) {
    bookButton.setEnabled(shouldBookButtonBeEnabled(rowIndex));
  }

  /* (non-Javadoc)
   * @see suncertify.ui.WindowManager#initializeComponents()
   */
  @Override
  public void initializeComponents() {
    model = new ContractorTableModel(columnNames, getAllRecords());
    table = new ContractorTable(this, model);
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

  /**
   * Book selected contractor.
   */
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

  /**
   * Creates the actions panel.
   *
   * @return the j panel
   */
  private JPanel createActionsPanel() {
    final JPanel actionPanel = new JPanel();
    actionPanel.setBorder(BorderFactory.createTitledBorder(ACTIONS_BORDER_TITLE));
    actionPanel.add(bookButton);
    return actionPanel;
  }

  /**
   * Creates the results panel.
   *
   * @return the j panel
   */
  private JPanel createResultsPanel() {
    final JPanel resultsPanel = new JPanel(new BorderLayout());
    resultsPanel.setBorder(BorderFactory.createTitledBorder(Messages.RESULTS_BORDER_TITLE));
    resultsPanel.add(scrollPane, BorderLayout.CENTER);
    return resultsPanel;
  }

  /**
   * Creates the search panel.
   *
   * @return the j panel
   */
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

  /**
   * Filter table on search values.
   */
  private void filterTableOnSearchValues() {
    final String name = getNameSearchValue();
    final String location = getLocationSearchValue();
    try {
      final ContractorPk primaryKey = new ContractorPk(name, location);
      final Map<Integer, Contractor> records = service.find(primaryKey);
      model.updateData(records);
      bookButton.setEnabled(false);
      scrollPane.setVisible(true);
    } catch (final RemoteException exception) {
      handleFatalException(REMOTE_EXCEPTION_MESSAGE_TEXT, exception);
    } catch (final ContractorNotFoundException exception) {
      scrollPane.setVisible(false);
      handleException(exception.getMessage(), CONTRACTOR_NOT_FOUND_EXCEPTION_MESSAGE_TITLE,
          exception);
    }
  }

  /**
   * Gets the all records.
   *
   * @return the all records
   */
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

  /**
   * Gets the location search value.
   *
   * @return the location search value
   */
  private String getLocationSearchValue() {
    return locationField.getText().trim();
  }

  /**
   * Gets the name search value.
   *
   * @return the name search value
   */
  private String getNameSearchValue() {
    return nameField.getText().trim();
  }

  /**
   * Refresh table.
   */
  private void refreshTable() {
    model.updateData(getAllRecords());
    bookButton.setEnabled(false);
    filterTableOnSearchValues();
  }

  /**
   * Should book button be enabled.
   *
   * @param rowIndex the row index
   * @return true, if successful
   */
  private boolean shouldBookButtonBeEnabled(final int rowIndex) {
    boolean shouldEnable = false;
    if (rowIndex == -1) {
      shouldEnable = false;
    } else {
      final String[] fieldValues = model.getRowFields(rowIndex);
      final Contractor contractor = ContractorBuilder.build(fieldValues);
      shouldEnable = !contractor.isBooked();
    }
    return shouldEnable;
  }
}
