/*
 * ContractorTableModel.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui.model;

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

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
    return columns.length;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(final int columnIndex) {
    return columns[columnIndex];
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
    return values.length;
  }

  /* (non-Javadoc)
   * @see suncertify.ui.model.ContractorModel#getRowFields(int)
   */
  @Override
  public String[] getRowFields(final int rowIndex) {
    return values[rowIndex];
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    return values[rowIndex][columnIndex];
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable(final int rowIndex, final int columnIndex) {
    return false;
  }

  @Override
  public void updateCell(final int row, final int column,final String cellData){
    values[row][column]= cellData;
    fireTableCellUpdated(row, column);
  }

  /* (non-Javadoc)
   * @see suncertify.ui.model.ContractorModel#updateData(java.lang.String[][])
   */
  @Override
  public void updateData(final String[][] data) {
    values = data;
    fireTableDataChanged();
  }

}
