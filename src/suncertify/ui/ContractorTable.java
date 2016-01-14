/*
 * ContractorTable.java  1.0  14-Jan-2016
 * 
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 * 
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

import suncertify.domain.Contractor;
import suncertify.util.ContractorBuilder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ContractorTable.
 */
public class ContractorTable extends JTable {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Instantiates a new contractor table.
   *
   * @param model the model
   * @param bookButton the book button
   */
  public ContractorTable(final ContractorTableModel model, final JButton bookButton) {
    super(model);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setRowSelectionAllowed(true);
    setColumnSelectionAllowed(false);
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(final MouseEvent event) {
        hideButtonIfBooked(model, bookButton);
      }

      @Override
      public void mouseEntered(final MouseEvent event) {
        // DO NOTHING
      }

      @Override
      public void mouseExited(final MouseEvent event) {
        // DO NOTHING
      }

      @Override
      public void mousePressed(final MouseEvent event) {
        hideButtonIfBooked(model, bookButton);

      }

      @Override
      public void mouseReleased(final MouseEvent event) {
        hideButtonIfBooked(model, bookButton);

      }

      private void hideButtonIfBooked(final ContractorTableModel model, final JButton bookButton) {
        final int rowIndex = getTable().getSelectedRows()[0];
        final String[] fieldValues = model.getRowFields(rowIndex);
        final Contractor contractor = ContractorBuilder.build(fieldValues);
        if (contractor.isBooked()) {
          bookButton.setVisible(false);
        } else {
          bookButton.setVisible(true);
        }
      }
    });
  }

  /**
   * Gets the table.
   *
   * @return the table
   */
  private ContractorTable getTable() {
    return this;
  }
}
