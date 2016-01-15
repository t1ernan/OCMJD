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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
   * @param model
   *          the model
   * @param bookButton
   *          the book button
   */
  public ContractorTable(final MainWindow window) {
    super(window.getModel());
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setRowSelectionAllowed(true);
    setColumnSelectionAllowed(false);
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(final MouseEvent event) {
        window.hideButtonIfBooked();
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
        window.hideButtonIfBooked();

      }

      @Override
      public void mouseReleased(final MouseEvent event) {
        window.hideButtonIfBooked();
      }

    });
  }
}
