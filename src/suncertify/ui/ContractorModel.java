/*
 * ContractorModel.java  1.0  14-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */
package suncertify.ui;

// TODO: Auto-generated Javadoc
/**
 * The Interface ContractorModel.
 */
public interface ContractorModel {

  /**
   * Gets the row fields.
   *
   * @param rowIndex the row index
   * @return the row fields
   */
  String[] getRowFields(int rowIndex);

  /**
   * Update data.
   *
   * @param data the data
   */
  void updateData(String[][] data);

  void updateRow(int row, String[] fieldValues);
}
