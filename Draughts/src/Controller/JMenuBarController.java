package Controller;

import View.WholeGameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>
 */
public class JMenuBarController implements ActionListener {
    
    private final WholeGameView gameView;
    
    public JMenuBarController(WholeGameView gameView) {
        
        this.gameView = gameView;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().trim()) {
            case "Exit": {
                int option = JOptionPane.showConfirmDialog(gameView,
                                                           "Are you sure you want to quit ???");
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
            }
            case "New Game": {
                int option = JOptionPane
                .showConfirmDialog(gameView,
                                   "Are you sure you want to erase this game \n and start a new one ???");
                if (option == JOptionPane.YES_OPTION) {
                    gameView.newGame();
                }
                break;
            }
            case "How to play":
                JOptionPane
                .showMessageDialog(
                                   gameView,
                                   "[:] Click on the piece you want to move and destinations will pop up \n with red borders. \n\n"
                                   + " [:] One cannot avoid jumping when there is a jump move.\n"
                                   + "However, carrying out multiple jumps is OPTIONAL \n\n"
                                   + " [:] Pieces which can jump other pieces pop up with blue rounded borders \n\n Have fun !!!",
                                   "Draughts", JOptionPane.PLAIN_MESSAGE);
                break;
            case "About":
                JOptionPane
                .showMessageDialog(
                                   gameView,
                                   "[:] Draughts is a just a simple game \n with simple graphics developed by Fabrice Appolinary \n for fun. \n\n"
                                   + "[:] This is the beta version (i.e not yet finished) \nso prepare to meet a bunch of bugs. \n\n"
                                   + "[:] Honestly, my evaluation function is dumb  because \n i havent mastered all the Artificial Intelligence sh!t\n at the time."
                                   + " This means you should always win \n unless you are so dumb . \n\n"
                                   + "[:] YOU CAN DO ANYTHING YOU WANT WITH \n THE CODE PROVIDED ALONG WITH THE GAME ,\n EXCEPT "
                                   + "CLAIM CREDIT FOR IT \n (such as by trying to copyright the code "
                                   + "yourself). \n\n Copyrights (c) by NiiMs Inc, All rights reserved  ",
                                   " Draughts ", JOptionPane.INFORMATION_MESSAGE, null);
                break;
        }
    }
    
}
