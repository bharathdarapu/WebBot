package com.ms.cloudathon2022.model;

import java.util.List;

import com.ms.cloudathon2022.security.SecurityPerformance;

public class Holdings {

	private String ticker;
	private double allocation;
	private double securityAmountHolding;
	private double numOfShares;
	private int wholeNumOfShares;
	private double actualHoldingAmount;
	private List<SecurityPerformance> securityPerformance;
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public double getAllocation() {
		return allocation;
	}
	public void setAllocation(double allocation) {
		this.allocation = allocation;
	}
	public double getSecurityAmountHolding() {
		return securityAmountHolding;
	}
	public void setSecurityAmountHolding(double securityAmountHolding) {
		this.securityAmountHolding = securityAmountHolding;
	}
	public double getNumOfShares() {
		return numOfShares;
	}
	public void setNumOfShares(double numOfShares) {
		this.numOfShares = numOfShares;
	}
	public int getWholeNumOfShares() {
		return wholeNumOfShares;
	}
	public void setWholeNumOfShares(int wholeNumOfShares) {
		this.wholeNumOfShares = wholeNumOfShares;
	}
	
	public double getActualHoldingAmount() {
		return actualHoldingAmount;
	}
	public void setActualHoldingAmount(double actualHoldingAmount) {
		this.actualHoldingAmount = actualHoldingAmount;
	}
	
	public List<SecurityPerformance> getSecurityPerformance() {
		return securityPerformance;
	}
	public void setSecurityPerformance(List<SecurityPerformance> securityPerformance) {
		this.securityPerformance = securityPerformance;
	}
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
}
