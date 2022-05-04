package com.ms.cloudathon2022.model;

import java.util.List;

import com.ms.cloudathon2022.security.Security;

public class Model {

	public String modelName;
	public List<Security> allocation;
	public List<Holdings> holdings;
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public List<Security> getAllocation() {
		return allocation;
	}
	public void setAllocation(List<Security> allocation) {
		this.allocation = allocation;
	}
	
	public List<Holdings> getHoldings() {
		return holdings;
	}
	public void setHoldings(List<Holdings> holdings) {
		this.holdings = holdings;
	}
	
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
	
}
