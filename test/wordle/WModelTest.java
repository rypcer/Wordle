/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import wordle.WModel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ajmal
 */
public class WModelTest {
    
    
    static WModel model = new WModel();
    public WModelTest() {
        
    }
        // TEST THIS FUNCTION in uNIT test against reoccuring letters
    /*
        // test 1
        hello
        lilol  or llilo
        // test 2        
        cigar
        ccrgr
    
    */
    @Test
    public void setGuessStateColor() {
        model.setGuessStateColor(0,6);
    }
    @Test
    public void testIsGuessInWords_True(){
        boolean isWordValid = model.isGuessInWordList("hello");

        assertTrue(isWordValid);
    }
    @Test
    public void testIsGuessInWords_False(){
        boolean isWordValid = model.isGuessInWordList("1u;po");
        assertFalse(isWordValid);
    }

    
}
    

