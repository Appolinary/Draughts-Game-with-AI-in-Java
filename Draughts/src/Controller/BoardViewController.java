package Controller;

import Model.Move;
import View.BoardView;
import View.FieldView;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>
 */
public class BoardViewController implements MouseListener {
    
    private final BoardView boardView;
    private final ArrayList<Dimension> allJumpingPieces = new ArrayList<>();
    
    public BoardViewController(BoardView boardview) {
        
        this.boardView = boardview;
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        FieldView clickedField = (FieldView) e.getSource();
        
        if (!boardView.getallJumpingPieces().isEmpty() && !boardView.getallJumpingPieces().contains(clickedField.getDimensionLocation())
            && !boardView.isValidSelection(clickedField.getDimensionLocation())) {
            
            return;
            
        }
        
        if (boardView.getSelectedField() == null) {
            if (!boardView.getallJumpingPieces().isEmpty()) {
                if (boardView.isValidSelection(clickedField.getDimensionLocation())
                    && boardView.getallJumpingPieces().contains(clickedField.getDimensionLocation())) {
                    
                    boardView.setSelectedField(clickedField.getDimensionLocation());
                    boardView.showDestinations();
                }
                
            } else if (boardView.getallJumpingPieces().isEmpty()) {
                if (boardView.isValidSelection(clickedField.getDimensionLocation())) {
                    
                    boardView.setSelectedField(clickedField.getDimensionLocation());
                    boardView.showDestinations();
                }
                
            }
        } else if (boardView.getSelectedField() != null) {
            if (boardView.isSelectionEqualToSelectedField(clickedField.getDimensionLocation())) {
                
                boardView.disselectSelectedField();
                boardView.clearCanJumpFields(boardView.getallJumpingPieces());
                boardView.getallJumpingPieces().addAll(boardView.showAllPiecesThatCanJump());
                
            } else if (boardView.isValidSelection(clickedField.getDimensionLocation())) {
                
                boardView.clearCanJumpFields(boardView.getallJumpingPieces());
                boardView.makeMoveTo(clickedField.getDimensionLocation());
                
                boardView.disselectSelectedField();
                
                Move computerMove = boardView.generateBestMoveForComputer();
                if (computerMove == null) {
                    JOptionPane
                    .showMessageDialog(
                                       boardView,
                                       "You have won !!!, Am jealous for you \n Guess you are smarter than my  evaluation function ",
                                       "Draughts", JOptionPane.PLAIN_MESSAGE);
                    boardView.newGame();
                    boardView.clearCanJumpFields(boardView.getallJumpingPieces());
                    return;
                    
                }
                
                boardView.makeMove(computerMove);
                
                if (boardView.hasTheOpponentLost()) {
                    JOptionPane
                    .showMessageDialog(
                                       boardView,
                                       "You have lost !!!, really sorry \n Guess you are dummer than my  evaluation function ",
                                       "Draughts", JOptionPane.PLAIN_MESSAGE);
                    boardView.newGame();
                    boardView.clearCanJumpFields(boardView.getallJumpingPieces());
                    return;
                }
                
                boardView.clearCanJumpFields(boardView.getallJumpingPieces());
                boardView.getallJumpingPieces().addAll(boardView.showAllPiecesThatCanJump());
            }
            
        }
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
    
}
