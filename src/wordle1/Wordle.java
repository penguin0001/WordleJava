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
	
	// list of words the player has guessed
	private List<String> grid;
	// word the player must guess
	private String target;
	// text file containing every word in the English language
	private String fileName = "C:\\Users\\robyn\\Documents\\documents\\general-workspace\\WordleCloneNew\\src\\wordle1\\dictionary.txt";
	// text file where the players stats are stored
	private String saveFile = "C:\\Users\\robyn\\Documents\\documents\\general-workspace\\WordleCloneNew\\src\\wordle1\\savefile.txt";
	// list of every word in the English language, taken from the text file
	private List<String> dictionary;
	// list of letters on a standard keyboard- these are what the player can use to guess
	private String qwerty = "qwertyuiopasdfghjklzxcvbnm";
	private LinkedHashMap<Character, Integer> letters;
	
	// initialise empty grid and letters
	public Wordle() {
		grid = new ArrayList<String>();
		this.dictionary = new ArrayList<String>();
		letters = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < qwerty.length(); i++) {
			letters.put(qwerty.charAt(i), 0);			
		}
	}
	
	// print the words the player has guessed so far, formatting the letters according to whether they are correct
	private void printGrid() {
		for (String word : grid) {
			System.out.println(formatLetter(word));
		}
	}
	
	// print the virtual keyboard
	// letters that have already been eliminated are removed from the keyboard
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
	
	// correctly positioned letters have an asterisk, incorrectly positioned letters have an apostrophe, non-present letters have nothing
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
	
	// take input from the player, checking if it's a valid guess (i.e., 5 letters long and a real word)
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
	
	// used for filtering any problematic words out of the dictionary (i.e. ones that contain capitals or special characters)
	// also useful for adding foreign dictionaries if desired
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
	
	// convert a text file to a list
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
	
	// save the player's score to a text file
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
	
	
	// get the player's scores from the text file
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
	
	// print the players previous scores
	private void printScores(String fileName) {
		HashMap<String, Integer> scores = getScores(fileName);
		for (String s : scores.keySet()) {
			System.out.println(s + ": " + scores.get(s));
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
	
	public void printIntro() {
		System.out.println("Welcome to Wordle Java, here's how to play: ");
		System.out.println("");
		System.out.println("A random 5-letter word will be chosen. You have 6 tries to guess the word correctly.");
		System.out.println("Your previous guesses will be displayed in a grid, with the letters marked according to how correct they are: ");
		System.out.println("-Letters marked with an asterisk (e.g. A*) are present in the target word and are in the correct place.");
		System.out.println("-Letters marked with an apostrophe are present in the target word but are in the wrong place.");
		System.out.println("-Letters with no mark are not present in the target word.");
		System.out.println("The same goes for the letters in the keyboard, except incorrect letters will be removed entirely.");
		System.out.println("");
		System.out.println("You will be given a summary of all your scores at the end of each game.");
		System.out.println("Good luck!");
		System.out.println("");
	}
	
}
