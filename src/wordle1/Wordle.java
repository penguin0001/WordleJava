package wordle1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wordle {
	
	private List<String> grid;
	private String target;
	private String fileName = "C:\\Users\\robyn\\Documents\\documents\\general-workspace\\WordleCloneNew\\src\\wordle1\\dictionary.txt";
	private String saveFile = "C:\\Users\\robyn\\Documents\\documents\\general-workspace\\WordleCloneNew\\src\\wordle1\\savefile.txt";
	private List<String> dictionary;
	private String qwerty = "qwertyuiopasdfghjklzxcvbnm";
	private LinkedHashMap<Character, Integer> letters;
	
	public Wordle() {
		grid = new ArrayList<String>();
		this.dictionary = new ArrayList<String>();
		letters = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < qwerty.length(); i++) {
			letters.put(qwerty.charAt(i), 0);			
		}
	}
	
	private void printGrid() {
		for (String word : grid) {
			System.out.println(formatLetter(word));
		}
	}
	
	private void printLetters() {
		String print = "";
		int i = 0;
		
		for (Character c : letters.keySet()) {
			i++;
			if (i==11) print += "\n ";
			if (i==20) print += "\n  ";
			switch (letters.get(c)) {
			case 0:
				print += " [" + c + "] ";
				break;
			case 1:
				print += " [ ] ";
				break;
			case 2:
				print += " [" + c + "'] ";
				break;
			case 3:
				print += " [" + c + "*] ";
				break;
			}
			
		}
		System.out.println(print);
	}
	
	private String formatLetter(String word) {
		String newWord = "";
		for (int i = 0; i < word.length(); i++) {
			if (target.charAt(i) == word.charAt(i)) {
				newWord += Character.toString(word.charAt(i)).toUpperCase() + "* ";
				letters.put(word.charAt(i), 3);
			} else if (target.contains("" + word.charAt(i))) {
				newWord += Character.toString(word.charAt(i)).toUpperCase() + "' ";
				letters.put(word.charAt(i), 2);
			} else {
				newWord += Character.toString(word.charAt(i)).toUpperCase() + "  ";
				letters.put(word.charAt(i), 1);
			}
		}
		return newWord;
	}
	
	private boolean guess() {
		Scanner scanner = new Scanner(System.in);
		String guess = "";
		boolean valid = false;
		while (!valid) {
			System.out.println("Enter 5 letter word: ");
			guess = scanner.nextLine();
			if (guess.length() == 5 && dictionary.contains(guess)) {valid = true;}
			else {System.out.println("Invalid word");}
		} 
		
		grid.add(guess);
		//scanner.close();
		
		if (guess.equals(this.target)) { 
			return true;
		} else {
			return false;
		}
		
	}
	
	private boolean containsBadChars(String word) {
		if (word.contains("ú") || 
				word.contains("ó") ||
				word.contains("ò") ||
				word.contains("í") ||
				word.contains("é") ||
				word.contains("è") ||
				word.contains("à") ||
				word.contains("á") ||
				word.contains("À") ||
				word.contains("È") ||
				word.contains("Í") ||
				word.contains("Ò") ||
				word.contains("-") ||
				word.contains("ï") ||
				word.contains("ç") ||
				word.contains(" ") ||
				word.contains(".") ||
				word.contains("ü") ||
				word.contains("Q") ||
				word.contains("W") ||
				word.contains("E") ||
				word.contains("R") ||
				word.contains("T") ||
				word.contains("Y") ||
				word.contains("U") ||
				word.contains("I") ||
				word.contains("O") ||
				word.contains("P") ||
				word.contains("A") ||
				word.contains("S") ||
				word.contains("D") ||
				word.contains("F") ||
				word.contains("G") ||
				word.contains("H") ||
				word.contains("J") ||
				word.contains("K") ||
				word.contains("L") ||
				word.contains("Z") ||
				word.contains("X") ||
				word.contains("C") ||
				word.contains("V") ||
				word.contains("B") ||
				word.contains("N") ||
				word.contains("M")
		) {
			return true;
		} else {
			return false;
		}
	}	
	
	// add valid words into our dictionary
	private void getWords(String fileName) {
		ArrayList<String> lines = readFileLines(fileName);
		for (String l : lines) {
			if (l.length() == 5 && !dictionary.contains(l) && !containsBadChars(l)) {
				dictionary.add(l);
			}
		}
	}
	
	
	private ArrayList<String> readFileLines(String fileName) {
		BufferedReader buffer = null;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			InputStream in = new FileInputStream(fileName);
			buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = buffer.readLine();
			while (line != null) {
				lines.add(line);
				line = buffer.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					System.out.println("Could not close file");
					e.printStackTrace();
				}
			}
		}
		
		return lines;
	}
	
	private void saveToFile(String fileName, String score) {
		
		try {
			    FileWriter writer = new FileWriter(fileName); 
			    writer.write(score);
			    writer.write("\n");
			    writer.close();
		} catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		
	}
	
	// saves dictionary to file
	private void saveDictToFile(String fileName) {
		
		try {
			    FileWriter writer = new FileWriter(fileName); 
			    for (String word: dictionary) {
			    	writer.write(word);
			    	writer.write("\n");
			    }
			    writer.close();
		} catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		
	}
	
	private HashMap<String, Integer> getScores(String fileName) {
		ArrayList<String> scores = readFileLines(fileName);
		HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
		for (String l : scores) {
			if (scoreMap.get(l) != null) {
				scoreMap.put(l, scoreMap.get(l) + 1);
			} else {
				scoreMap.put(l, 1);
			}
			
		}
		
		return scoreMap;
	}
	
	private void printScores(String fileName) {
		HashMap<String, Integer> scores = getScores(fileName);
		for (String s : scores.keySet()) {
			System.out.println(s + ": " + scores.get(s));
		}
	}
	
	// play the game
	public void play() {
		getWords(fileName);
		saveDictToFile("C:\\Users\\robyn\\Documents\\documents\\general-workspace\\WordleCloneNew\\src\\wordle1\\wordle-safe-en");
		target = dictionary.get((int)Math.floor(Math.random()*(dictionary.size())));
		grid.clear();
		boolean correct = false;
		while(grid.size() < 6 && !correct) {
			if(guess()) correct = true;
			printGrid();
			printLetters();
		}
		if (correct) {
			String score = "" + grid.size();
			saveToFile(saveFile, score);
			System.out.println("WIN!");
			System.out.println("You took " + score + " tries.");
		} else {
			System.out.println("FAIL");
			System.out.println("Word was: " + target);
		}
		
		// retrieve previous scores from a text file and print them
		
		printScores(saveFile);
	}
	
	
	
}
