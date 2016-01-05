package suncertify.util;

import suncertify.dto.ContractorPK;

public class ContractorPKConverter {

	private static final int NUMBER_OF_FIELDS = 2;

	public static String[] toSearchCriteria(ContractorPK contractorPK) {
		final String[] fieldValues = new String[NUMBER_OF_FIELDS];
		fieldValues[0] = contractorPK.getName();
		fieldValues[1] = contractorPK.getLocation();

		return fieldValues;
	}

	public static ContractorPK toContractorPK(String[] fieldValues) {
		if (fieldValues.length < NUMBER_OF_FIELDS) {
			throw new IllegalArgumentException("Must have at least" + NUMBER_OF_FIELDS
					+ " elements in the string array argument to create a ContractorPK object");
		}
		final ContractorPK contractorPK = new ContractorPK();
		contractorPK.setName(fieldValues[0]);
		contractorPK.setLocation(fieldValues[1]);

		return contractorPK;
	}
}
