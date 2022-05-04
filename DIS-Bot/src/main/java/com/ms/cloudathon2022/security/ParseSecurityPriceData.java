package com.ms.cloudathon2022.security;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class ParseSecurityPriceData {
	
	public static void main(String args[]) throws Exception
	{
		ParseSecurityPriceData test = new ParseSecurityPriceData();
		test.parseHistory("AAPL");
	}

	public  List<SecurityHistory> parseHistory(String ticker)
	{
		
		/*System.out.println("*******************************");
		System.out.println("Getting Stock History for: "+ticker);
		System.out.println("*******************************");*/
		
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("Date", "date");
		mapping.put("Open", "open");
		mapping.put("High", "high");
		mapping.put("Low", "low");
		mapping.put("Close", "close");
		mapping.put("Adj Close", "adjClose");
		mapping.put("Volume", "volume");
		
		
		 HeaderColumnNameTranslateMappingStrategy<SecurityHistory> strategy = new HeaderColumnNameTranslateMappingStrategy<SecurityHistory>();
	        strategy.setType(SecurityHistory.class);
	        strategy.setColumnMapping(mapping);
		
	        CSVReader csvReader = null;
	        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	        InputStream in = classloader.getResourceAsStream(ticker.toUpperCase()+".csv");
			InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			csvReader = new CSVReader(reader);
	        CsvToBean csvToBean = new CsvToBean();
	        
	        // call the parse method of CsvToBean
	        // pass strategy, csvReader to parse method
	        List<SecurityHistory> stockHistoryList = csvToBean.parse(strategy, csvReader);
	        
	        return stockHistoryList;
	        
	}


}
