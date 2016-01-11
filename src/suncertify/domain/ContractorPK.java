package suncertify.domain;

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

	public ContractorPK() {
		name = "";
		location = "";
	}

	public ContractorPK(final String name, final String location) {
		this.name = name;
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String[] toStringArray() {
		return new String[] { name, location };
	}
}
