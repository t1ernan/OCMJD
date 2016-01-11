package suncertify.domain;

import static suncertify.util.Constants.RECORD_FIELDS;
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
	public void setPrimaryKey(final ContractorPK primaryKey) {
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
	public void setSpecialities(final String specialties) {
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
	public void setRate(final String rate) {
		this.rate = rate;
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
	 * Sets the size.
	 *
	 * @param size
	 *            the new size
	 */
	public void setSize(final String size) {
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
	public void setCustomerId(final String customerId) {
		this.customerId = customerId;
	}

	public boolean isBooked() {
		return isEightDigits(customerId);
	}

	public String[] toStringArray() {
		final String[] fieldValues = new String[RECORD_FIELDS];
		fieldValues[0] = primaryKey.getName();
		fieldValues[1] = primaryKey.getLocation();
		fieldValues[2] = specialties;
		fieldValues[3] = size;
		fieldValues[4] = rate;
		fieldValues[5] = customerId;
		return fieldValues;
	}
}
