package wordle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Ajmal
 */
public class OptionsPanel  extends CustomPanel{
    
    private JButton newGameButton;
    private JLabel errorLabel;
    private JLabel answerLabel;
    
    // Options Panel Methods
    public OptionsPanel(WModel model, WController controller){
        super(model, controller);
        super.panelSize = new Dimension(420,50);
        createOptionsPanel();
    }
    
    private void createOptionsPanel(){
        
        newGameButton = new JButton("New Game");
        errorLabel = new JLabel("Word Not in List");
        answerLabel = new JLabel("Answer: " + model.getAnswer());
        newGameButton.setEnabled(false);
        errorLabel.setVisible(false);
        answerLabel.setVisible(false);
        
        newGameButton.addActionListener((ActionEvent e) -> {controller.restartGame();});
        
        this.add(newGameButton);
        this.add(answerLabel);
        this.add(errorLabel);
    }
    
    @Override
    public void update(){
        if(model.allowGameRestart())
            newGameButton.setEnabled(true);
        if(model.hasGameRestarted()){
            newGameButton.setEnabled(false);
            answerLabel.setText("Answer: " + model.getAnswer());
        }
        if(model.isWordNotInList())
            errorLabel.setVisible(true);
        else
            errorLabel.setVisible(false);
  
        if(model.isShowAnswer() || model.alwaysShowAnswer())
            answerLabel.setVisible(true);
        else
            answerLabel.setVisible(false);
    }
}
