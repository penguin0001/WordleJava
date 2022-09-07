package wordle1;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//
		/**
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		System.out.println(input);
		ArrayList<String> list = new ArrayList<String>();
		list.add(input);
		System.out.println(list);
		s.close();
		*/
		
		Wordle wordle = new Wordle();
		wordle.play();
		
	}

}
