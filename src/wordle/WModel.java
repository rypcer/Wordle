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
    public final int GUESS_LENGTH = 5;
    // Divide Alphabet into 4 categories according to line above
    private HashMap<Character,Integer> availableLetters;
 
    private int guessStateColors[] = new int[GUESS_LENGTH];
    // 3 Flags
    private boolean allowOnlyWordListGuesses; 
    private boolean showAnwser;
    private boolean selectRandomGuessWord;
    
    private final List<String> targetWords;
    private final List<String> guessWords;
    
    private String answer;
    private static boolean playerHasWon;
    private static boolean isGuessSubmitted;
    private String guess = new String(); // ADD IN CLI aswell
    public WModel(){
        allowOnlyWordListGuesses = false;
        showAnwser= true;
        selectRandomGuessWord = false;
        playerHasWon = false;
        isGuessSubmitted = false;
        
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
    public String getGuess() {return guess;}
    public void setGuess(String guess) {this.guess = guess;}
    public boolean isGuessSubmitted() {return isGuessSubmitted;}
    public void setIsGuessSubmitted(boolean aIsGuessSubmitted) {
        isGuessSubmitted = aIsGuessSubmitted;}
    
    // Methods
    
    public void resetGuessColors(){
        Arrays.fill(guessStateColors, NO_STATE);   
    }
    
    public boolean isGuessInWordList(String guess){
        // If guess not in words or different length return false
        return guessWords.contains(guess) || targetWords.contains(guess); 
    }
    
    // TEST THIS FUNCTION in uNIT test against reoccuring letters
    /*
        // test 1
        hello
        lilol  or llilol
        // test 2        
        cigar
        ccrgr
        if letter found in guess
        occurence of letter count in word
    
    */
    
    private int getOccurencesInString(String s, char c){
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
    
    // precondition guess needs to be lower case
    public void colorLettersInGuess2(String guess){
        guess=guess.toLowerCase();
        int occurences;
        HashMap<Character,Integer> letterOccurences = new HashMap();
        for (int asciiValue = 97; asciiValue <= 122; asciiValue++){
            char c = (char)asciiValue;
            occurences = getOccurencesInString(answer,c);
            letterOccurences.put(c, occurences); 
        }
        
        char guessChar;
        for (int i = 0; i < GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            occurences = letterOccurences.get(guessChar);
            if(occurences > 0){
                
                if (guessChar == getAnswer().charAt(i)){
                    guessStateColors[i] = GREEN_STATE;
                    availableLetters.replace(guessChar, GREEN_STATE);
                }
                else{
                    guessStateColors[i] = YELLOW_STATE;
                    availableLetters.replace(guessChar, YELLOW_STATE);
                }
                letterOccurences.replace(guessChar,occurences-1);
            }
            else{
                guessStateColors[i] = GREY_STATE;
                availableLetters.replace(guessChar, GREY_STATE);
            }
        }
        
    }
    public void colorLettersInGuess(String guess) {
        guess = guess.toLowerCase();
        boolean[] isAnswerCharChecked = new boolean[GUESS_LENGTH]; 
        Arrays.fill(isAnswerCharChecked, false);
        resetGuessColors();

        char guessChar;
        char answerChar;
        
        for (int i = 0; i < GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            answerChar = getAnswer().charAt(i);
            if (guessChar == answerChar){
                guessStateColors[i] = GREEN_STATE;
                availableLetters.replace(guessChar, GREEN_STATE);
                isAnswerCharChecked[i] = true;
            }
        }

        for (int i = 0; i < GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            if (guessStateColors[i] != GREEN_STATE&&isCharInAnswer(guessChar, isAnswerCharChecked)){
                guessStateColors[i] = YELLOW_STATE;
                
                if (availableLetters.get(guessChar) != GREEN_STATE)
                    availableLetters.replace(guessChar, YELLOW_STATE);
            }
        }
        
        for (int i = 0; i < GUESS_LENGTH; i++){
            guessChar = guess.charAt(i);
            if ( guessStateColors[i] != YELLOW_STATE && guessStateColors[i] != GREEN_STATE) {
                guessStateColors[i] = GREY_STATE;
                if (availableLetters.get(guessChar) != YELLOW_STATE && availableLetters.get(guessChar) != GREEN_STATE)
                    availableLetters.replace(guessChar, GREY_STATE);
            }
         }
    }
    /*else if (getAnswer().contains(Character.toString(guessChar))){
                // Search through answer if it contains guess char
                for (int j = 0; j < GUESS_LENGTH; j++){
                    if (guessChar == getAnswer().charAt(j) && isAnswerCharChecked[j] == false ){
                        guessStateColors[i] = YELLOW_STATE;
                        isAnswerCharChecked[j] = true;
                        noMatch = false;
                        if (availableLetters.get(guessChar) != GREEN_STATE)
                            availableLetters.replace(guessChar, YELLOW_STATE);
                    }
                }
            }*/
    private boolean isCharInAnswer(char guessChar, boolean[] isAnswerCharChecked){
        for (int j = 0; j < GUESS_LENGTH; j++){
            if (guessChar == getAnswer().charAt(j) && isAnswerCharChecked[j] == false ){
                isAnswerCharChecked[j] = true;
                return true;
            }
        }
        return false;
    }
    
    private void initializeAvailableLetters() {
        // Put a-z lower case letters with NO_STATE in Hashmap
        for (int asciiValue = 97; asciiValue <= 122; asciiValue++)
            availableLetters.put((char)asciiValue, NO_STATE);
    }
    
    private String getRandomWord(){
        if (!selectRandomGuessWord())
            return "hello";//targetWords.get(0);
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

    
    // Controller Methods
    
    public void modifyGuess(String keyText, boolean isAddToGuess){
        if (isAddToGuess)
            guess += keyText;
        else if (!isAddToGuess)
            guess = removeLastChar(guess);
        //System.out.println(guess);
        setChanged();
        notifyObservers();
    }
    public void submitGuess(){ // Check with CLI if we can use there
        //allowOnlyWordListGuesses
        //if(isGuessInWordList(guess)){
           
        //}
        //else
        setIsGuessSubmitted(true);
        colorLettersInGuess(guess);
        //System.out.println(getAvailableLetters().get('c'));    
        setChanged();
        notifyObservers();
    }
    
    private static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
          ? null 
          : (s.substring(0, s.length() - 1));
    }


    
}
