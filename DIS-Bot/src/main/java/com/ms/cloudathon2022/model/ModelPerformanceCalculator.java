package com.ms.cloudathon2022.model;

import java.util.ArrayList;
import java.util.List;

import com.ms.cloudathon2022.security.ParseSecurityPriceData;
import com.ms.cloudathon2022.security.Security;
import com.ms.cloudathon2022.security.SecurityHistory;
import com.ms.cloudathon2022.security.SecurityPerformance;

public class ModelPerformanceCalculator {

	private double investmentAmount = 100000;
	//private Performance performance;
	
	
	
	public static void main(String args[])
	{
		Model model = new Model();
		
		List<Security> securitiesList = new ArrayList<Security>();
		
		Security security = new Security();
		security.setSecurityName("Apple");
		security.setTicker("AAPL");
		security.setAllocationPercentage(50);
		securitiesList.add(security);
		
		security = new Security();
		security.setSecurityName("Amazon");
		security.setTicker("AMZN");
		security.setAllocationPercentage(50);
		securitiesList.add(security);
		
		model.setAllocation(securitiesList);
		
		ModelPerformanceCalculator perf = new ModelPerformanceCalculator();
		perf.calculate(model);
		
	}
	
	
	public List<ModelPerformance> calculate(Model model)
	{
		//List<Security> allocationList = new ArrayList<Security>();
		
		
		//remove cash
		/*for(Security security: model.getAllocation())
		{
			System.out.println("Security: "+security);
			if (!security.getTicker().equalsIgnoreCase("cash"))
			allocationList.add(security);
		}
		
		model.setAllocation(allocationList);*/
		
		attachSecurityHistory(model);
		calculateSecurityPerformance(model);
		List<ModelPerformance> modelPerformanceHistory= calculateModelPerformance(model);
		
		return modelPerformanceHistory;
	}
	
	public Model attachSecurityHistory(Model model)
	{
		List<Security> oldsecurityList = model.getAllocation();
		List<Security> securityList = new ArrayList<Security>();
		
		for(Security oldsecurity: oldsecurityList)
		{
			Security security = new Security();
			ParseSecurityPriceData parseStockPriceData = new ParseSecurityPriceData();
			security.setSecurityName(oldsecurity.getSecurityName());
			security.setTicker(oldsecurity.getTicker());
			security.setAllocationPercentage(oldsecurity.getAllocationPercentage());
			security.setStockHistory(parseStockPriceData.parseHistory(oldsecurity.getTicker()));
			securityList.add(security);
		}
		
		model.setAllocation(securityList);
		
		return model;
		
		//System.out.println(model);
		
	}
	
	
	public void calculateSecurityPerformance(Model model)
	{
		
		
		List<Holdings> holdingsList = new ArrayList<Holdings>();
		
		for(Security security: model.getAllocation()) {

		Holdings holdings = new Holdings();
		holdings.setTicker(security.getTicker());
		holdings.setAllocation(security.getAllocationPercentage());
		holdings.setSecurityAmountHolding(investmentAmount*security.getAllocationPercentage()/100);
		Double closingPrice = security.getStockHistory().get(1).getAdjClose();
		Double numOfShares = holdings.getSecurityAmountHolding()/closingPrice;
		holdings.setNumOfShares(numOfShares);
		holdings.setWholeNumOfShares((int)Math.floor(numOfShares));
		holdings.setActualHoldingAmount(holdings.getWholeNumOfShares()*closingPrice);
		
		List<SecurityPerformance> securityPerformanceList = new ArrayList<SecurityPerformance>();
		
		Double oldPrice=0.0;
		for(SecurityHistory securityHistory : security.getStockHistory()) {
			
			SecurityPerformance securityPerformance = new SecurityPerformance();
			securityPerformance.setDate(securityHistory.getDate());
			securityPerformance.setWholeNumOfShares(holdings.getWholeNumOfShares());
			securityPerformance.setAmountHolding(securityPerformance.getWholeNumOfShares()*securityHistory.getAdjClose());
			securityPerformance.setAllocationPercentage(security.getAllocationPercentage());
			securityPerformance.setCurrentPrice(securityHistory.getAdjClose());
			
			if(oldPrice==0.0)
			{
				oldPrice=securityHistory.getAdjClose();
				securityPerformance.setPercentageDifferenceFromPrevious(0.0);
			}
			else if(oldPrice>0)
			{
				securityPerformance.setPercentageDifferenceFromPrevious((securityHistory.getAdjClose()-oldPrice)/oldPrice*100);
				oldPrice=securityHistory.getAdjClose();
			}
			
			
			securityPerformanceList.add(securityPerformance);
			
			
			
		}
		holdings.setSecurityPerformance(securityPerformanceList);
		holdingsList.add(holdings);
		}
		model.setHoldings(holdingsList);
		
		//Calculate the total cash left
		double cashLeft = 0;
		for(Holdings holding: holdingsList)
		{
			cashLeft = cashLeft+(holding.getSecurityAmountHolding()-holding.getActualHoldingAmount());
		}
		
		investmentAmount=investmentAmount - cashLeft;
		
	}
	
	public List<ModelPerformance> calculateModelPerformance(Model model)
	{
		List<ModelPerformance> modelPerformanceHistory = new ArrayList<ModelPerformance>();
		List<ModelPerformance> tempModelPerformanceHistory = new ArrayList<ModelPerformance>();
			
		//copy date of all securities
		
		for(SecurityPerformance securityPerfomance: model.getHoldings().get(0).getSecurityPerformance())
		{
			ModelPerformance modelPerformance = new ModelPerformance();
			modelPerformance.setDate(securityPerfomance.getDate());
			tempModelPerformanceHistory.add(modelPerformance);
		}
		
		//add all holdings
		for(ModelPerformance tempModelPerformance: tempModelPerformanceHistory)
		{
			ModelPerformance modelPerformance = new ModelPerformance();
			modelPerformance.setDate(tempModelPerformance.getDate());
			double totalHoldingAmount = 0;
			for(Holdings holdings: model.getHoldings())
			{
				for(SecurityPerformance securityPerformance: holdings.getSecurityPerformance())
				{
					if(securityPerformance.getDate().equals(tempModelPerformance.getDate()))
						totalHoldingAmount = totalHoldingAmount+securityPerformance.getAmountHolding();	
				}
			}
			modelPerformance.setAmountHolding(totalHoldingAmount);
			modelPerformance.setAmountChange(totalHoldingAmount-investmentAmount);
			modelPerformance.setPercentChange(((totalHoldingAmount-investmentAmount)/investmentAmount)*100);
			modelPerformanceHistory.add(modelPerformance);
		}
	
		System.out.println(modelPerformanceHistory);
		return modelPerformanceHistory;
		
	}
	
}
