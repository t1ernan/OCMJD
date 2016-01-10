package suncertify.ui.model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import suncertify.domain.Contractor;
import suncertify.util.ContractorBuilder;


public class ContractorTable extends JTable {

	private JButton bookButton;
	
	public ContractorTable(final ContractorTableModel model, final JButton bookButton) {
		super(model);
		this.bookButton = bookButton;
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setRowSelectionAllowed(true);
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				hideButtonIfBooked(model, bookButton);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				hideButtonIfBooked(model, bookButton);
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				hideButtonIfBooked(model, bookButton);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//DO NOTHING
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				//DO NOTHING
			}

			private void hideButtonIfBooked(final ContractorTableModel model, final JButton bookButton) {
				final int rowIndex = getTable().getSelectedRows()[0];
				final String[] fieldValues = model.getRowFields(rowIndex);
				final Contractor contractor = ContractorBuilder.build(fieldValues);
				if(contractor.isBooked()){
					bookButton.setVisible(false);
				}else{
					bookButton.setVisible(true);
				}
			}
		});
	}
	
	private ContractorTable getTable(){
		return this;
	}
}
