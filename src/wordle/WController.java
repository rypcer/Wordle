/*
 * Overview:
 * Controller just checks with Model Methods if input should be handled or not.
 * Notes:
 * We don't really update View via Controller 
 * as its done with Observer Update through Model
 * but could be useful in some cases
 */
package wordle;

/**
 *
 * @author Ajmal
 */
public class WController {
   
    private WModel model;
    private WView view; 
    
    public WController(WModel model){
        this.model = model;
    }
    public void setView(WView view){
        this.view = view;
    }
    public void addToGuess(String buttonText){
        if(!isGuessComplete() && !model.getPlayerHasWon() && playerHasTriesLeft())
            model.modifyGuess(buttonText, true);
    }
    public void removeFromGuess(){
        if(model.getGuess().length() > 0 && !model.getPlayerHasWon() && playerHasTriesLeft())
            model.modifyGuess(null, false);
    }
    public void submitGuess(){
        if(isGuessComplete() && !model.getPlayerHasWon() && playerHasTriesLeft()){
            model.submitGuess();
        }
    }
    public void restartGame(){
        model.initGame();
    }
    private boolean isGuessComplete (){
        return model.getGuess().length() == model.GUESS_LENGTH;
    }
    private boolean playerHasTriesLeft(){
        return model.getCurrentGuessTry() != model.MAX_GUESSES;
    }
}
