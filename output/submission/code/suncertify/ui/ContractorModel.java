/*
 * ContractorModel.java  1.0  18-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.ui;

import suncertify.domain.Contractor;

import java.util.List;

import javax.swing.table.TableModel;

/**
 * ContractorModel extends the {@link TableModel} interfaces and specifies extra methods that the
 * {@link ContractorTable} table will need for interacting with the custom contractor data model.
 */
public interface ContractorModel extends TableModel {

  /**
   * Gets the row fields.
   *
   * @param rowIndex
   *          the row index.
   * @return the row fields
   */
  String[] getRowFields(int rowIndex);

  /**
   * Updates the ContractorTable with the current state of the data model using the specified
   * {@code data}.
   *
   * @param data
   *          the current state of the data model.
   */
  void updateData(List<Contractor> data);
}
