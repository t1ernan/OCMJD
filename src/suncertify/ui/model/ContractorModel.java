package suncertify.ui.model;

import java.util.Map;

import suncertify.domain.Contractor;

public interface ContractorModel {

	String[] getRowFields(int rowIndex);

	void updateData(Map<Integer,Contractor> data);
}
