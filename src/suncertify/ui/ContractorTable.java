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


import javax.swing.JTable;
import javax.swing.ListSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ContractorTable.
 */
public class ContractorTable extends JTable {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  public ContractorTable(final ClientWindow window) {
    super(window.getTableModel());
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setRowSelectionAllowed(true);
    setColumnSelectionAllowed(false);
    getSelectionModel()
        .addListSelectionListener(change -> window.enableOrDisableBookButton(getSelectedRow()));
  }
}
