package suncertify.util;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public class ContractorConverter {

	private static final int NUMBER_OF_FIELDS = 6;

	public static String[] toFieldValues(Contractor contractor) {
		final String[] fieldValues = new String[NUMBER_OF_FIELDS];
		ContractorPK contractorPK = contractor.getPrimaryKey();
		fieldValues[0] = contractorPK.getName();
		fieldValues[1] = contractorPK.getLocation();
		fieldValues[2] = contractor.getSpecialities();
		fieldValues[3] = String.valueOf(contractor.getSize());
		fieldValues[4] = contractor.getRate();
		fieldValues[5] = contractor.getOwner();

		return fieldValues;
	}

	public static Contractor toContractor(String[] fieldValues) {
		if (fieldValues.length < NUMBER_OF_FIELDS) {
			throw new IllegalArgumentException("Must have at least " + NUMBER_OF_FIELDS
					+ " elements in the string array argument to create a Contractor object");
		}

		final Contractor contractor = new Contractor();
		final ContractorPK primaryKey = ContractorPKConverter.toContractorPK(fieldValues);

		contractor.setPrimaryKey(primaryKey);
		contractor.setSpecialities(fieldValues[2]);
		contractor.setSize(Integer.parseInt(fieldValues[3]));
		contractor.setRate(fieldValues[4]);
		contractor.setOwner(fieldValues[5]);

		return contractor;
	}
}
