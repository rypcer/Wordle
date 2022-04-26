/* 
 * Overview:
 * 
 * Notes:
 * ASCII table used to initialize letters in efficient way, best idea I ever had
 * 
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
    public final int GUESS_LENGTH = 5;
    public final int EMPTY_STATE = -2;
    public final int NO_STATE = -1, GREY_STATE = 0, GREEN_STATE = 1, YELLOW_STATE = 2;
    
    // Each Alphabet letter has a state 
    private HashMap<Character,Integer> availableLetters;
    private int guessStateColors[];
    private final List<String> targetWordsList;
    private final List<String> guessWordList;
    
    // 3 Flags
    private boolean allowOnlyWordListGuesses; 
    private boolean alwaysShowAnswer;
    private boolean selectRandomGuessWord;
    
    private String guess;
    private String answer;
    private int currentGuessTry;
    private boolean playerHasWon;
    private boolean isGuessSubmitted;
    private boolean allowGameRestart;
    private boolean hasGameRestarted;
    private boolean wordNotInList;
    private boolean showAnswer;
   
      
    public WModel(){
        targetWordsList = loadInFromFile("src/wordle/data/common.txt");
        guessWordList = loadInFromFile("src/wordle/data/words.txt");
        allowOnlyWordListGuesses = false;
        alwaysShowAnswer = true;
        selectRandomGuessWord = true;
        guessStateColors = new int[GUESS_LENGTH];
        initGame();
    }
   
    public void initGame(){
        currentGuessTry = 0;
        playerHasWon = false;
        isGuessSubmitted = false;
        initializeAvailableLetters();
        answer = getRandomWord();
        currentGuessTry = 0;
        guess = new String();
        allowGameRestart = false;
        wordNotInList = false;
        showAnswer = false;
        hasGameRestarted = true;
        setChanged();
        notifyObservers();
        // After updating Observers, set back to false
        hasGameRestarted = false;
    }
        
    // Getter & Setters
    public int getCurrentGuessTry() {return currentGuessTry;}
    public void setCurrentGuessTry(int currentGuessTry) {this.currentGuessTry = currentGuessTry;}
    public boolean allowGameRestart() {return allowGameRestart;}
    public void setAllowGameRestart(boolean aEnableGameRestart) {this.allowGameRestart = aEnableGameRestart;}
    public boolean hasGameRestarted() {return hasGameRestarted;}
    public void setHasGameRestarted(boolean aHasGameRestarted) {this.hasGameRestarted = aHasGameRestarted;}
    public boolean isWordNotInList() {return wordNotInList;}
    public void setWordNotInList(boolean aWordNotInList) {this.wordNotInList = aWordNotInList;}
    public boolean isShowAnswer() {return showAnswer;}
    public void setShowAnswer(boolean aShowAnswer) {this.showAnswer = aShowAnswer;}
    public boolean alwaysShowAnswer() {return alwaysShowAnswer;}
    public void setAlwaysShowAnswer(boolean alwaysShowAnswer) {this.alwaysShowAnswer = alwaysShowAnswer;}
    public boolean allowOnlyWordListGuesses() {return allowOnlyWordListGuesses;}
    public boolean showAnwser() {return showAnswer;}
    public boolean selectRandomGuessWord() {return selectRandomGuessWord;}
    public String getAnswer() {return answer;}
    public boolean getPlayerHasWon() {return playerHasWon;}
    public void setPlayerHasWon(boolean aPlayerHasWon) {this.playerHasWon = aPlayerHasWon;}
    public String getGuess() {return guess;}
    /*returns true if guess is 5 letters*/
    public boolean setGuess(String guess) {
        if(guess.length()==GUESS_LENGTH){this.guess = guess; return true;}
        else return false;   
    }
    public boolean isGuessSubmitted() {return isGuessSubmitted;}
    public void setIsGuessSubmitted(boolean aIsGuessSubmitted) {isGuessSubmitted = aIsGuessSubmitted;}
    public int getGuessStateColor(int index) {return guessStateColors[index];}
    public void setGuessStateColor(int index, int state) {
        assert state >= NO_STATE && state <= YELLOW_STATE:
                "PreCon: Enter States from NO_STATE - YELLOW_STATE";
        this.guessStateColors[index] = state;}
    public HashMap<Character,Integer> getAvailableLetters() {return availableLetters;}
    

    // Methods
    public void modifyGuess(String keyText, boolean isAddToGuess){
        if (isAddToGuess)
            guess += keyText;
        else if (!isAddToGuess)
            guess = removeLastChar(guess);
        setChanged();
        notifyObservers();
    }
    
    public void submitGuess(){ 
        wordNotInList = false;
        if(allowOnlyWordListGuesses){
            if(!isGuessInWordList(guess.toLowerCase())){
                wordNotInList = true;
                setChanged();
                notifyObservers();
                return;
            }
        }
            
        setIsGuessSubmitted(true);
        colorLettersInGuess(guess);

        if(guess.toLowerCase().equals(answer)){
            playerHasWon = true;
        }

        currentGuessTry++;
        guess = "";
        
        // Checks needs to be done after try is incremented
        if(currentGuessTry == MAX_GUESSES){
            showAnswer = true;
        }
        
        if(currentGuessTry == 1){
            allowGameRestart = true;
        }
        setChanged();
        notifyObservers();
    }
    
    public void resetGuessStateColors(){
        Arrays.fill(guessStateColors, NO_STATE);   
    }
    
    public boolean isGuessInWordList(String guess){
        return guessWordList.contains(guess) || targetWordsList.contains(guess); 
    }
    
    // precondition guess needs to be lower case
    public void colorLettersInGuess(String guess) {
        guess = guess.toLowerCase();
        boolean[] isAnswerCharChecked = new boolean[GUESS_LENGTH]; 
        Arrays.fill(isAnswerCharChecked, false);
        resetGuessStateColors();
        char guessChar;
        char answerChar;
        
        // Color Guess Letters Grey
        for (int i = 0; i < GUESS_LENGTH; i++){
             guessChar = guess.charAt(i);
             guessStateColors[i] = GREY_STATE;
             availableLetters.replace(guessChar, GREY_STATE);
        }
        // Color Guess Letters Green
        for (int i = 0; i < GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            answerChar = getAnswer().charAt(i);
            if (guessChar == answerChar){
                guessStateColors[i] = GREEN_STATE;
                availableLetters.replace(guessChar, GREEN_STATE);
                isAnswerCharChecked[i] = true;
            }
        }
        // Color Guess Letters Yellow
        for (int i = 0; i < GUESS_LENGTH; i++){
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
        for (int j = 0; j < GUESS_LENGTH; j++){
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
        for (int asciiValue = 97; asciiValue <= 122; asciiValue++)
            availableLetters.put((char)asciiValue, NO_STATE);
    }
    
    private String getRandomWord(){
        if (!selectRandomGuessWord())
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
        // do i need to check if arr is empty?
        return list;
    }

    private static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
          ? null 
          : (s.substring(0, s.length() - 1));
    }
  
}
