/* 
 * Overview:
 * Hierarchy of Swing
 * JFrame->ContentPane->JPanel->JComponents eg key, textfield...
 * Notes:
 * JFrame has a contentpane where all components are added, adding it below ways is same
 * Jframe.add(new JButton()) same as JFrame.getContentPane().add(new JButton()) 
 * If button/label displays '...' as text, means no space for chars
 * .pack() // Resizes to fit the window dimensions and layouts of its subcomponents
 */
package wordle;

import java.util.Observer;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Ajmal
 */

public class WView implements Observer {
   
    private final WModel model;
    private final WController controller;
    
    private JFrame frame;
    private CustomPanel optionsPanel;
    private CustomPanel keyboardPanel;
    private CustomPanel guessPanel;
    
    public WView(WModel model, WController controller){
        
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        createControls();
        controller.setView(this);
        update(model, null); // Add model as observerable
    }

    public void createControls(){
        frame = new JFrame("Wordle GUI Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane(); 
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        
        // Initialize Panels
        guessPanel = new GuessPanel(model, controller);
        contentPane.add(guessPanel);
        keyboardPanel = new KeyboardPanel(model, controller);
        contentPane.add(keyboardPanel);
        optionsPanel = new OptionsPanel(model, controller);
        contentPane.add(optionsPanel);
        
        frame.pack(); 
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    @Override
    public void update(java.util.Observable o, Object arg){
        guessPanel.update();
        keyboardPanel.update();
        optionsPanel.update();
        frame.repaint();
    }
}
