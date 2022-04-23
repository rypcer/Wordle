/* Hierarchy of Swing
 * JFrame->ContentPane->JPanel->JComponents eg key, textfield...
 * JFrame has a contentpane where all components are added, adding it below ways is same
 * Jframe.add(new JButton()) same as JFrame.getContentPane().add(new JButton())  
 */
package wordle;

import java.util.Observer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.border.*;

/**
 *
 * @author Ajmal
 */
/** CLASS INVARIANT: keyboardLaout == model.availableLetters*/
public class WView implements Observer {
    
    private static final Dimension GUESS_SIZE = new Dimension(420, 350);
    private static final Dimension KEYBOARD_SIZE = new Dimension(420, 220);
    private static Color GREEN = new Color(122, 171, 104);
    private static Color YELLOW = new Color(199, 178, 96);
    
    private final WModel model;
    private final WController controller;
    private JFrame frame;
    private JPanel keyboardPanel;
    private JPanel guessPanel;
    private String keyboardLayout;
    private JButton keyboardButtons[];
    
    public WView(WModel model, WController controller){
        
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        initializeKeyboard();
        createControls();
        controller.setView(this);
        update(model, null); //Adds model as observerable
    }
    private void initializeKeyboard(){
        keyboardLayout = "QWERTYUIOPASDFGHJKLZXCVBNM";
        keyboardButtons = new JButton[keyboardLayout.length()+2];
        for(int i = 0, x = 0; i < keyboardButtons.length;i++){
            if(i == 19)
                keyboardButtons[i] = createKeyboardButton("Enter");
            else if (i == 27)
                keyboardButtons[i] = createKeyboardButton(Character.toString(
                        (char)9003));
            else{
                keyboardButtons[i] = createKeyboardButton(Character.toString(
                        keyboardLayout.charAt(x)));
                x++;
            }
        }
    }
    public void createControls()
    {
        frame = new JFrame("Wordle GUI Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane(); 
        createGuessPanel();
        contentPane.add(guessPanel,BorderLayout.CENTER);
        createKeyboardPanel();
        contentPane.add(keyboardPanel,BorderLayout.PAGE_END);
        frame.pack(); // Resizes to fit the window dimensions and layouts of its subcomponents
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    @Override
    public void update(java.util.Observable o, Object arg){
        // Add labels of guesses for each letter
        frame.repaint();
        // Check color keyboard according to availableLetters
    }
    private void createGuessPanel(){
        guessPanel = new JPanel();
        //panel.setLayout(new BoxLayout(keyboardPanel,BoxLayout.X_AXIS));
        guessPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessPanel.setPreferredSize(GUESS_SIZE);
        guessPanel.setBorder(BorderFactory.createTitledBorder("guesses"));
       
    }
    private void createKeyboardPanel() {
        keyboardPanel = new JPanel();
        for(int i = 0 ; i < keyboardButtons.length;i++)
           keyboardPanel.add(keyboardButtons[i]);
        keyboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        keyboardPanel.setPreferredSize(KEYBOARD_SIZE);
        keyboardPanel.setBorder(BorderFactory.createTitledBorder("keyboard"));
    }
    private JButton createKeyboardButton(String text) {
        JButton key = new JButton(text);
        changeKeyColor(key,model.NO_STATE);
        Border margin = new EmptyBorder(20, 14, 20, 14);
        Border compound = new CompoundBorder(null, margin);
        key.setBorder(compound);
        return key;
    }
    
    private void changeKeyColor(JButton key,int state){
        if(state == model.NO_STATE){
            key.setForeground(Color.BLACK);
            key.setBackground(Color.LIGHT_GRAY);
        }
        else if (state == model.GREY_STATE){
            key.setForeground(Color.WHITE);
            key.setBackground(Color.GRAY);
        }
        else if (state == model.GREEN_STATE){
            key.setForeground(Color.WHITE);
            key.setBackground(GREEN);
        }
        else if (state == model.YELLOW_STATE){
            key.setForeground(Color.WHITE);
            key.setBackground(YELLOW);
        }
    }
    
}
