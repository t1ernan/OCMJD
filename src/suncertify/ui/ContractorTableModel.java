/*
 * ContractorTableModel.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ContractorTableModel.
 */
public class ContractorTableModel extends AbstractTableModel implements ContractorModel {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The columns. */
  private final String[] columns;

  /** The values. */
  private String[][] values;

  /**
   * Instantiates a new contractor table model.
   *
   * @param columns the columns
   * @param values the values
   */
  public ContractorTableModel(final String[] columns, final String[][] values) {
    super();
    this.columns = Arrays.copyOf(columns, columns.length);
    this.values = values;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int getColumnCount() {
    return columns.length;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String getColumnName(final int columnIndex) {
    return columns[columnIndex];
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int getRowCount() {
    return values.length;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String[] getRowFields(final int rowIndex) {
    return values[rowIndex];
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    return values[rowIndex][columnIndex];
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
  public void updateData(final String[][] data) {
    values = data;
    fireTableDataChanged();
  }

}
