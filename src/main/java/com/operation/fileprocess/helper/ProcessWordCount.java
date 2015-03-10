package com.operation.fileprocess.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import com.operation.fileprocess.mapper.WordAnalyzerMapper;

public class ProcessWordCount {
	
	 public static String outputFile = null;

	static Map<String, Integer> wordCount = new HashMap<String, Integer>();
	static String DELIMITER = ",";
	static Map<String, String> headerRow = new HashMap<String, String>();
	LinkedHashMap<Integer, WordAnalyzerMapper> searchMap = new LinkedHashMap<Integer, WordAnalyzerMapper>();
	static Integer mapcount = 0;

	public synchronized static void wordProcess(String currentWord) {

		if (wordCount.containsKey(currentWord)) {
			int count = wordCount.get(currentWord).intValue();
			wordCount.put(currentWord, new Integer(count + 1));
		} else {
			wordCount.put(currentWord, new Integer(1));
		}
	}

	public void writeToCSV() throws IOException {
		File csvOutputFile = new File(outputFile + "/ProcessedFile.csv");
		FileWriter fw = new FileWriter(csvOutputFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		headerRow.put("word", "count");
		for (Entry<String, String> headerRow : headerRow.entrySet()) {
			if (!csvOutputFile.exists()) {
				csvOutputFile.createNewFile();
			}
			bw.write(headerRow.getKey() + DELIMITER + headerRow.getValue());
			bw.newLine();
		}
		for (Entry<String, Integer> entry : wordCount.entrySet()) {
			if (!csvOutputFile.exists()) {
				csvOutputFile.createNewFile();
			}
			bw.write(entry.getKey() + DELIMITER + entry.getValue());
			bw.newLine();
		}
		bw.close();
		System.out.println("CSV file generated with : " + wordCount.size());

	}

	@SuppressWarnings("resource")
	public void optionOne() {
		Scanner optionOneScanner = new Scanner(System.in);
		System.out.println("*** Please enter the word to search ****");
		String optOneInput = optionOneScanner.next();
		boolean isWordFound = false;
		for (Entry<Integer, WordAnalyzerMapper> entry : searchMap.entrySet()) {
			if (optOneInput.equals(entry.getValue().getKey())) {
				System.out.println("Found word :" + entry.getValue().getKey() + "And its Rank is : " + entry.getKey());
				isWordFound = true;
			}
		}
		if (!isWordFound)
			System.out.println("** Word Not Found **");
		mainOption();
	}

	@SuppressWarnings("resource")
	public void optionTwo() {
		Scanner optionTwoScanner = new Scanner(System.in);
		System.out.println("*** Please enter the word to search ***");
		String optTwoInput = optionTwoScanner.next();
		System.out.println("Enter S if you want to go towards the Start else E towards the End!");
		String optSeek = optionTwoScanner.next();
		System.out.println("Also Enter the position you want to traverse from this current word : ");
		Integer optTwoIndex = optionTwoScanner.nextInt();
		int currentPosition = 0;
		int difference = 0;
		for (Entry<Integer, WordAnalyzerMapper> entry : searchMap.entrySet()) {
			if (optTwoInput.equals(entry.getValue().getKey())) {
				System.out.println("Found word :" + entry.getValue().getKey() + " And its Rank is : " + entry.getKey());
				currentPosition = entry.getKey();
				// System.out.println(currentPosition);

			}
		}

		if (optSeek.equalsIgnoreCase("S")) {
			difference = currentPosition - optTwoIndex;
			if (difference > 0) {
				for (Entry<Integer, WordAnalyzerMapper> entry : searchMap.entrySet()) {
					if (difference == entry.getKey().intValue()) {
						System.out.println("Found word :" + entry.getValue().getKey());
						break;
					}
				}

			} else {
				System.out.println(" Please enter a valid position!");
			}
			mainOption();
		} else if (optSeek.equalsIgnoreCase("E")) {
			System.out.println("** Current: " + currentPosition + " option: " + optTwoIndex.intValue() + " mapcount: "
					+ mapcount);
			Integer searchIndex = currentPosition + optTwoIndex.intValue();
			if (searchIndex <= mapcount) {

				for (Entry<Integer, WordAnalyzerMapper> entry : searchMap.entrySet()) {
					if (searchIndex.equals(entry.getKey())) {
						System.out.println("Found word :" + entry.getValue().getKey());
					}
				}
			} else {
				System.out.println("Please enter a valid option : outOfSearchBound");
			}
			mainOption();

		} else {
			System.out.println("Please start over !");
			mainOption();
		}

	}

	@SuppressWarnings("resource")
	public void optionThree(){
		Scanner optionThreeScanner = new Scanner(System.in);
		System.out.println("*** Please enter the rank number : ***");
		int optOneInput = optionThreeScanner.nextInt();
		boolean isKeyFound = false;
		for (Entry<Integer, WordAnalyzerMapper> entry : searchMap.entrySet()) {
			if (optOneInput == (entry.getKey())) {
				System.out.println("Found word :" + entry.getValue().getKey() + " And its word count is : " + entry.getValue().getValue());
				isKeyFound = true;
			}
		}
		if (!isKeyFound)
			System.out.println("** Key Not Found **");
		mainOption();
		
	}
	
	public void optionFour() {
		System.out.println("Thank you! have a good day!");
		System.exit(0);
	}

	public void mainOption() {
		int input = 0;
		@SuppressWarnings("resource")
		Scanner newScanner = new Scanner(System.in);
		System.out.println("Please enter the functionality which needs to be performed : ");
		System.out.println("1. Press 1 to Find the word count of the respective Word! ");
		System.out.println("2. Press 2 to Find the back and forth of the number with index mentioned!");
		System.out.println("3. Press 3 to Search word by Rank!");
		System.out.println("4. Press 4 to Exit");

		System.out.println("please enter an option to proceed! ");
		try {
			input = newScanner.nextInt();
			switch (input) {
			case 1:
				optionOne();
				break;
			case 2:
				optionTwo();
				break;
			case 3:
				optionThree();
				break;
			case 4:
				optionFour();
				break;
			default:
				System.out.println("** Please Enter a valid Option **");
			}
		} catch (InputMismatchException e) {
			System.out.println(" Please Enter a valid Number ");
			mainOption();

		}

	}

	public void wordAnalyzer() {

		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(wordCount.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (java.util.Iterator<Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Integer> entry : sortedMap.entrySet()) {
			mapcount = mapcount + 1;
			searchMap.put(mapcount, new WordAnalyzerMapper(entry.getKey(), (long) entry.getValue()));

		}
		mainOption();
	}

}
