package suncertify.dto;

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
	private int size;

	/**
	 * Gets the primary key.
	 *
	 * @return the primary key
	 */
	public ContractorPK getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primary key.
	 *
	 * @param primaryKey
	 *            the new primary key
	 */
	public void setPrimaryKey(ContractorPK primaryKey) {
		this.primaryKey = primaryKey;
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
	 * Sets the specialties.
	 *
	 * @param specialties
	 *            the new specialties
	 */
	public void setSpecialities(String specialties) {
		this.specialties = specialties;
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
	 * Sets the rate.
	 *
	 * @param rate
	 *            the new rate
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size
	 *            the new size
	 */
	public void setSize(int size) {
		this.size = size;
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
	 * Sets the customerId.
	 *
	 * @param customerId
	 *            the new customerId
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}