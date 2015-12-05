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
		final Contractor contractor = new Contractor();
		final ContractorPK primaryKey = new ContractorPK();
    	primaryKey.setName(fieldValues[0]);
    	primaryKey.setLocation(fieldValues[1]);
        
        contractor.setPrimaryKey(primaryKey);
        contractor.setSpecialities(fieldValues[2]);
        contractor.setSize(Integer.parseInt(fieldValues[3]));
        contractor.setRate(fieldValues[4]);
        contractor.setOwner(fieldValues[5]);
        
		return contractor;
	}
}
