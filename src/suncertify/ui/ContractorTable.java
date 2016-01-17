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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

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
  public ContractorTable(final ClientWindow window, TableModel model) {
    super(model);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setRowSelectionAllowed(true);
    setColumnSelectionAllowed(false);
    setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(final MouseEvent event) {
        window.enableOrDisableButton();
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
        window.enableOrDisableButton();

      }

      @Override
      public void mouseReleased(final MouseEvent event) {
        window.enableOrDisableButton();
      }
    });
  }

  @Override
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    Component component = super.prepareRenderer(renderer, row, column);
    final double width = component.getPreferredSize().getWidth();
    final TableColumn tableColumn = getColumnModel().getColumn(column);
    tableColumn.setPreferredWidth((int) Math.max(width + getIntercellSpacing().getWidth(), tableColumn.getPreferredWidth()));
    return component;
  }
}
