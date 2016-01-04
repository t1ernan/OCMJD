package suncertify.domain;

import java.io.Serializable;

public class Contractor implements Serializable {

	private ContractorPK primaryKey;

	private String specialities;

	private String rate;

	private String owner;

	private int size;

	public ContractorPK getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ContractorPK primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getSpecialities() {
		return specialities;
	}

	public void setSpecialities(String specialities) {
		this.specialities = specialities;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
