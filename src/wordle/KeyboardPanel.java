package wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.*;

/**
 *
 * @author Ajmal
 */
/** CLASS INVARIANT: keyboardLayout letters need to exist in model.availableLetters*/
public class KeyboardPanel extends CustomPanel{

    private final String KEYBOARD_LAYOUT;
    private JButton[] keyboardButtons;
    private int backspaceKeyIndex = 27, enterKeyIndex = 19;

    public KeyboardPanel(WModel model, WController controller){
        super(model, controller);
        super.panelSize = new Dimension(420, 220);
        KEYBOARD_LAYOUT = "QWERTYUIOPASDFGHJKLZXCVBNM";
        initKeyboard();
        createKeyboardPanel();
    }
    
    @Override
    public void update(){
        JButton key; char c;
        for(int i = 0 ; i < keyboardButtons.length;i++){
            key = keyboardButtons[i];
            c = key.getText().toLowerCase().charAt(0);
            if(i != enterKeyIndex && i != backspaceKeyIndex)
                changeKeyState(key, model.getAvailableLetters().get(c));
        }
    }

    private void initKeyboard(){
        String backspace = Character.toString((char)9003);
        String enter = "Enter";
        keyboardButtons = new JButton[KEYBOARD_LAYOUT.length()+2];
        // x iteratrates through keyboardLayout array
        // i iteratrates through keyboardButtons array
        for (int i = 0, x = 0; i < keyboardButtons.length; i++){
            if (i == enterKeyIndex)
                keyboardButtons[i] = createKeyboardButton(enter);
            else if (i == backspaceKeyIndex)
                keyboardButtons[i] = createKeyboardButton(backspace);
            else{
                keyboardButtons[i] = createKeyboardButton(Character.toString(
                        KEYBOARD_LAYOUT.charAt(x)));
                x++; 
            }
        }
    }
    
    private void createKeyboardPanel() {
        //this.setBorder(BorderFactory.createTitledBorder("keyboard"));
        
        // Add action listener to KeyButtons
        for(int i = 0 ; i < keyboardButtons.length;i++){
            if(i == enterKeyIndex){
                keyboardButtons[i].addActionListener((ActionEvent e) -> {
                    controller.submitGuess();});
            }
            else if(i == backspaceKeyIndex){
                keyboardButtons[i].addActionListener((ActionEvent e) -> {
                    controller.removeFromGuess();});
            }
            else{
                keyboardButtons[i].addActionListener((ActionEvent e) -> {
                    String buttonText = ((JButton)e.getSource()).getText();
                    controller.addToGuess(buttonText);});
            }
        }
        // Add KeyButtons to Panel
        for(int i = 0 ; i < keyboardButtons.length;i++)
           this.add(keyboardButtons[i]);
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
