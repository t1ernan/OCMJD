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

import javax.swing.table.TableModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface ContractorModel.
 */
public interface ContractorModel extends TableModel {

  String[] getRowFields(int rowIndex);

  void updateData(String[][] data);

  void updateRow(int row, String[] fieldValues);
}
