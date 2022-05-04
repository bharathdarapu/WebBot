package com.ms.cloudathon2022.security;

import java.util.List;

public class Security {

	public String securityName;
	public String ticker;
	public double allocationPercentage;
	public List<SecurityHistory> stockHistory;
	
	public String getSecurityName() {
		return securityName;
	}
	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}
	public double getAllocationPercentage() {
		return allocationPercentage;
	}
	public void setAllocationPercentage(double allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	
	
	public List<SecurityHistory> getStockHistory() {
		return stockHistory;
	}
	public void setStockHistory(List<SecurityHistory> stockHistory) {
		this.stockHistory = stockHistory;
	}
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
}
