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
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * The class ContractorTableModel is subclass of {@link AbstractTableModel} responsible for
 * providing the {@link ContractorTable} with the method implementations necessary to interrogate
 * the tabular data model provided, in this case, a List of {@link Contractor} objects. It also
 * implements the {@link ContractorModel}, which provides extra method definitions for updating the
 * data model with new records and retrieving contractor values given a row number.
 */
public class ContractorTableModel extends AbstractTableModel implements ContractorModel {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The column names. */
  private final String[] columnNames;

  /** The list containing the contractor records. */
  private List<Contractor> recordList;

  /**
   * Instantiates a new contractor table model with the specified {@code columnNames} and
   * {@code recordList}.
   *
   * @param columnNames
   *          an array of column names.
   * @param recordList
   *          a list of Contractors.
   */
  public ContractorTableModel(final String[] columnNames, final List<Contractor> recordList) {
    super();
    this.columnNames = Arrays.copyOf(columnNames, columnNames.length);
    this.recordList = recordList;
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
    return recordList.size();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String[] getRowFields(final int rowIndex) {
    return recordList.get(rowIndex).toStringArray();
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
  public void updateData(final List<Contractor> data) {
    recordList = data;
    fireTableDataChanged();

  }

}
