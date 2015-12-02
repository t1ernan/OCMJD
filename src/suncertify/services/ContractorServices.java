package suncertify.services;

import java.util.Map;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public interface ContractorServices {

	public void createBooking(Contractor contractor) throws Exception;
	
	public Map<Long,Contractor> listAllContractors() throws Exception;
	
	public Contractor findContractor(ContractorPK primaryKey) throws Exception;
}
