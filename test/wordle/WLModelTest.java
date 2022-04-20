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
public class WLModelTest {
    
    
    static WModel model = new WModel();
    public WLModelTest() {
        
    }

    @Test
    public void testGetRandomWord() {
        assertNotNull(model.getRandomWord());
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
    

