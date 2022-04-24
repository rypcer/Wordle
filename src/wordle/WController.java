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
        System.out.println("asdasd");
        if(guessFieldsFilled())
            model.submitGuess();
    }
    public boolean guessFieldsFilled (){
        return model.getGuess().length() == model.GUESS_LENGTH;
    }
}
