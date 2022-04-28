/** 
 * Overview:
 * 
 * Notes:
 * ASCII table used to initialize letters in efficient way, best idea I ever had
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
    
    public final int MAX_GUESSES = 6;
    public final int MAX_GUESS_LENGTH = 5;
    public final int EMPTY_STATE = -2;
    public final int NO_STATE = -1, GREY_STATE = 0, GREEN_STATE = 1, YELLOW_STATE = 2;
    private final int LETTER_A_ASCII_VALUE = 97, LETTER_Z_ASCII_VALUE = 122;
    
    // Each Alphabet letter has a state 
    private HashMap<Character,Integer> availableLetters;
    private int guessStateColors[];
    private final List<String> targetWordsList;
    private final List<String> guessWordList;
    
    private String guess;
    private String answer;
    private int currentGuessTry;
    private boolean hasPlayerWon;
    private boolean isGuessSubmitted;
    private boolean allowNewGame;
    private boolean hasNewGameStarted;
    private boolean wordNotInList;
    private boolean displayAnswer;
    
    // 3 Flags
    private boolean allowOnlyWordListGuesses; 
    private boolean alwaysDisplayAnswer;
    private boolean selectRandomGuessWord;
      
    public WModel(){
        targetWordsList = loadInFromFile("src/wordle/data/common.txt");
        guessWordList = loadInFromFile("src/wordle/data/words.txt");
        allowOnlyWordListGuesses = true;
        alwaysDisplayAnswer = true;
        selectRandomGuessWord = false;
        wordNotInList = false;
        guessStateColors = new int[MAX_GUESS_LENGTH];
        initGame();
    }

    public boolean invariant(){
        boolean isInsideAlphabet = true;
        for(int i = 0; i < guess.length();i++)
            if(guess.charAt(i) <= LETTER_A_ASCII_VALUE && 
                    guess.charAt(i) >= LETTER_Z_ASCII_VALUE)
                isInsideAlphabet = false;
                
        return guess.length() <= MAX_GUESS_LENGTH && isInsideAlphabet;
    }
    /**
     * @post. All variables need to have the current values
     * at all times
     */
    public void initGame(){
        //String oldAnswer = answer;
        initializeAvailableLetters();
        answer = getRandomWord();
        guess = new String();
        
        currentGuessTry = 0;
        hasPlayerWon = false;
        isGuessSubmitted = false;
        allowNewGame = false;
        hasNewGameStarted = true;
        displayAnswer = false;
        
        setChanged();
        notifyObservers();
        // After updating Observers, set back to false
        hasNewGameStarted = false;
        //assert answer.compareTo(oldAnswer) == 1 : " No new random word generated ";
        assert currentGuessTry == 0 : "currentGuessTry is not 0";
        assert hasPlayerWon == false : "hasPlayerWon is true";
        assert isGuessSubmitted == false : "isGuessSubmitted is true";
        assert allowNewGame == false : "allowNewGame is true";
        assert hasNewGameStarted == false : "hasNewGameStarted is true";
        assert displayAnswer == false : "displayAnswer is true";
    }
        
    // Getter & Setters
    public boolean alwaysShowAnswer() {return alwaysDisplayAnswer;}
    public boolean allowOnlyWordListGuesses() {return allowOnlyWordListGuesses;}
    public boolean isGuessSubmitted() {return isGuessSubmitted;}
    public boolean displayAnwser() {return displayAnswer;}
    public boolean hasPlayerWon() {return hasPlayerWon;}
    public boolean allowNewGame() {return allowNewGame;}
    public boolean hasNewGameStarted() {return hasNewGameStarted;}
    public boolean isWordNotInList() {return wordNotInList;}
    public boolean isShowAnswer() {return displayAnswer;}
    public String getAnswer() {return answer;}
    public String getGuess() {return guess;}
    public int getGuessStateColor(int index) {return guessStateColors[index];}
    public int getCurrentGuessTry() {return currentGuessTry;}
    public HashMap<Character,Integer> getAvailableLetters() {return availableLetters;}
    
    public void setAllowOnlyWordListGuesses(boolean con){this.allowOnlyWordListGuesses = con;}
    public void setIsGuessSubmitted(boolean isGuessSubmitted) {this.isGuessSubmitted = isGuessSubmitted;}
    /**
    * @pre. invariant must be met
    */
    public void setGuess(String guess) {
        assert invariant() : "invariant must be true initially";
        this.guess = guess;
    }
   
    
    // Conditions
    public boolean isGuessComplete(){
        return guess.length() == MAX_GUESS_LENGTH;
    }
    public boolean playerHasTriesLeft(){
        return currentGuessTry != MAX_GUESSES;
    }
    
    // Methods
   
    /**
     * @pre. keyText length needs to be 1
     * @post. guess length needs to be modified with 
     * correct value and meet invariant
     */
    public void addToGuess(String keyText){
        assert keyText != null : "keyText must exist";
        assert keyText.length() == 1 : "keyText length needs to be 1";
        assert invariant() : "invariant must be true initially";
        String oldGuess = guess;
        
        keyText = keyText.toLowerCase();
        guess += keyText;
        setChanged();
        notifyObservers();
        
        assert !guess.equals(oldGuess): "guess not modified";
        assert guess.equals(oldGuess+keyText) : "guess has been assigned wrong value";
        assert invariant() : "invariant must be maintained";
    }
    
    /**
     * @post. guess length needs to at least 0, be modified and meet invariant
     */
    public void removeFromGuess(){
        assert guess != null : "guess is null";
        String oldGuess = guess;
        
        guess = removeLastChar(guess);
        setChanged();
        notifyObservers();
        
        assert guess.length() >= 0 : "guess is empty";
        assert !guess.equals(oldGuess): "guess not modified";
        assert guess.equals(removeLastChar(oldGuess)) : "guess has not been removed properly";
        assert invariant() : "invariant must be maintained";
    }
    
    /**
     * @post. guess should be empty and 
     * currentGuessTry increased by 1  
     */
    public void submitGuess(){ 
        int oldCurrentGuessTry = currentGuessTry;
        setChanged();
        guess = guess.toLowerCase();
        wordNotInList = false;
        if (allowOnlyWordListGuesses){
            if (!isGuessInWordList(guess)){
                wordNotInList = true;
                notifyObservers();
                return;
            }
        }
            
        isGuessSubmitted = true;
        colorLettersInGuess(guess);

        if (guess.equals(answer)){
            hasPlayerWon = true;
        }
        
        currentGuessTry++;
        guess = "";
       
        // Below checks needs to be done after try is incremented
        if (currentGuessTry == MAX_GUESSES){
            displayAnswer = true;
        }
        // condition below not needed as guessTry is always 1,
        // but prevents variable reassignment
        if (currentGuessTry == 1){
            allowNewGame = true;
        }
        notifyObservers();
        isGuessSubmitted = false;
        assert  isGuessSubmitted == false : "guess not submitted";
        assert guess.length() == 0 : "guess is not empty";
        assert currentGuessTry == oldCurrentGuessTry + 1 : 
        "currentGuessTry not increased" ;
    }
    
    private boolean isGuessInWordList(String guess){
        return guessWordList.contains(guess) || targetWordsList.contains(guess); 
    }
    
    private void resetGuessStateColors(){
        Arrays.fill(guessStateColors, NO_STATE);   
    }
    
    private void colorLettersInGuess(String guess) {
        boolean[] isAnswerCharChecked = new boolean[MAX_GUESS_LENGTH]; 
        Arrays.fill(isAnswerCharChecked, false);
        resetGuessStateColors();
        char guessChar;
        char answerChar;
        
        // Color Guess Letters Grey
        for (int i = 0; i < MAX_GUESS_LENGTH; i++){
             guessChar = guess.charAt(i);
             guessStateColors[i] = GREY_STATE;
             availableLetters.replace(guessChar, GREY_STATE);
        }
        // Color Guess Letters Green
        for (int i = 0; i < MAX_GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            answerChar = getAnswer().charAt(i);
            if (guessChar == answerChar){
                guessStateColors[i] = GREEN_STATE;
                availableLetters.replace(guessChar, GREEN_STATE);
                isAnswerCharChecked[i] = true;
            }
        }
        // Color Guess Letters Yellow
        for (int i = 0; i < MAX_GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            if (guessStateColors[i] != GREEN_STATE && 
                    isCharInAnswer(guessChar, isAnswerCharChecked)){
                guessStateColors[i] = YELLOW_STATE;
                if (availableLetters.get(guessChar) != GREEN_STATE)
                    availableLetters.replace(guessChar, YELLOW_STATE);
            }
        }
    }
    
    private boolean isCharInAnswer(char guessChar, boolean[] isAnswerCharChecked){
        for (int j = 0; j < MAX_GUESS_LENGTH; j++){
            if (guessChar == getAnswer().charAt(j) && isAnswerCharChecked[j] == false ){
                isAnswerCharChecked[j] = true;
                return true;
            }
        }
        return false;
    }
    
    /** Initializes new memory each game, but doesn't affect performance*/
    private void initializeAvailableLetters() {
        // Put a-z lower case letters with NO_STATE in Hashmap
        availableLetters = new HashMap<>();
        for (int asciiValue = LETTER_A_ASCII_VALUE; asciiValue <= LETTER_Z_ASCII_VALUE; asciiValue++)
            availableLetters.put((char)asciiValue, NO_STATE);
    }
    
    private String getRandomWord(){
        if (!selectRandomGuessWord)
            return targetWordsList.get(0);
        String word;
        Random rand = new Random(); 
        int listSize = targetWordsList.size();
        int randomIndex = rand.nextInt(listSize);
        word = targetWordsList.get(randomIndex);
        if (word.isEmpty()) // is checking needed? 
            return null;
        return word;
    }
    

    private static List<String> loadInFromFile(String filePath) {
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
        return list;
    }
    
    private static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
          ? null 
          : (s.substring(0, s.length() - 1));
    }
  
}
