package suncertify.business;

import java.rmi.RemoteException;
import java.util.Map;

import suncertify.dto.Contractor;
import suncertify.dto.ContractorPK;

public interface ContractorServices {

	public void book(Contractor contractor) throws ServiceException, RemoteException;

	public Map<Integer, Contractor> find(ContractorPK primaryKey) throws ServiceException, RemoteException;
}
