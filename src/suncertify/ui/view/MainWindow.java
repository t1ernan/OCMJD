package suncertify.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import suncertify.business.ContractorNotFoundException;
import suncertify.business.ContractorService;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;
import suncertify.ui.model.ContractorTable;
import suncertify.ui.model.ContractorTableModel;
import suncertify.util.ContractorBuilder;

public class MainWindow extends JFrame {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	private final ContractorService service;
	private final JTable table;
	private final ContractorTableModel tableModel;
	private final String[] columnNames = { "Name", "Location", "Specialties", "Size", "Rate", "Customer ID" };
	private final JButton bookButton;
	
	public MainWindow(final ContractorService service){
		super("Bodgitt & Scarper Booking System");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.LIGHT_GRAY);
		this.service = service;
		this.tableModel = new ContractorTableModel(columnNames, getAllRecords());
		this.bookButton = createBookButton();
		this.table = new ContractorTable(tableModel, bookButton);
		
		final JScrollPane scrollPane = new JScrollPane(table);
		this.getContentPane().add(scrollPane, BorderLayout.NORTH);
		this.getContentPane().add(bookButton, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}

	private Map<Integer, Contractor> getAllRecords(){
		Map<Integer, Contractor> records = null;
		try {
			return service.find(new ContractorPK("", ""));
		} catch (ContractorNotFoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return records;
	}
	
	public JButton createBookButton(){
		final JButton bookButton = new JButton("Book");
		bookButton.addActionListener(action -> {
			final int rowIndex = table.getSelectedRows()[0];
			final ContractorTableModel model = (ContractorTableModel) table.getModel();
			final String[] fieldValues = model.getRowFields(rowIndex);
			final String customerId = JOptionPane.showInputDialog("Please enter the customer ID number.");
			fieldValues[5] = customerId;
			try {
				final Contractor contractor =ContractorBuilder.build(fieldValues);
				service.book(contractor);
				updateView();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Try again: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		return bookButton;
	}
	
	public void updateView(){
		this.tableModel.updateData(getAllRecords());
	}
}
