package com.ms.cloudathon2022.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModelPerformance {

	//Date	Open	High	Low	Close	Adj Close	Volume
	
	private String date;
	private Date formattedDate;
	private double AmountHolding;
	private double amountChange;
	private double percentChange;
	
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
	
	public double getPercentChange() {
		return percentChange;
	}
	public void setPercentChange(double percentChange) {
		this.percentChange = percentChange;
	}
	
	
	
	public double getAmountChange() {
		return amountChange;
	}
	public void setAmountChange(double amountChange) {
		this.amountChange = amountChange;
	}
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
	
}
