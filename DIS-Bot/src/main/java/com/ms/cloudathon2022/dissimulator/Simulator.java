package com.ms.cloudathon2022.dissimulator;

import java.util.ArrayList;
import java.util.List;

import com.ms.cloudathon2022.model.Model;
import com.ms.cloudathon2022.model.ModelPerformance;
import com.ms.cloudathon2022.model.ModelPerformanceCalculator;
import com.ms.cloudathon2022.security.Security;

public class Simulator {
	
	
	public static void main(String args[])
	{
		Simulator sim = new Simulator();
		sim.simulate();
	}
	
	
	public List<ModelPerformance> simulate()
	{
		
		
		/*Company	Sector	Symbol	Weight	      Price	Chg	% Chg	Weighted Average
		Microsoft Corporation	Technology	MSFT	5.841368	   297.00	-0.31	(-0.10%)	33.3
		salesforce.com inc.	Technology	CRM	4.050827	   208.09	0	0.00%	23.14
		Apple Inc.	Technology	AAPL	3.22694	   164.34	-0.51	(-0.31%)	18.4
		International Business Machines Corporation	Technology	IBM	2.418519	   124.18	0	0.00%	13.7
		Cisco Systems Inc.	Technology	CSCO	1.08523	   56.00	-0.04	(-0.07%)	6.17
		Intel Corporation	Technology	INTC	0.926402	   47.65	-0.06	(-0.13%)	5.25*/
		
		Model model = new Model();
		
		List<Security> securitiesList = new ArrayList<Security>();
		
		Security security = new Security();
		security.setSecurityName("Microsoft");
		security.setTicker("MSFT");
		security.setAllocationPercentage(33.3);
		securitiesList.add(security);
		
		security = new Security();
		security.setSecurityName("salesforce.com");
		security.setTicker("CRM");
		security.setAllocationPercentage(23.14);
		securitiesList.add(security);
		
		security = new Security();
		security.setSecurityName("Apple");
		security.setTicker("AAPL");
		security.setAllocationPercentage(18.4);
		securitiesList.add(security);
		
		security = new Security();
		security.setSecurityName("IBM");
		security.setTicker("IBM");
		security.setAllocationPercentage(13.7);
		securitiesList.add(security);
		
		security = new Security();
		security.setSecurityName("Cisco");
		security.setTicker("CSCO");
		security.setAllocationPercentage(6.17);
		securitiesList.add(security);
		
		security = new Security();
		security.setSecurityName("Intel");
		security.setTicker("INTC");
		security.setAllocationPercentage(5.25);
		securitiesList.add(security);
		
		model.setAllocation(securitiesList);
		
		ModelPerformanceCalculator modelPerformanceCalculator = new ModelPerformanceCalculator();
		
		List<ModelPerformance> modelPerformanceHistory = modelPerformanceCalculator.calculate(model);
		//System.out.println(modelPerformanceHistory);
		return modelPerformanceHistory;
		
	}

}
