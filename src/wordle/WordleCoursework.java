/*
 * Code is divided into methods for better readability and debugging
 * ASCII table used to initialze letters in efficient way, best idea I ever had
 * Used purple Color to easier see
 * Static shares memory for all instances of class, they can be modified.
 * final doesnt let us modify.
 */
package wordle;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;

/**
 *
 * @author Ajmal
 */
public class WordleCoursework {
    // Are initialized only once at start of execution
    private static final WModel model = new WModel();
    private static boolean isGuessInWords;
    private static String guess;
    private static String answer;
    private static Scanner input;
    private static boolean playerHasWon;
    // Below used to display colored guess word
 
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREY_BACKGROUND = "\u001B[45m"; // grey color: \u001B[100m
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
   
    private static final int NO_STATE = -1, GREY_STATE = 0, GREEN_STATE = 1, YELLOW_STATE = 2;
    // Divide Alphabet into 4 categories according to line above
    private static HashMap<Character,Integer> availableLetters; 
    private static int guessStateColors[] = new int[5];
    
    
    private static void initialize(){
        answer = model.getRandomWord();
        input = new Scanner(System.in);
        playerHasWon = false;
        availableLetters = new HashMap<>();
        initializeAvailableLetters();
        System.out.println("ASdasdasd");
    }
    
    public static void main(String[] args) {
        initialize();
        System.out.println("--- WORDLE CLI VERSION ---");
        System.out.format("Guess a 5 Letter Word in %d Tries!\n"+
                "Green = Letter in Word and Correct Location\n"+
                "Yellow = Letter in Word but Wrong Location\n"+
                "Grey = Letter not in Word\n",model.getMAX_GUESSES());
        if(model.displayWordToBeGuessed())
                System.out.println("Answer = "+answer+" - FOR TESTING PURPOSES");
        System.out.println("-".repeat(27));

        for (int tries=0; tries < model.getMAX_GUESSES(); tries++){
            // Initialize guess colors to NONE every round
            Arrays.fill(guessStateColors,NO_STATE);            
            printAvailableLetters();
            // Receive Guess from player
            System.out.print("Enter Guess: ");
            guess = input.nextLine();
            guess = guess.toLowerCase();
            
            if(guess.equals(answer)){
                playerHasWon = true;
                break;
            }
            
            if(guess.length()!=5){
                System.out.println("Enter 5 Letters!");
                tries--;
                continue;
            }
            
            if(model.allowOnlyWordListGuesses()){
                isGuessInWords = model.isGuessInWordList(guess);
                if(!isGuessInWords){
                    System.out.println("Not in WordList!");
                    tries--;
                    continue;
                }   
            }
            
            System.out.print("- ");
            colorLettersInGuess(guess, guessStateColors);
            printColoredGuess(guess, guessStateColors);
            System.out.println(",  Tries Left: " + ((tries+1)-model.getMAX_GUESSES())*-1);
        }
        
        if(playerHasWon)
           System.out.println("YOU WIN! - Guessed Correctly !");
        else
           System.out.println("YOU LOST! - Out of Guesses !");
        
        // Print ANSWER
        System.out.println("ANSWER = "+ANSI_GREEN_BACKGROUND + answer.toUpperCase()+ ANSI_RESET);
    }

    private static void initializeAvailableLetters() {
        // Put a-z letters with NO_STATE in Hashmap
        for(int asciiValue = 97; asciiValue <= 122; asciiValue++)
            availableLetters.put((char)asciiValue,NO_STATE);
    }

    public static void colorLettersInGuess(String guess, int[] guessStateColors) {
        for (int i=0; i < 5;i++){
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);
            if(guessChar==answerChar){
                guessStateColors[i] = GREEN_STATE;
                availableLetters.replace(guessChar,GREEN_STATE);
            }
            else if (answer.contains(Character.toString(guessChar))){
                for(int j = 0; j < 5; j++){
                    if(guessChar==answer.charAt(j)&&guessStateColors[j]!=GREEN_STATE){
                        guessStateColors[i] = YELLOW_STATE;
                        if(availableLetters.get(guessChar)!=GREEN_STATE)
                            availableLetters.replace(guessChar,YELLOW_STATE);
                    }
                }
            }
            else{
                guessStateColors[i] = GREY_STATE;
                availableLetters.replace(guessChar,GREY_STATE);
            }
        }
    }
    
    public static String getColorByState(int COLOR_STATE){
        if(COLOR_STATE==GREEN_STATE)
            return ANSI_GREEN_BACKGROUND;
        else if(COLOR_STATE==YELLOW_STATE)
            return ANSI_YELLOW_BACKGROUND;
        else
            return ANSI_GREY_BACKGROUND;
    }
    public static void printColoredGuess(String guess, int[] guessStateColors){
        for (int i=0; i < 5;i++){
            int COLOR_STATE = guessStateColors[i];
            String COLOR = getColorByState(COLOR_STATE);
            System.out.print(COLOR + guess.toUpperCase().charAt(i)+ ANSI_RESET);
        }
    }
    public static void printAvailableLetters(){
        for (Character key: availableLetters.keySet()) {
            String letter = key.toString().toUpperCase();
            int COLOR_STATE = availableLetters.get(key);
            if(COLOR_STATE == -1)
                System.out.print(letter);
            else{
                String COLOR = getColorByState(COLOR_STATE);
                System.out.print(COLOR+letter+ ANSI_RESET);
            }
        }
        System.out.println();
    }
}

