package suncertify.util;

import static suncertify.util.Constants.*;
import static suncertify.util.Utils.*;
import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public class ContractorBuilder {

	public static Contractor build(final String[] fieldValues) {
		validateFieldValues(fieldValues);
		final Contractor contractor = new Contractor();
		final ContractorPK primaryKey = new ContractorPK(fieldValues[0], fieldValues[1]);
		contractor.setPrimaryKey(primaryKey);
		contractor.setSpecialities(fieldValues[2]);
		contractor.setSize(Integer.parseInt(fieldValues[3]));
		contractor.setRate(fieldValues[4]);
		contractor.setCustomerId(fieldValues[5]);
		return contractor;
	}

	private static void validateFieldValues(final String[] fieldValues) {
		validateNumberOfElements(fieldValues.length);
		validateSizeField(fieldValues[3]);
		validateCustomerIdField(fieldValues[5]);
	}

	private static void validateCustomerIdField(final String customerId) {
		final boolean isValidCustomerId = (isEightDigits(customerId) || customerId.isEmpty());
		if (!isValidCustomerId) {
			throw new IllegalArgumentException(
					"The 'customer id' field must either be an 8 digit number or left blank.");
		}
	}

	private static void validateSizeField(final String size) {
		try {
			if (Integer.parseInt(size) < 0) {
				throw new IllegalArgumentException("The field 'size' must be a positive integer value.");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The field 'size' must be a positive integer value.");
		}
	}

	private static void validateNumberOfElements(final int fields) {
		if (fields != RECORD_FIELDS) {
			throw new IllegalArgumentException(
					"Must have " + RECORD_FIELDS + " elements in the string array argument.");
		}
	}
}
