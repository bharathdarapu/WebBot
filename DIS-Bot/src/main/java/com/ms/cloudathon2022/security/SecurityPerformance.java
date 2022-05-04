package com.ms.cloudathon2022.security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SecurityPerformance {

	//Date	Open	High	Low	Close	Adj Close	Volume
	
	private String date;
	private Date formattedDate;
	private double AmountHolding;
	private int wholeNumOfShares;
	private double allocationPercentage;
	private double currentPrice;
	private double percentageDifferenceFromPrevious;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
		try {
			this.formattedDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Date getFormattedDate() {
		return formattedDate;
	}
	
	
	public void setFormattedDate(Date formattedDate) {
		this.formattedDate = formattedDate;
	}
	
	public double getAmountHolding() {
		return AmountHolding;
	}
	public void setAmountHolding(double amountHolding) {
		AmountHolding = amountHolding;
	}
	
	
	public int getWholeNumOfShares() {
		return wholeNumOfShares;
	}
	public void setWholeNumOfShares(int wholeNumOfShares) {
		this.wholeNumOfShares = wholeNumOfShares;
	}
	
	public double getAllocationPercentage() {
		return allocationPercentage;
	}
	public void setAllocationPercentage(double allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}
	
	
	
	public double getPercentageDifferenceFromPrevious() {
		return percentageDifferenceFromPrevious;
	}
	public void setPercentageDifferenceFromPrevious(double percentageDifferenceFromPrevious) {
		this.percentageDifferenceFromPrevious = percentageDifferenceFromPrevious;
	}
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
	
}
