package suncertify.ui.model;

import suncertify.domain.Contractor;

import java.util.Map;

public interface ContractorModel {

  String[] getRowFields(int rowIndex);

  void updateData(Map<Integer, Contractor> data);
}
