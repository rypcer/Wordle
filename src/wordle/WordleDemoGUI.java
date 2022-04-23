package wordle;

/**
 *
 * @author Ajmal
 */
public class WordleDemoGUI {
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
            new Runnable() {
                public void run () {createAndShowGUI();}
            }
        );
    }

    public static void createAndShowGUI() {
        WModel model = new WModel();
        WController controller = new WController(model);
        WView view = new WView(model, controller);
    }
}
