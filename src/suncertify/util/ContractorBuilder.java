package suncertify.util;

import static suncertify.util.Constants.RECORD_FIELDS;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPk;

public final class ContractorBuilder {

	private ContractorBuilder(){

	}

	public static Contractor build(final String[] fieldValues) {
		if (fieldValues.length != RECORD_FIELDS) {
			throw new IllegalArgumentException(
					"Must have " + RECORD_FIELDS + " elements in the string array argument.");
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
