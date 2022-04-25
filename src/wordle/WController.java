/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

/**
 *
 * @author Ajmal
 */
public class WController {
   
    private WModel model;
    // We dont really update View via Controller 
    // as its done with Observer Update through Model
    private WView view; 
    
    public WController(WModel model){
        this.model = model;
    }
    public void setView(WView view){
        this.view = view;
    }
    public void addToGuess(String buttonText){
        //int asciiNum = ((int)buttonText.charAt(0));
        if(!guessFieldsFilled())
            model.modifyGuess(buttonText, true);
    }
    public void removeFromGuess(){
        if(model.getGuess().length() > 0)
            model.modifyGuess(null, false);
    }
    public void submitGuess(){
        if(guessFieldsFilled()){
            model.submitGuess();
        }
        
    }
    public boolean guessFieldsFilled (){
        return model.getGuess().length() == model.GUESS_LENGTH;
    }
}
