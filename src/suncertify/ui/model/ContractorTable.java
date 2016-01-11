package suncertify.ui.model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import suncertify.domain.Contractor;
import suncertify.util.ContractorBuilder;


public class ContractorTable extends JTable {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	public ContractorTable(final ContractorTableModel model, final JButton bookButton) {
		super(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setRowSelectionAllowed(true);
		addMouseListener(new MouseListener() {

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

			@Override
			public void mouseClicked(final MouseEvent e) {
				hideButtonIfBooked(model, bookButton);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				//DO NOTHING
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				//DO NOTHING
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				hideButtonIfBooked(model, bookButton);

			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				hideButtonIfBooked(model, bookButton);

			}
		});
	}

	private ContractorTable getTable(){
		return this;
	}
}
