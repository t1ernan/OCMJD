/*
 * ContractorConverter.java  1.0  19-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.util;

import static suncertify.util.Constants.RECORD_FIELDS;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;

/**
 * The class ContractorConverter has a single purpose: to convert string arrays into
 * {@link Contractor} objects if possible. It's single method {@link #toContractor(String[])} takes
 * a string array argument and returns a Contractor object if it meets the necessary criteria.
 */
public final class ContractorConverter {

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private ContractorConverter() {
  }

  /**
   * Converts the specified {@code fieldValues} string array into a contractor.
   *
   * @param fieldValues
   *          the field values in string array format.
   * @return a Contractor object constructed using the elements in specified {@code fieldValues}.
   * @throws IllegalArgumentException
   *           if {@code fieldValues} is {@code null} or has more elements than the number of
   *           Contractor fields.
   */
  public static Contractor toContractor(final String[] fieldValues)
      throws IllegalArgumentException {
    if (fieldValues == null) {
      throw new IllegalArgumentException(
          "Specified string array argument, 'fieldValues', cannot be null");
    }
    if (fieldValues.length != RECORD_FIELDS) {
      throw new IllegalArgumentException(
          "Specified string array argument, 'fieldValues', must have exactly " + RECORD_FIELDS
              + " elements.");
    }
    final Contractor contractor = new Contractor();
    final ContractorPk primaryKey = new ContractorPk(fieldValues[0], fieldValues[1]);
    contractor.setPrimaryKey(primaryKey);
    contractor.setSpecialities(fieldValues[2]);
    contractor.setSize(fieldValues[3]);
    contractor.setRate(fieldValues[4]);
    contractor.setCustomerId(fieldValues[5]);
    return contractor;
  }
}
