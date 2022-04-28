/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Ajmal
 */
public abstract class CustomPanel extends JPanel {
    
    protected Dimension panelSize;
    protected WModel model;
    protected WController controller;
    public final Color GREEN = new Color(122, 171, 104);
    public final Color YELLOW = new Color(199, 178, 96);
    
    CustomPanel(WModel model, WController controller){

        panelSize = super.getPreferredSize();
        this.model = model;
        this.controller = controller;
    }
    public abstract void update();
    
    @Override
    public Dimension getMinimumSize(){
        return getPreferredSize();
    }
    @Override
    public Dimension getMaximumSize(){
        return getPreferredSize();
    }
    
    @Override
    public Dimension getPreferredSize(){
        return  panelSize;
    }
    
    @Override
    public float getAlignmentX() {
        return Component.CENTER_ALIGNMENT;
    }
}
