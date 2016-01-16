/*
 * ContractorPk.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.domain;

import java.io.Serializable;

/**
 * The primary key used to uniquely identify different contractors comprised of a
 * contractor's name and location. A component of the {@link Contractor} class. Implements
 * {@link Serializable}.
 */
public class ContractorPk implements Serializable {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /** The name. */
  private String name;

  /** The location. */
  private String location;

  /**
   * Constructs a new default contractor primary key.
   */
  public ContractorPk() {
    name = "";
    location = "";
  }

  /**
   * Constructs a new contractor primary key with the specified {@code name} and {@code location}.
   *
   * @param name
   *          the name
   * @param location
   *          the location
   */
  public ContractorPk(final String name, final String location) {
    this.name = name;
    this.location = location;
  }

  /**
   * Gets the location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the location.
   *
   * @param location
   *          the new location
   */
  public void setLocation(final String location) {
    this.location = location;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String toString() {
    return "ContractorPk [name=" + name + ", location=" + location + "]";
  }

  /**
   * Converts the fields of the ContractorPk object to a string array.
   *
   * @return the string[]
   */
  public String[] toStringArray() {
    return new String[] { name, location };
  }


}
