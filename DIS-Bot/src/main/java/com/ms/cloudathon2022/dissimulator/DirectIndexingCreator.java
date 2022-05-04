package com.ms.cloudathon2022.dissimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DirectIndexingCreator {

	// STOCK, PRICE, WEIGHT
	private static final String PRICES = "prices.csv";
	
	// FROM, TO, TICK SIZE, LOT SIZE
	private static final String BOARD_LOT = "boardlot.csv";


	static class Stock {
		String stock;
		double price;
		double weight;
		int lotSize;
		int shares;
		double totalPrice;
		double calculatedWeight;
		
		Stock(String stock, double price, double weight, int lotSize, int shares, double totalPrice, double calculatedWeight) {
			this.stock = stock;
			this.price = price;
			this.weight = weight;
			this.lotSize = lotSize;
			this.shares = shares;
			this.totalPrice = totalPrice;
		}
		
		@Override
		public String toString() {
			return String.format("\n%s, %s, %s%%, %,d, %,d, %,.2f, %.2f%%", 
					stock, price, weight, lotSize, shares, totalPrice, calculatedWeight);
		}
	}

	static class BoardLot {
		double from;
		double to;
		double tickSize;
		int lotSize;
		
		BoardLot(double from, double to, double tickSize, int lotSize) {
			this.from = from;
			this.to = to;
			this.tickSize = tickSize;
			this.lotSize = lotSize;
		}
	}
	
	public static void main(String[] args) throws Exception {
		URL priceResource;
			priceResource = DirectIndexingCreator.class.getClassLoader().getResource(PRICES);
			URL boardLotResource = DirectIndexingCreator.class.getClassLoader().getResource(BOARD_LOT);
		
		File pricesCsv = new File(priceResource.toURI());
		File boardLotCsv = new File(boardLotResource.toURI());

		List<BoardLot> boardLot = parseBoardLot(boardLotCsv);
		List<Stock> stocks = parseStocks(pricesCsv, boardLot);

		// get stock with max price
		Stock max = stocks.stream().max((p1, p2) -> new Double(p1.price).compareTo(p2.price)).get();
		// calculate total investment using the stock above
		double total = (max.price * max.lotSize) / (max.weight / 100);
		
		double actualTotal = 0;
		double totalCalculatedWeight = 0;
		for (Stock stock : stocks) {
			int shares = stock.lotSize;
			
			double weight = stock.price * shares * 100 / total;
			while (weight < stock.weight) {
				shares += stock.lotSize;
				weight = stock.price * shares * 100 / total;
			};
			if (weight > stock.weight) {
				weight = stock.price * (shares - stock.lotSize) * 100 / total;
				shares -= stock.lotSize;
			}
			
			stock.shares = shares;
			stock.totalPrice = shares * stock.price;
			stock.calculatedWeight = weight;
			actualTotal += stock.totalPrice;
			totalCalculatedWeight += weight;
		}
		System.out.println("STOCK, PRICE, WEIGHT, LOT SIZE, SHARES, TOTAL PRICE, CALCULATED WEIGHT");
		System.out.println(stocks);
		System.out.println(String.format("Total Requirement: %,.2f", actualTotal));
		System.out.println(String.format("Total Calculated Weight: %.2f%%", totalCalculatedWeight));
	}


	private static List<Stock> parseStocks(File file, List<BoardLot> boardLot) 
			throws FileNotFoundException {

		List<Stock> list = new ArrayList<>();
		
		Scanner in = new Scanner(file);
		String line;
		boolean headerRead = false;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!headerRead) {
				headerRead = true;
				continue;
			}
			String[] array = line.split(",");
			
			if (array[1].isEmpty()) {
				continue;
			}
			
			Stock stock = new Stock(
					array[0],
					Double.parseDouble(array[1]),
					Double.parseDouble(array[2]),
					getLotSize(Double.parseDouble(array[1]), boardLot),
					0,
					0d,
					0d);
			list.add(stock);
		}
		in.close();
		
		return list;
	}
	
	private static List<BoardLot> parseBoardLot(File file) throws FileNotFoundException {
		List<BoardLot> list = new ArrayList<>();
		
		Scanner in = new Scanner(file);
		String line;
		boolean headerRead = false;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!headerRead) {
				headerRead = true;
				continue;
			}
			String[] array = line.split(",");
			
			double to = 0d;
			try {
				to = Double.parseDouble(array[1]);

				BoardLot boardLot = new BoardLot(
						Double.parseDouble(array[0]),
						to,
						Double.parseDouble(array[2]),
						Integer.parseInt(array[3]));
				list.add(boardLot);
			} catch (Exception e) {
			}
			
		}
		in.close();
		
		return list;
	}
	
	private static int getLotSize(double price, List<BoardLot> board) {
		for (BoardLot boardLot : board) {
			if (boardLot.to == 0d || price <= boardLot.to) {
				return boardLot.lotSize;
			}
		}
		return 0;
	}
}