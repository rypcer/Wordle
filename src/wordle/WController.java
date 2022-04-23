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
    
}
