/* Hierarchy of Swing
 * JFrame->ContentPane->JPanel->JComponents eg key, textfield...
 * JFrame has a contentpane where all components are added, adding it below ways is same
 * Jframe.add(new JButton()) same as JFrame.getContentPane().add(new JButton()) 
 *   //'...' means no space for char
 * .pack() // Resizes to fit the window dimensions and layouts of its subcomponents
 */
package wordle;

import java.util.Observer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 *
 * @author Ajmal
 */
/** CLASS INVARIANT: keyboardLaout == model.availableLetters*/
public class WView implements Observer {
    
    private static final Dimension GUESS_SIZE = new Dimension(340,420);
    private static final Dimension OPTIONS_SIZE = new Dimension(420,50);
    private static final Dimension KEYBOARD_SIZE = new Dimension(420, 220);
    
    private static Color GREEN = new Color(122, 171, 104);
    private static Color YELLOW = new Color(199, 178, 96);

    private final WModel model;
    private final WController controller;
    private JFrame frame;
    private JPanel optionsPanel;
    private JPanel keyboardPanel;
    private JPanel guessPanel; // Either create new component like jLabel by inheriting or draw as graphic
    private String keyboardLayout;
    private JButton keyboardButtons[];
    private JLabel guessFields[][];
    private JButton newGameButton;
    private int backspaceKeyIndex = 27, enterKeyIndex = 19;

    
    public WView(WModel model, WController controller){
        
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        createControls();
        controller.setView(this);
        update(model, null); // Add model as observerable
    }

    public void createControls()
    {
        frame = new JFrame("Wordle GUI Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane(); 
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)); 
        initializeGuessFields();
        createGuessPanel();
        contentPane.add(guessPanel);
        initializeKeyboard();
        createKeyboardPanel();
        contentPane.add(keyboardPanel);
        createOptionsPanel();
        contentPane.add(optionsPanel);
        frame.pack(); 
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    @Override
    public void update(java.util.Observable o, Object arg){
        updateGuessPanel();
        updateKeyboardPanel();
        updateOptionsPanel();
        frame.repaint();
    }
    
    // Options Panel Methods
    private void createOptionsPanel(){
        optionsPanel = new JPanel();
        optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.setPreferredSize(OPTIONS_SIZE);
        optionsPanel.setMaximumSize(OPTIONS_SIZE);
        // Create Button
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener((ActionEvent e) -> {
            controller.restartGame();
        });
        optionsPanel.add(newGameButton);
        newGameButton.setEnabled(false);
    }
    private void updateOptionsPanel(){
        if(model.allowGameRestart())
            newGameButton.setEnabled(true);
        if(model.hasGameRestarted())
            newGameButton.setEnabled(false);
    }
    
    
    // Guess Field Panel Methods
    private void initializeGuessFields(){

        guessFields = new JLabel[model.getMAX_GUESSES()][model.GUESS_LENGTH];
        for(int row = 0; row < model.getMAX_GUESSES(); row++){
            for(int col = 0; col < model.GUESS_LENGTH; col++ ){
                guessFields[row][col] = createGuessField("");
            }
        }
        
    }
    
    private void createGuessPanel(){
        //guessPanel.setLayout(new BoxLayout(guessPanel,BoxLayout.X_AXIS));
        guessPanel = new JPanel();
        guessPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessPanel.setPreferredSize(GUESS_SIZE);
        guessPanel.setMaximumSize(GUESS_SIZE);
        guessPanel.setBorder(BorderFactory.createTitledBorder("guess"));
        // Add all guess fields to panel
        for(int i = 0; i < guessFields.length; i++)
            for(int j = 0; j < model.GUESS_LENGTH; j++)
                guessPanel.add(guessFields[i][j]);
    } 
    
    private void updateGuessPanel(){
        // Added as variables so they dont call model each iteration, is it right?
        String guess = model.getGuess();
        int currentGuessTry = model.getCurrentGuessTry();
        JLabel currentGuessField;
        if(model.hasGameRestarted()){
            clearAllGuessRows();
            return;
        }
        
        // Update PREVIOUS Guess Row Colors 
        if(model.isGuessSubmitted()){
            for(int j = 0; j < model.GUESS_LENGTH; j++){
                currentGuessField = guessFields[currentGuessTry-1][j];
                int COLOR_STATE = model.getGuessStateColor(j);
                changeGuessFieldState(currentGuessField, COLOR_STATE);
            }
            model.setIsGuessSubmitted(false);
            return;
        }
        
        // Update CURRENT Guess Row Colors (User adds guess words)
        for (int j = 0; j < model.GUESS_LENGTH; j++){
            currentGuessField = guessFields[currentGuessTry][j];
            if (j < guess.length()){
                currentGuessField.setText(Character.toString(guess.charAt(j)));
                changeGuessFieldState(currentGuessField, model.NO_STATE);
            }
            else {
                clearGuessField(currentGuessField);
            }
        }
        
    }

    public void clearGuessField(JLabel currentGuessField) {
        currentGuessField.setText(null);
        changeGuessFieldState(currentGuessField, model.EMPTY_STATE);
    }
    
    private void clearAllGuessRows(){
        for(int row = 0; row < model.getMAX_GUESSES(); row++){
            for(int col = 0; col < model.GUESS_LENGTH; col++ ){
                clearGuessField(guessFields[row][col]);
            }
        }
    }
    private JLabel createGuessField(String text){
        JLabel field = new JLabel(text);
        field.setFont(new Font("Sans", Font.BOLD, 28)); 
        changeGuessFieldState(field, model.EMPTY_STATE);
        field.setPreferredSize(new Dimension(55,55));
        return field;
    }
    
    private void changeGuessFieldState(JLabel field, int state){
        if (state == model.GREEN_STATE)
            setFieldColor(field, null, GREEN, Color.WHITE);
        else if (state == model.YELLOW_STATE)
            setFieldColor(field, null, YELLOW, Color.WHITE);
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
    

    // Keyboard Panel Methods - create keyboard panel class? 
    private void initializeKeyboard(){
        keyboardLayout = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String backspace = Character.toString((char)9003);
        String enter = "Enter";
        keyboardButtons = new JButton[keyboardLayout.length()+2];
        for(int i = 0, x = 0; i < keyboardButtons.length;i++){
            if(i == enterKeyIndex)
                keyboardButtons[i] = createKeyboardButton(enter);
            else if (i == backspaceKeyIndex)
                keyboardButtons[i] = createKeyboardButton(backspace);
            else{
                keyboardButtons[i] = createKeyboardButton(Character.toString(
                        keyboardLayout.charAt(x)));
                x++;
            }
        }
    }
    
    private void createKeyboardPanel() {
        keyboardPanel = new JPanel();
        keyboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        keyboardPanel.setPreferredSize(KEYBOARD_SIZE);
        keyboardPanel.setBorder(BorderFactory.createTitledBorder("keyboard"));
        
        // Add action listener to KeyButtons
        for(int i = 0 ; i < keyboardButtons.length;i++){
            if(i == enterKeyIndex){
                keyboardButtons[i].addActionListener((ActionEvent e) -> {
                    controller.submitGuess();
                });
            }
            else if(i == backspaceKeyIndex){
                keyboardButtons[i].addActionListener((ActionEvent e) -> {
                    controller.removeFromGuess();
                });
            }
            else{
                keyboardButtons[i].addActionListener((ActionEvent e) -> {
                    String buttonText = ((JButton)e.getSource()).getText();
                    controller.addToGuess(buttonText);
                });
            }
        }
        // Add KeyButtons to Panel
        for(int i = 0 ; i < keyboardButtons.length;i++)
           keyboardPanel.add(keyboardButtons[i]);

        
    }
    private void updateKeyboardPanel(){
        JButton key;
        for(int i = 0 ; i < keyboardButtons.length;i++){
            key = keyboardButtons[i];
            char c = key.getText().toLowerCase().charAt(0);
            if(i != enterKeyIndex && i != backspaceKeyIndex)
                changeKeyState(key, model.getAvailableLetters().get(c));
        }
    }
    
    private JButton createKeyboardButton(String text) {
        JButton key = new JButton(text);
        changeKeyState(key, model.NO_STATE);
        Border margin = new EmptyBorder(20, 14, 20, 14);
        Border compound = new CompoundBorder(null, margin);
        key.setBorder(compound);
        return key;
    }
    
    private void changeKeyState(JButton key, int state){
        if(state == model.NO_STATE)
            changeKeyColor(key, Color.BLACK, Color.LIGHT_GRAY);
        else if (state == model.GREY_STATE)
            changeKeyColor(key, Color.WHITE, Color.DARK_GRAY);
        else if (state == model.GREEN_STATE)
            changeKeyColor(key, Color.WHITE, GREEN);
        else if (state == model.YELLOW_STATE)
            changeKeyColor(key, Color.WHITE, YELLOW);
    }
    private void changeKeyColor(JButton key, Color fontColor, Color fillColor){
        key.setForeground(fontColor);
        key.setBackground(fillColor);
    }
}
