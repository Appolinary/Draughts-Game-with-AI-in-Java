package View;

import Controller.JMenuBarController;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>  
 */
public class WholeGameView extends JFrame {

      private final JMenuBarController controller;
      private final BoardView boardView;
      private final JLabel NiiMs = new JLabel(
              "copyright (c) by NiiMs Inc , All rights reserved.");

      public WholeGameView() {

            setLayout(null);
            setTitle("Draughts");
            setLocation(250, 150);
            boardView = new BoardView();

            add(boardView);
            boardView.setBounds(10, 10, 380, 380);

            add(NiiMs);
            NiiMs.setBounds(90, 400, 300, 20);

            setDefaultCloseOperation(3);
            setResizable(false);
            setSize(409, 483);

            controller = new JMenuBarController(this);

            addMenuBar();
            getContentPane().setBackground(Color.lightGray);
            setIconImage(new ImageIcon(this.getClass().getResource("N.jpg"))
                    .getImage());

            setVisible(true);
      }

      private void addMenuBar() {

            JMenuBar bar = new JMenuBar();
            JMenu Game = new JMenu("Game");
            JMenuItem item0 = new JMenuItem("New Game                        ",
                    new ImageIcon(this.getClass().getResource("new.jpg")));
            JMenuItem item1 = new JMenuItem("Exit                            ",
                    new ImageIcon(this.getClass().getResource("x.jpg")));
            item0.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                    ActionEvent.CTRL_MASK));
            item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                    ActionEvent.CTRL_MASK));
            item0.addActionListener(controller);
            item1.addActionListener(controller);
            Game.add(item0);
            Game.addSeparator();
            Game.add(item1);
            bar.add(Game);

            JMenu Help = new JMenu("Help");
            JMenuItem about = new JMenuItem("About                ");
            JMenuItem How = new JMenuItem("How to play                  ");
            How.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                    ActionEvent.CTRL_MASK));
            about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    ActionEvent.CTRL_MASK));
            How.addActionListener(controller);
            about.addActionListener(controller);
            Help.add(How);
            Help.add(about);
            bar.add(Help);

            setJMenuBar(bar);

      }

      public void newGame() {
            boardView.newGame();
      }

      public static void main(String args[]) {

            try {

                  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                  System.out.println("Error , could not load");
            }
            WholeGameView v = new WholeGameView();

      }

}
