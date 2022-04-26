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
        if (!model.isGuessComplete() && !model.getPlayerHasWon() && model.playerHasTriesLeft())
            model.modifyGuess(buttonText, true);
    }
    public void removeFromGuess(){
        if (model.getGuess().length() > 0 && !model.getPlayerHasWon() && model.playerHasTriesLeft())
            model.modifyGuess(null, false);
    }
    public void submitGuess(){
        if (model.isGuessComplete() && !model.getPlayerHasWon() && model.playerHasTriesLeft()){
            model.submitGuess();
        }
    }
    public void restartGame(){
        model.initGame();
    }
}
