/*
 * ContractorTableModel.java  1.0  18-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import suncertify.domain.Contractor;

import java.util.Arrays;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * The class ContractorTableModel is subclass of {@link AbstractTableModel} responsible for
 * providing the {@link ContractorTable} with the method implementations necessary to interrogate
 * the tabular data model provided, in this case, a map of {@link Contractor} records. It also
 * implements the {@link ContractorModel}, which provides extra method definitions for updating the
 * data model with new records and retrieving contractor values given a row number.
 */
public class ContractorTableModel extends AbstractTableModel implements ContractorModel {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The column names. */
  private final String[] columnNames;

  /** The map containing the contractor records. */
  private Map<Integer, Contractor> recordMap;

  /**
   * Instantiates a new contractor table model with the specified {@code columnNames} and
   * {@code recordMap}.
   *
   * @param columnNames
   *          the columns
   * @param recordMap
   *          the values
   */
  public ContractorTableModel(final String[] columnNames,
      final Map<Integer, Contractor> recordMap) {
    super();
    this.columnNames = Arrays.copyOf(columnNames, columnNames.length);
    this.recordMap = recordMap;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String getColumnName(final int columnIndex) {
    return columnNames[columnIndex];
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int getRowCount() {
    return recordMap.size();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String[] getRowFields(final int rowIndex) {
    return getContractor(rowIndex).toStringArray();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    return getRowFields(rowIndex)[columnIndex];
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public boolean isCellEditable(final int rowIndex, final int columnIndex) {
    return false;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void updateData(final Map<Integer, Contractor> data) {
    recordMap = data;
    fireTableDataChanged();

  }

  /**
   * Gets the contractor for the specified {@code rowIndex}.
   *
   * @param rowIndex
   *          the row index of the contractor.
   * @return the contractor represented by the given row index.
   */
  private Contractor getContractor(final int rowIndex) {
    return (Contractor) recordMap.values().toArray()[rowIndex];
  }

}
