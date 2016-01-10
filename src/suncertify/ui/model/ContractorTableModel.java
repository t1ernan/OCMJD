package suncertify.ui.model;

import java.util.Map;

import javax.swing.table.AbstractTableModel;

import suncertify.domain.Contractor;

public class ContractorTableModel extends AbstractTableModel implements ContractorModel{

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;
	
	private String[] columns;
	private Map<Integer, Contractor> records;
	
	public ContractorTableModel(String[] columns, Map<Integer, Contractor> records){
		this.columns = columns;
		this.records= records;
	}
	
	@Override
	public int getColumnCount() {
		return this.columns.length;
	}

	@Override
	public int getRowCount() {
		return this.records.size();
	}
	@Override
	public String getColumnName(int columnIndex){
		return this.columns[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final Contractor contractor = this.records.get(rowIndex);
		return contractor.toStringArray()[columnIndex];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public String[] getRowFields(int rowIndex) {
		return this.records.get(rowIndex).toStringArray();
	}

	@Override
	public void updateData(Map<Integer, Contractor> data) {
		this.records = data;
		this.fireTableDataChanged();
	}

}
