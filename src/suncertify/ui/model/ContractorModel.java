package suncertify.ui.model;

import java.util.Map;

import suncertify.domain.Contractor;

public interface ContractorModel {

	public String[] getRowFields(int rowIndex);
	
	public void updateData(Map<Integer,Contractor> data);
}
