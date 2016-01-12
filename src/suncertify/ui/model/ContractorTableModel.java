package suncertify.ui.model;

import suncertify.domain.Contractor;

import java.util.Arrays;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ContractorTableModel extends AbstractTableModel implements ContractorModel {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  private final String[] columns;
  private Map<Integer, Contractor> records;

  public ContractorTableModel(final String[] columns, final Map<Integer, Contractor> records) {
    super();
    this.columns = Arrays.copyOf(columns, columns.length);
    this.records = records;
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public String getColumnName(final int columnIndex) {
    return columns[columnIndex];
  }

  @Override
  public int getRowCount() {
    return records.size();
  }

  @Override
  public String[] getRowFields(final int rowIndex) {
    return records.get(rowIndex).toStringArray();
  }

  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    final Contractor contractor = records.get(rowIndex);
    return contractor.toStringArray()[columnIndex];
  }

  @Override
  public boolean isCellEditable(final int rowIndex, final int columnIndex) {
    return false;
  }

  @Override
  public void updateData(final Map<Integer, Contractor> data) {
    records = data;
    fireTableDataChanged();
  }

}
