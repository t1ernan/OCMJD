/*
 * Contractor.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.domain;

import static suncertify.util.Utils.isEightDigits;

import java.io.Serializable;

/**
 * A domain object representing a contractor. Used to transfer contractor data between the different
 * layers of the application. Contains helpful methods for determining if the contractor has been
 * booked by a customer and for converting the contractor fields to a string array. Implements
 * {@link Serializable}.
 */
public class Contractor implements Serializable {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The primary key for a Contractor. */
  private ContractorPk primaryKey;

  /** The specialties. */
  private String specialties;

  /** The rate. */
  private String rate;

  /** The customerId. */
  private String customerId;

  /** The size. */
  private String size;

  /**
   * Constructs a new default contractor.
   */
  public Contractor() {
    super();
    primaryKey = new ContractorPk();
    specialties = "";
    rate = "";
    customerId = "";
    size = "";
  }

  /**
   * Gets the customerId.
   *
   * @return the customerId
   */
  public String getCustomerId() {
    return customerId;
  }

  /**
   * Gets the primary key.
   *
   * @return the primary key
   */
  public ContractorPk getPrimaryKey() {
    return primaryKey;
  }

  /**
   * Gets the rate.
   *
   * @return the rate
   */
  public String getRate() {
    return rate;
  }

  /**
   * Gets the size.
   *
   * @return the size
   */
  public String getSize() {
    return size;
  }

  /**
   * Gets the specialties.
   *
   * @return the specialties
   */
  public String getSpecialties() {
    return specialties;
  }

  /**
   * Checks if the contractor is booked by a customer. Returns true if the {@code customerId} field
   * contains an eight digit number, i.e. if the contractor is booked.
   *
   * @return true, if is booked.
   */
  public boolean isBooked() {
    return isEightDigits(customerId);
  }

  /**
   * Sets the customerId.
   *
   * @param customerId
   *          the new customerId
   */
  public void setCustomerId(final String customerId) {
    this.customerId = customerId;
  }

  /**
   * Sets the primary key.
   *
   * @param primaryKey
   *          the new primary key
   */
  public void setPrimaryKey(final ContractorPk primaryKey) {
    this.primaryKey = primaryKey;
  }

  /**
   * Sets the rate.
   *
   * @param rate
   *          the new rate
   */
  public void setRate(final String rate) {
    this.rate = rate;
  }

  /**
   * Sets the size.
   *
   * @param size
   *          the new size
   */
  public void setSize(final String size) {
    this.size = size;
  }

  /**
   * Sets the specialties.
   *
   * @param specialties
   *          the new specialties
   */
  public void setSpecialities(final String specialties) {
    this.specialties = specialties;
  }

  /**
   * Converts the fields of the contractor object to a string array.
   *
   * @return the string[]
   */
  public String[] toStringArray() {
    final String name = primaryKey.getName();
    final String location = primaryKey.getLocation();
    return new String[] { name, location, specialties, size, rate, customerId };
  }
}
