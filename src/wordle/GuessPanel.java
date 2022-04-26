package wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Ajmal
 */
public class GuessPanel extends CustomPanel{

    private JLabel[][] guessRows;

    public GuessPanel(WModel model, WController controller){
        super(model, controller);
        super.panelSize = new Dimension(340,420);
        
        initializeGuessRows();
        addGuessRowsToPanel();
    }
    
    @Override
    public void update(){
        // Added as variables so they dont call model each iteration, is it right?
        String guess = model.getGuess();
        int currentGuessTry = model.getCurrentGuessTry();
        JLabel currentGuessField;
        int COLOR_STATE;
        
        if(model.hasGameRestarted()){
            clearGuessRows();
            return;
        }
        
        // Update PREVIOUS Guess Row Colors
        
        if(model.isGuessSubmitted()){
            for(int j = 0; j < model.GUESS_LENGTH; j++){
                currentGuessField = guessRows[currentGuessTry-1][j];
                COLOR_STATE = model.getGuessStateColor(j);
                changeGuessFieldState(currentGuessField, COLOR_STATE);
            }
            model.setIsGuessSubmitted(false);
            return;
        }
        
        // Update CURRENT Guess Row Colors (User adds guess words)
        for (int j = 0; j < model.GUESS_LENGTH; j++){
            currentGuessField = guessRows[currentGuessTry][j];
            if (j < guess.length()){
                currentGuessField.setText(Character.toString(guess.toUpperCase().charAt(j)));
                changeGuessFieldState(currentGuessField, model.NO_STATE);
            }
            else {
                clearGuessField(currentGuessField);
            }
        }
        
    }
    
    private void initializeGuessRows(){
        guessRows = new JLabel[model.MAX_GUESSES][model.GUESS_LENGTH];
        for(int row = 0; row < model.MAX_GUESSES; row++){
            for(int col = 0; col < model.GUESS_LENGTH; col++ ){
                guessRows[row][col] = createGuessField("");
            }
        }
    }
    
    private void addGuessRowsToPanel(){
        //guessPanel.setBorder(BorderFactory.createTitledBorder("guess"));
        for(int i = 0; i < guessRows.length; i++)
            for(int j = 0; j < model.GUESS_LENGTH; j++)
                this.add(guessRows[i][j]);
    } 
    
    private JLabel createGuessField(String text){
        JLabel field = new JLabel(text);
        field.setFont(new Font("Sans", Font.BOLD, 28)); 
        changeGuessFieldState(field, model.EMPTY_STATE);
        field.setPreferredSize(new Dimension(55,55));
        return field;
    }
    
    private void clearGuessField(JLabel currentGuessField) {
        currentGuessField.setText(null);
        changeGuessFieldState(currentGuessField, model.EMPTY_STATE);
    }
    
    private void clearGuessRows(){
        for(int row = 0; row < model.MAX_GUESSES; row++){
            for(int col = 0; col < model.GUESS_LENGTH; col++ ){
                clearGuessField(guessRows[row][col]);
            }
        }
    }

    private void changeGuessFieldState(JLabel field, int state){
        if (state == model.GREEN_STATE)
            setFieldColor(field, null, model.GREEN, Color.WHITE);
        else if (state == model.YELLOW_STATE)
            setFieldColor(field, null, model.YELLOW, Color.WHITE);
        else if (state == model.GREY_STATE)
            setFieldColor(field, null, Color.DARK_GRAY, Color.WHITE);
        else if (state == model.NO_STATE)
            setFieldColor(field,Color.GRAY, null, Color.BLACK);
        else if (state == model.EMPTY_STATE)
            setFieldColor(field,Color.LIGHT_GRAY, null, null);
    }
    
    private void setFieldColor(JLabel field, Color borderColor, 
            Color fillColor, Color fontColor){
        
        field.setBackground(fillColor);
        boolean isOpaque = fillColor != null;
        field.setOpaque(isOpaque);
        field.setForeground(fontColor);
        int borderThickness = 2;
        Border line;
        line = (borderColor != null) ? BorderFactory.createLineBorder(borderColor, 
                borderThickness) : null;
        Border margin = new EmptyBorder(0, 16, 0, 0);
        Border compound = new CompoundBorder(line, margin);
        field.setBorder(compound);
    }
}
