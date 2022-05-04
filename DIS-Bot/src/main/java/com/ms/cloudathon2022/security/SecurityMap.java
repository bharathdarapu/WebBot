package com.ms.cloudathon2022.security;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

public class SecurityMap {
    private static final HashMap<String, String> tickerMap = new HashMap<String,String>();

    static {
    	
    	tickerMap.put("GOOGL", "Alphabet or Google or GOOGL");
    	tickerMap.put("AAPL", "Apple Inc or AAPL");
    	tickerMap.put("MSFT", "Microsoft Corp or MSFT");
    	tickerMap.put("AMZN", "Amazon or AMZN");
    	tickerMap.put("JNJ", "Johnson & Johnson or JNJ");
    	tickerMap.put("XOM", "Exxon or XOM");
    	tickerMap.put("TSLA", "Tesla or TSLA");
    	tickerMap.put("NVDA", "NVIDIA Corp or NVDA");
    	tickerMap.put("FB", "Meta or FB or Facebook");
    	tickerMap.put("JPM", "JPMorgan Chase & Co or JPM");
    	tickerMap.put("BRK-B", "Berkshire Hathaway Inc Class B or BRK");
    	tickerMap.put("VOO", "S&P 500 or VOO");
    	tickerMap.put("IBM", "IBM");
    	tickerMap.put("CRM", "sales force OR salesforce or CRM");
    	tickerMap.put("CSCO", "Cisco or CSCO");
    	tickerMap.put("INTC", "Intel or INTC");
    	tickerMap.put("DJI", "DOW Jones OR DJI OR Dow.");
    	
    }
    
    public static String findTicker(String securityName) {
    	for (Entry<String, String> entry : tickerMap.entrySet()) {
            if (entry.getValue().toLowerCase().contains(securityName.toLowerCase()))
            		return entry.getKey();
            		
        }
    	
    	return null;
    }
    
}