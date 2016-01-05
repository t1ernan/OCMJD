package suncertify.dto;

import java.io.Serializable;

/**
 * A Data Transfer Object (DTO) used to transfer record search criteria between
 * the different layers of the application. A component of the
 * {@link Contractor} class. Implements {@link Serializable}.
 */
public class ContractorPK implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	/** The name. */
	private String name;

	/** The location. */
	private String location;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
