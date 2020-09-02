package Mastermind;

import java.util.Scanner;
import java.util.Arrays;

public class Mastermind {
	
	// CONFIG
	static final int COMBINATION_LENGTH = 4;
	static final int NUMBER_OF_TRY = 12;
	//END CONFIG
	
	//Result constants
	static final int GOOD_PLACEMENT = 0;
	static final int GOOD_COLOR = 1;
	
	//Error management
	static final int VALID = 0;
	static final int BAD_LENGTH = 1;
	static final int DOUBLE_COLOR = 2;
	static final int WRONG_INPUT = 3;

	public enum Colors {
		r("ROUGE", "r"),
		j("JAUNE", "j"),
		ve("VERT", "ve"),
		ble("BLEU", "ble"),
		o("ORANGE", "o"),
		bla("BLANC", "bla"),
		vi("VIOLET", "vi"),
		f("FUSHIA", "f");
		
		private String color;
		private String shortName;
		
		Colors(String color, String shortName) {
			this.color = color;
			this.shortName = shortName;
		}
		
		public String getName() {
			return color;
		}

		public String getShortName() {
			return shortName;
		}
		
		public String getNameFormated(int lengthNeeded) {
			String formatedColor = color;
			while (formatedColor.length() < lengthNeeded) {
				formatedColor += " ";
			}
			return formatedColor;	
		}
	}
	
	public static Colors getRandomColor() {
		int index = (int) (Math.random()*Colors.values().length);
		return Colors.values()[index];
	}
	
	public static Colors[] getCombination() {
		Colors [] combination = new Colors[COMBINATION_LENGTH];
		
		for (int i=0; i<COMBINATION_LENGTH; i++) {
			boolean colorConflict = false;
			do {
				combination[i] = getRandomColor();
				for (int j=i-1; j>=0; j--) {
					if (combination[j] == combination[i]) {
						colorConflict = true;
						break;
					} else {
						colorConflict = false;
					}
				}
			} while (colorConflict);
		}		
		return combination;
	}
	
	public static void displayWelcomeMessage() {
		System.out.println();
		System.out.println("          ███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███╗   ███╗██╗███╗   ██╗██████╗");
		System.out.println("          ████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗████╗ ████║██║████╗  ██║██╔══██╗");
		System.out.println("          ██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝██╔████╔██║██║██╔██╗ ██║██║  ██║");
		System.out.println("          ██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗██║╚██╔╝██║██║██║╚██╗██║██║  ██║");
		System.out.println("          ██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║██║ ╚═╝ ██║██║██║ ╚████║██████╔ ");
		System.out.println("          ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═════╝");
		System.out.println();
	}
	
	public static void displayBadInput(int errorCode) {
		switch(errorCode) {
			case BAD_LENGTH:
				System.out.println("La longueur de votre proposition est incorrecte");
				break;
			
			case DOUBLE_COLOR:
				System.out.println("Vous ne pouvez pas saisir de doublons (il n'y en a pas dans la solution)");
				break;
				
			case WRONG_INPUT:
				System.out.println("Une des couleurs que vous avez saisi n'existe pas. (Vous devez utiliser les abbréviations fournies)");
				break;
		}
		System.out.println("Votre réponse n'est pas valide. Votre tour n'a pas été compté");
	}

	public static void displayColorList() {
		int colorsQuantity = Colors.values().length;
		String shortNamesList = "";
		for (int i=0; i<colorsQuantity;i++) {
			shortNamesList += Colors.values()[i].getName()+" : "+Colors.values()[i].getShortName()+" | ";
		}
		shortNamesList = shortNamesList.substring(0, shortNamesList.length()-2);
		System.out.println();
		System.out.println("Saisir votre combinaison séparée par des espaces, en utilisant les abréviations suivantes:");
		System.out.println(shortNamesList);
		System.out.println();
	}
	
	public static int[] getColumnSize(Colors [][] resultHistory, int currentTurn) {
		int[] columnSize = new int[COMBINATION_LENGTH];
		
		for (int i=0; i<COMBINATION_LENGTH; i++) {
			columnSize[i] = 0;
			for (int j=0; j<currentTurn; j++) {
				if (columnSize[i] < resultHistory[j][i].getName().length()) {
					columnSize[i] = resultHistory[j][i].getName().length();
				}
			}
		}
		return columnSize;
	}
	
	public static String spaces(int numberOfSpaces) {
		String spaces = "";
		while (spaces.length() < numberOfSpaces) {
			spaces += " ";
		}
		return spaces;
	}
	
	public static String line(int length) {
		String line = "";
		while (line.length() < length) {
			line += "_";
		}
		return line;
	}
	
	public static void displayHistory(int currentTurn, Colors[][] answerHistory, int[][] resultHistory) {
		int[] columnSize = getColumnSize(answerHistory, currentTurn);
		for (int i=0; i<currentTurn; i++) {
			int spacesToAdd = Integer.toString(currentTurn).length() - Integer.toString(i+1).length();
			String answer = "| Essai N°"+(i+1)+" : "+spaces(spacesToAdd)+"| ";
			for (int j=0; j<COMBINATION_LENGTH; j++) {
				answer += answerHistory[i][j].getNameFormated(columnSize[j])+" | ";
			}
			answer += "Mal placés: "+resultHistory[i][GOOD_COLOR]+" | ";
			answer += "Bien placés: "+resultHistory[i][GOOD_PLACEMENT]+" | ";
			
			if (i == 0 ) {
				System.out.println(line(answer.length()-1));
			}
			System.out.println(answer);
		}
	}
	
	public static void displayNewTurnMessage(int tour, int toursRestants, int validAnswer, Colors[][] answerHistory, int[][] resultHistory) {
		System.out.println();
		if (validAnswer != VALID) {
			displayBadInput(validAnswer);
		}
		displayColorList();
		if (toursRestants != NUMBER_OF_TRY) {
			displayHistory(tour, answerHistory, resultHistory);
		}
		if (toursRestants > 1) {
			System.out.println("Il vous reste "+toursRestants+" tours");	
		} else {
			System.out.println("C'est votre dernier tour!");
		}
	}
	
	public static void displaySolution(Colors[] combination) {
		String solution = "";
		System.out.println("La solution était: ");
		for (int i=0; i<COMBINATION_LENGTH; i++) {
			solution += combination[i].getName()+" | ";
		}
		System.out.println(solution.substring(0, solution.length()-2));
	}
	
	public static int checkAnswer(String[] answer) {
		int colorsQuantity = Colors.values().length;
		String [] validInput = new String[colorsQuantity];
		for (int i=0; i< colorsQuantity; i++) {
			validInput[i] = Colors.values()[i].getShortName();
		}
		
		if (answer.length != COMBINATION_LENGTH) {
			return BAD_LENGTH;
		}
		
		int answerLength = answer.length;
		for(int i=0; i<answerLength; i++) {
			for (int j=i+1; j<answerLength; j++) {
				if (answer[i].equals(answer[j])) {
					return DOUBLE_COLOR;
				}
			}
			if (!Arrays.asList(validInput).contains(answer[i])) {
				return WRONG_INPUT;
			}
		}
		return VALID;
	}

	public static int[] analyseAnswer(Colors[] combination, Colors[] answer) {
		int [] result = {0, 0};
		
		for (int i=0; i<answer.length; i++) {
			for (int j=0; j<combination.length; j++) {
				if (answer[i] == combination[j] && i == j) {
					result[GOOD_PLACEMENT]++;
				} else if (answer[i] == combination[j]) {
					result[GOOD_COLOR]++;
				}
			}
		}		
		return result;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int validAnswer = 0;
		String[] answer = new String[COMBINATION_LENGTH];
		Colors[][] answerHistory = new Colors[NUMBER_OF_TRY][COMBINATION_LENGTH];
		int[] result = new int[2];
		int[][] resultHistory = new int[NUMBER_OF_TRY][2];
		boolean win = false;
		Colors combination[] = getCombination();
		
		displayWelcomeMessage();
		
		for (int currentTurn=0, remainingTurns = NUMBER_OF_TRY; currentTurn<NUMBER_OF_TRY; currentTurn++, remainingTurns--, validAnswer=VALID) {
			do {
				displayNewTurnMessage(currentTurn, remainingTurns, validAnswer, answerHistory, resultHistory);
				answer = sc.nextLine().split(" ");
				validAnswer = checkAnswer(answer);
			} while (validAnswer != VALID);
			
			for (int i=0; i<COMBINATION_LENGTH; i++) {
				answerHistory[currentTurn][i] = Colors.valueOf(answer[i]);
			}
			result = analyseAnswer(combination, answerHistory[currentTurn]);
			resultHistory[currentTurn][GOOD_PLACEMENT] = result[GOOD_PLACEMENT];
			resultHistory[currentTurn][GOOD_COLOR] = result[GOOD_COLOR];
			
			if (result[GOOD_PLACEMENT] == COMBINATION_LENGTH) {
				win = true;
				break;
			}	
		}
		if (win) {
			System.out.println("Vous avez gagné!");
		} else {
			System.out.println("Désolé, vous avez perdu...");
		}
		displaySolution(combination);
		sc.close();
	}
}
