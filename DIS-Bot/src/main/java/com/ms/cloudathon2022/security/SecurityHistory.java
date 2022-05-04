package com.ms.cloudathon2022.security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SecurityHistory {

	//Date	Open	High	Low	Close	Adj Close	Volume
	
	private String date;
	private Date formattedDate;
	private double open;
	private double high;
	private double low;
	private double close;
	private double adjClose;
	private double volume;
	
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
	public void setFormattedDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
		try {
			this.formattedDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getAdjClose() {
		return adjClose;
	}
	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
	
}
