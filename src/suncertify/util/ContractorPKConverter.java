package suncertify.util;

import suncertify.domain.ContractorPK;

public class ContractorPKConverter {
	
	private static final int NUMBER_OF_FIELDS = 2;
	
	public static String[] toSearchCriteria(ContractorPK contractorPK) {
		final String[] fieldValues = new String[NUMBER_OF_FIELDS];
		fieldValues[0] = contractorPK.getName();
		fieldValues[1] = contractorPK.getLocation();
		
		return fieldValues;
	}
	
	public static ContractorPK toContractorPK(String[] fieldValues) {
		final ContractorPK contractorPK = new ContractorPK();
		contractorPK.setName(fieldValues[0]);
		contractorPK.setLocation(fieldValues[1]);
		
		return contractorPK;
	}
}
