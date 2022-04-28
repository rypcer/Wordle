/**
 * Basic Queries like getters setters are not tested as their too basic
 * Only derived queries are tested or commands
 * In our case we have 3 commands that need testing
 */
package wordle;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ajmal
 */
public class WModelTest {
    
    public WModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addToGuess method, of class WModel.
     * @Scenario1
     * Requires a String of length 1 to be passed in.
     * String should be part of alphabet.
     * 
     * The guess value should be modified
     * with the added string appended to guess.
     * Once appended, guess cannot exceed maximum guess length
     * And should still be within Alphabet.
     * 
     */
    @Test
    public void testAddToGuess() {
        System.out.println("addToGuess");
        String keyText = "S";
        WModel instance = new WModel();
        
        instance.addToGuess(keyText);
        assertEquals(instance.getGuess(), "s");
        assertTrue(instance.invariant());
    }

    /**
     * Test of removeFromGuess method, of class WModel.
     * @Scenario1
     * The guess should only have the last character popped off
     * It should also contain no less than 0,
     * or no more than the maximum guess length
     * And should still be within Alphabet.
     */
    @Test
    public void testRemoveFromGuess() {
        System.out.println("removeFromGuess");
        WModel instance = new WModel();
        instance.setGuess("appl");
        
        instance.removeFromGuess();
        assertEquals(instance.getGuess().length() >= 0, true);
        assertEquals(instance.getGuess(),"app");
        assertTrue(instance.invariant());
    }
    
    /**
     * Test of submitGuess method, of class WModel.
     * @Scenario1
     * Current Guess should be cleared
     * Allows for a new game to be started
     * Guess does not have to be inside the word list
     */
    @Test
    public void testSubmitGuess() {
        System.out.println("submitGuess-Scenario1");
        WModel instance = new WModel();
        instance.setGuess("aApLa");
        
        instance.submitGuess();
        assertEquals(instance.getCurrentGuessTry(),1);
        assertEquals(instance.getGuess().length(),0);
        assertTrue(instance.allowNewGame());
        assertFalse(instance.allowOnlyWordListGuesses());
    }
    /**
     * Test of submitGuess method, of class WModel.
     * @Scenario2
     * Verify if game displays answer when player is out of tries
     * Current Guess should be cleared
     * Allows for a new game to be started
     * Guess does not have to be inside the word list
     * 
     */
    @Test
    public void testSubmitGuess2() {
        System.out.println("submitGuess-Scenario2");
        WModel instance = new WModel();
        instance.setGuess("aApLa");
        
        instance.submitGuess();
        assertEquals(instance.getCurrentGuessTry(),1);
        assertEquals(instance.getGuess().length(),0);
        assertTrue(instance.allowNewGame());
        assertFalse(instance.isWordNotInList());
        assertFalse(instance.allowOnlyWordListGuesses());
    }
    /**
     * Test of submitGuess method, of class WModel.
     * @Scenario3
     * Verify if player has won.
     * Current Guess should be cleared
     * Still Allows for a new game to be started
     * Guess does not have to be inside the word list
     */
    @Test
    public void testSubmitGuess3() {
        System.out.println("submitGuess-Scenario3");
        WModel instance = new WModel();
        instance.setGuess(instance.getAnswer());
        
        instance.submitGuess();
        assertEquals(instance.getCurrentGuessTry(),1);
        assertEquals(instance.getGuess().length(),0);
        assertTrue(instance.allowNewGame());
        assertTrue(instance.hasPlayerWon());
        assertFalse(instance.allowOnlyWordListGuesses());
    }
    /**
     * Test of submitGuess method, of class WModel.
     * @Scenario4
     * Verify if player has won.
     * Current Guess should be cleared
     * Still Allows for a new game to be started
     * Guess has to be inside the word list
     */
    @Test
    public void testSubmitGuess4() {
        System.out.println("submitGuess-Scenario4");
        WModel instance = new WModel();
        instance.setGuess(instance.getAnswer());
        instance.setAllowOnlyWordListGuesses(true);
        
        instance.submitGuess();
        assertEquals(instance.getCurrentGuessTry(),1);
        assertEquals(instance.getGuess().length(),0);
        assertTrue(instance.allowNewGame());
        assertTrue(instance.hasPlayerWon());
        assertTrue(instance.allowOnlyWordListGuesses());
        assertFalse(instance.isWordNotInList());
    }
    
    /**
     * Test of submitGuess method, of class WModel.
     * @Scenario5 same as @Scenario 2 but with word list guesses
     * Verify if game displays answer when player is out of tries.
     * Current Guess should be cleared
     * Allows for a new game to be started
     * Guess has to be inside the word list
     * 
     */
    @Test
    public void testSubmitGuess5() {
        System.out.println("submitGuess-Scenario5");
        WModel instance = new WModel();
        instance.setGuess("hello");
        instance.setAllowOnlyWordListGuesses(true);
        
        instance.submitGuess();
        assertEquals(instance.getCurrentGuessTry(),1);
        assertEquals(instance.getGuess().length(),0);
        assertTrue(instance.allowNewGame());
        assertTrue(instance.allowOnlyWordListGuesses());
        assertFalse(instance.isWordNotInList());
    }
    /**
     * Test of submitGuess method, of class WModel.
     * @Scenario6
     * 
     * Guess has to be inside the word list
     * But guess is not inside it
     * 
     */
    @Test
    public void testSubmitGuess6() {
        System.out.println("submitGuess-Scenario6");
        WModel instance = new WModel();
        instance.setGuess("aApLa");
        instance.setAllowOnlyWordListGuesses(true);
        
        instance.submitGuess();
        assertTrue(instance.allowOnlyWordListGuesses());
        assertTrue(instance.isWordNotInList());
    }
}
