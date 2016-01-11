package suncertify.domain;

import static suncertify.util.Utils.isEightDigits;

import java.io.Serializable;

/**
 * A Data Transfer Object (DTO) used to transfer record data between the
 * different layers of the application. Implements {@link Serializable}.
 */
public class Contractor implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	/** The primary key for a Contractor. */
	private ContractorPK primaryKey;

	/** The specialties. */
	private String specialties;

	/** The rate. */
	private String rate;

	/** The customerId. */
	private String customerId;

	/** The size. */
	private String size;

	public Contractor() {
		super();
		primaryKey = new ContractorPK();
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
	public ContractorPK getPrimaryKey() {
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

	public boolean isBooked() {
		return isEightDigits(customerId);
	}

	/**
	 * Sets the customerId.
	 *
	 * @param customerId
	 *            the new customerId
	 */
	public void setCustomerId(final String customerId) {
		this.customerId = customerId;
	}

	/**
	 * Sets the primary key.
	 *
	 * @param primaryKey
	 *            the new primary key
	 */
	public void setPrimaryKey(final ContractorPK primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Sets the rate.
	 *
	 * @param rate
	 *            the new rate
	 */
	public void setRate(final String rate) {
		this.rate = rate;
	}

	/**
	 * Sets the size.
	 *
	 * @param size
	 *            the new size
	 */
	public void setSize(final String size) {
		this.size = size;
	}

	/**
	 * Sets the specialties.
	 *
	 * @param specialties
	 *            the new specialties
	 */
	public void setSpecialities(final String specialties) {
		this.specialties = specialties;
	}

	public String[] toStringArray() {
		final String name = primaryKey.getName();
		final String location = primaryKey.getLocation();
		return new String[] { name, location, specialties, size, rate, customerId };
	}
}
