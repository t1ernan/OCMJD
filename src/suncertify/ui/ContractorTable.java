/*
 * ContractorTable.java  1.0  18-Jan-2016
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
import javax.swing.table.TableModel;

/**
 * The class ContractorTable is a subclass of {@link JTable} responsible for displaying contractor
 * records. Its sole constructor requires a {@link ClientWindow} reference, used to enable or
 * disable the book button depending on the current table row selected, and a {@link TableModel}
 * reference to initialize the table with a data model.
 */
public class ContractorTable extends JTable {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new contractor table.
   *
   * @param window
   *          the client window reference used to enable/disable the book button.
   * @param model
   *          the data model the table will be constructed with.
   */
  public ContractorTable(final ClientWindow window, final TableModel model) {
    super(model);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setRowSelectionAllowed(true);
    setColumnSelectionAllowed(false);
    getSelectionModel()
        .addListSelectionListener(change -> window.enableOrDisableBookButton(getSelectedRow()));
  }
}
