package wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 *
 * @author Ajmal
 */
public class WModel extends Observable {
    private final int MAX_GUESSES = 6;

    // 3 Flags
    private boolean allowOnlyWordListGuesses; 
    private boolean displayWordToBeGuessed;
    private boolean selectRandomGuessWord;
    
    private final List<String> targetWords;
    private final List<String> guessWords;
    
    
    
    public WModel(){
        allowOnlyWordListGuesses = true;
        displayWordToBeGuessed = true;
        selectRandomGuessWord = true;
        targetWords = loadInFromFile("src/wordle/data/common.txt");
        guessWords = loadInFromFile("src/wordle/data/words.txt");
       
    }
    //======================================================= MODEL NEEDS UPDATE OBSERVER METHOD!
 
    
    // Getter & Setters
    
    public boolean allowOnlyWordListGuesses() {
        return allowOnlyWordListGuesses;
    }

    public boolean displayWordToBeGuessed() {
        return displayWordToBeGuessed;
    }

    public boolean selectRandomGuessWord() {
        return selectRandomGuessWord;
    }

    public int getMAX_GUESSES() {
        return MAX_GUESSES;
    }
    
    // Methods
    

    public boolean isGuessInWordList(String guess){
        // If guess not in words or different length return false
        return guessWords.contains(guess) || targetWords.contains(guess); 
    }
    

    public String getRandomWord(){
        if(!selectRandomGuessWord())
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
        } catch (Exception e) {
            e.printStackTrace();  
        }
       // do i need to check if arr is empty?
        return list;
    }


    
}
