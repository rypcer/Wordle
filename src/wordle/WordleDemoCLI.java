package wordle;

import java.util.Scanner;

/**
 *
 * @author Ajmal
 */
public class WordleDemoCLI {
    
    // Are initialized only once at start of execution
    private static final WModel model = new WModel();
    private static Scanner input = new Scanner(System.in);
    private static String guess;
    // Below used to display colored guess word in Console
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREY_BACKGROUND = "\u001B[100m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    
    public static void main(String[] args) {

        System.out.println("--- WORDLE CLI VERSION ---");
        System.out.format("Guess a %d Letter Word in %d Tries!\n"+
                "Green = Letter in Word and Correct Location\n"+
                "Yellow = Letter in Word but Wrong Location\n"+
                "Grey = Letter not in Word\n",model.MAX_GUESS_LENGTH,model.MAX_GUESSES);
        if (model.alwaysShowAnswer())
                System.out.println("Answer = "+ model.getAnswer() +
                        " - FOR TESTING PURPOSES");
        System.out.println("-".repeat(27));

        while (model.getCurrentGuessTry() < model.MAX_GUESSES){
            System.out.println("\n");
            //printAvailableLettersWithColor();
            printAvailableLetters();
            
            // Receive Guess from player
            System.out.print("Enter Guess: ");
            guess = input.nextLine().toLowerCase();
            if (guess.length() != model.MAX_GUESS_LENGTH){
                System.out.format("Enter %d Letters!\n",model.MAX_GUESS_LENGTH);
                continue;
            }
            model.setGuess(guess);
            model.submitGuess(); // clears model.guess

            if (model.isWordNotInList()){
                 System.out.println("Not in WordList!");
                 continue;
            }
            if (model.hasPlayerWon()){
                break;
            }
            System.out.print("- ");
            printColoredGuess(guess);
            System.out.println(",  Tries Left: " + 
                    (model.MAX_GUESSES - model.getCurrentGuessTry()));
        }
        
        if (model.hasPlayerWon())
           System.out.println("YOU WIN! - Guessed Correctly !");
        else
           System.out.println("YOU LOST! - Out of Guesses !");

        System.out.println("ANSWER = "+ ANSI_GREEN_BACKGROUND + 
                model.getAnswer().toUpperCase()+ ANSI_RESET);
    }

   
    public static void printColoredGuess(String guess){
        for (int i = 0; i < model.MAX_GUESS_LENGTH; i++){
            int COLOR_STATE = model.getGuessStateColor(i); 
            String COLOR = getANSIColorByState(COLOR_STATE);
            System.out.print(COLOR + guess.toUpperCase().charAt(i) + ANSI_RESET);
        }
    }
    
    public static String getANSIColorByState(int COLOR_STATE){
        if (COLOR_STATE == model.GREEN_STATE)
            return ANSI_GREEN_BACKGROUND;
        else if (COLOR_STATE == model.YELLOW_STATE)
            return ANSI_YELLOW_BACKGROUND;
        else
            return ANSI_GREY_BACKGROUND;
    }
    
    public static void printAvailableLetters(){
        String labels[] = {"Green","Yellow","Grey","Available"};
        int states[] = {model.GREEN_STATE,model.YELLOW_STATE,
            model.GREY_STATE,model.NO_STATE};
        String letter;
        int COLOR_STATE;
        for (int i = 0; i < states.length; i++){
            System.out.print(labels[i]+"- ");
            for (Character key : model.getAvailableLetters().keySet()) {
                letter = key.toString().toUpperCase();
                COLOR_STATE = model.getAvailableLetters().get(key);
                if (COLOR_STATE == states[i])
                    System.out.print(letter);
            }
            System.out.println();
        }
    }
    // Alternative One liner
    public static void printAvailableLettersWithColor(){
        String letter;
        int COLOR_STATE;
        for (Character key : model.getAvailableLetters().keySet()) {
            letter = key.toString().toUpperCase();
            COLOR_STATE = model.getAvailableLetters().get(key);
            if (COLOR_STATE == model.NO_STATE)
                System.out.print(letter);
            else{
                String COLOR = getANSIColorByState(COLOR_STATE);
                System.out.print(COLOR+letter+ ANSI_RESET);
            }
        }
        System.out.println();
    }
}

