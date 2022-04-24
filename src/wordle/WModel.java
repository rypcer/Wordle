/* 
 * ASCII table used to initialize letters in efficient way, best idea I ever had
 * Static shares memory for all instances of class, they can be modified.
 * final doesn't let us modify.
*/
package wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * 
 * @author Ajmal
 */
public class WModel extends Observable {
    
    private final int MAX_GUESSES = 6; // do we need to have a getter for constants?
    public final int NO_STATE = -1, GREY_STATE = 0, GREEN_STATE = 1, YELLOW_STATE = 2;
    public final int EMPTY_STATE = -2;
    // Divide Alphabet into 4 categories according to line above
    private HashMap<Character,Integer> availableLetters; 
    private int guessStateColors[] = new int[5];
    // 3 Flags
    private boolean allowOnlyWordListGuesses; 
    private boolean showAnwser;
    private boolean selectRandomGuessWord;
    
    private final List<String> targetWords;
    private final List<String> guessWords;
    
    private String answer;
    private static boolean playerHasWon;
    public String guess = new String(); // ADD IN CLI aswell
    public WModel(){
        allowOnlyWordListGuesses = true;
        showAnwser= true;
        selectRandomGuessWord = true;
        playerHasWon = false;
        
        targetWords = loadInFromFile("src/wordle/data/common.txt");
        guessWords = loadInFromFile("src/wordle/data/words.txt");
        availableLetters = new HashMap<>();
        answer = getRandomWord();
        initializeAvailableLetters();
        resetGuessColors();
    }
    //======================================================= MODEL NEEDS TO UPDATE OBSERVERS! 
    
        
    // Getter & Setters
    
    public boolean allowOnlyWordListGuesses() {return allowOnlyWordListGuesses;}
    public boolean showAnwser() {return showAnwser;}
    public boolean selectRandomGuessWord() {return selectRandomGuessWord;}
    public int getMAX_GUESSES() {return MAX_GUESSES;}
    public int getNO_STATE() {return NO_STATE;}
    public int getGREY_STATE() {return GREY_STATE;}
    public int getGREEN_STATE() {return GREEN_STATE;}
    public int getYELLOW_STATE() {return YELLOW_STATE;}
    public String getAnswer() {return answer;}
    public static boolean getPlayerHasWon() {return playerHasWon;}
    public static void setPlayerHasWon(boolean aPlayerHasWon) {
        playerHasWon = aPlayerHasWon;}
    public HashMap<Character,Integer> getAvailableLetters() {
        return availableLetters;}
    public int getGuessStateColor(int index) {return guessStateColors[index];}
    public void setGuessStateColor(int index, int state) {
        assert state >= NO_STATE && state <= YELLOW_STATE:
                "PreCon: Enter States from NO_STATE - YELLOW_STATE";
        this.guessStateColors[index] = state;}
    
    
    // Methods
    
    public void resetGuessColors(){
        Arrays.fill(guessStateColors, NO_STATE);   
    }
    
    public boolean isGuessInWordList(String guess){
        // If guess not in words or different length return false
        return guessWords.contains(guess) || targetWords.contains(guess); 
    }
    
    public void colorLettersInGuess(String guess) {
        for (int i = 0; i < 5; i++){
            char guessChar = guess.charAt(i);
            char answerChar = getAnswer().charAt(i);
            if (guessChar == answerChar){
                guessStateColors[i] = GREEN_STATE;
                availableLetters.replace(guessChar, GREEN_STATE);
            }
            else if (getAnswer().contains(Character.toString(guessChar))){
                for (int j = 0; j < 5; j++){
                    if (guessChar == getAnswer().charAt(j) && guessStateColors[j] != GREEN_STATE){
                        guessStateColors[i] = YELLOW_STATE;
                        if (availableLetters.get(guessChar) != GREEN_STATE)
                            availableLetters.replace(guessChar, YELLOW_STATE);
                    }
                }
            }
            else {
                guessStateColors[i] = GREY_STATE;
                availableLetters.replace(guessChar, GREY_STATE);
            }
        }
    }
    
    public void addQ(){
        //guess.concat("Q");
        guess = "Q";
        setChanged();
        notifyObservers();
    };
    
    
    private void initializeAvailableLetters() {
        // Put a-z lower case letters with NO_STATE in Hashmap
        for (int asciiValue = 97; asciiValue <= 122; asciiValue++)
            availableLetters.put((char)asciiValue, NO_STATE);
    }
    
    private String getRandomWord(){
        if (!selectRandomGuessWord())
            return targetWords.get(0);
        String word;
        Random rand = new Random(); 
        int listSize = targetWords.size();
        int randomIndex = rand.nextInt(listSize);
        word = targetWords.get(randomIndex);
        if (word.isEmpty()) // is checking needed? 
            return null;
        return word;
    }
    
    private List<String> loadInFromFile(String filePath) {
       List<String> list = new ArrayList<>();
       try {
            // Open and read the file
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String strLine;
            // Write each line into list
            while ((strLine = reader.readLine()) != null) {
                list.add(strLine);
            }
            reader.close();
        } 
        catch (Exception e) {
            e.printStackTrace();  
        }
        // do i need to check if arr is empty?
        return list;
    }


    
}
