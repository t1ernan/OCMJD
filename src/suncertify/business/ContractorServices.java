package suncertify.business;

import java.util.Map;

import suncertify.domain.Contractor;
import suncertify.domain.ContractorPK;

public interface ContractorServices {

	public void createBooking(Contractor contractor) throws Exception;

	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws Exception;
}
