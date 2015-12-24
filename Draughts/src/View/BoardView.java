package View;

import Controller.BoardViewController;
import Model.BestChoice;
import Model.Board;
import Model.Constants;
import Model.Move;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.EtchedBorder;

/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>  
 */
public class BoardView extends JPanel {

      private Board board;
      private FieldView[][] fieldViews;
      private Dimension selectedField;
      private BoardViewController controller;

      public BoardView() {
            setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
                    Color.black, null));
            setLayout(new GridLayout(8, 8));
            initObjects();

      }

      private void initObjects() {
            selectedField = null;
            board = new Board();
            fieldViews = new FieldView[8][8];
            initAndAddFieldViews();

      }

      private void initAndAddFieldViews() {

            controller = new BoardViewController(this);

            for (int row = 0; row < 8; row++) {
                  for (int col = 0; col < 8; col++) {

                        if (row % 2 == col % 2) {

                              fieldViews[row][col] = new FieldView(row, col);
                              add(fieldViews[row][col]);
                              fieldViews[row][col].addMouseListener(controller);

                              if (row < 3) {
                                    fieldViews[row][col].setType(Constants.WHITE);
                              } else if (row > 4) {
                                    fieldViews[row][col].setType(Constants.BLACK);
                              }
                        } else {
                              fieldViews[row][col] = new FieldView(row, col);
                              fieldViews[row][col].setBackground(Color.MAGENTA);
                              add(fieldViews[row][col]);
                        }
                  }
            }

      }

      public boolean isValidSelection(Dimension selection) {

            if (selectedField == null) {
                  if (board.getPieceAt(selection).getSide() != Constants.COMPUTER
                          && board.getPieceAt(selection).getType() != Constants.NULL) {

                        ArrayList<Move> legalMoves = board.getLegalMovesFor(selection);
                        return !legalMoves.isEmpty();

                  }
            } else {

                  ArrayList<Move> legalMoves = board.getLegalMovesFor(selectedField);
                  for (Move move : legalMoves) {

                        Dimension des = move.getDestination();
                        if (des.equals(selection)) {
                              return true;
                        }
                  }

            }
            return false;

      }

      public boolean isSelectionEqualToSelectedField(Dimension selection) {

            if (selectedField != null) {
                  return selectedField.equals(selection);
            }
            return false;

      }

      public Dimension getSelectedField() {
            return selectedField;
      }

      public void setSelectedField(Dimension selectedField) {
            this.selectedField = selectedField;
      }

      public void disselectSelectedField() {
            resetDestinations();
            selectedField = null;
      }

      public void makeMove(Move move) {

            board.makeActualMove(move);
            if (move.isJump()) {
                  processJumps(move);
                  return;
            }

            int x = move.getSource().width, y = move.getSource().height;
            int r = move.getDestination().width, w = move.getDestination().height;

            if (fieldViews[x][y].getType() == Constants.WHITE && r == 7) {
                  fieldViews[7][w].setType(Constants.WHITE_KING);
            } else if (fieldViews[x][y].getType() == Constants.BLACK && r == 0) {
                  fieldViews[0][w].setType(Constants.BLACK_KING);
            } else {
                  fieldViews[r][w].setType(fieldViews[x][y].getType());
            }

            fieldViews[x][y].setType(Constants.NULL);
      }

      private void processJumps(Move move) {

            ArrayList<Dimension> jumpedPieces = move.getJumpedPieces();

            for (Dimension d : jumpedPieces) {
                  fieldViews[d.width][d.height].setType(Constants.NULL);

            }

            int x = move.getSource().width, y = move.getSource().height; // source
            int r = move.getDestination().width, w = move.getDestination().height; // destination

            if (fieldViews[x][y].getType() == Constants.WHITE && r == 7) {
                  fieldViews[7][w].setType(Constants.WHITE_KING);
            } else if (fieldViews[x][y].getType() == Constants.BLACK && r == 0) {
                  fieldViews[0][w].setType(Constants.BLACK_KING);
            } else {
                  fieldViews[r][w].setType(fieldViews[x][y].getType());
            }

            fieldViews[x][y].setType(Constants.NULL);
            try {

                  Thread.sleep(150);

            } catch (Exception e) {
            }

      }

      // called when selectedField != null
      public void makeMoveTo(Dimension to) {

            resetDestinations();
            ArrayList<Move> legalMoves = board.getLegalMovesFor(selectedField);
            for (Move move : legalMoves) {

                  if (to.equals(move.getDestination())) {
                        makeMove(move);
                        return;
                  }

            }

      }

      // calls the minmax
      public Move generateBestMoveForComputer() {

            BestChoice best = board.MinMax(board, Constants.COMPUTER, 0,
                    Constants.MINUS_INFINITY, Constants.PLUS_INFINITY);

            return best.getBestMove();
      }

      public void showDestinations() {

            fieldViews[selectedField.width][selectedField.height]
                    .setBorder(BorderFactory.createLineBorder(Color.yellow, 5));

            ArrayList<Move> allDes = board.getLegalMovesFor(selectedField);
            for (Move m : allDes) {

                  fieldViews[m.getDestination().width][m.getDestination().height]
                          .setBorder(BorderFactory.createLineBorder(Color.red, 5));

            }

      }

      private void resetDestinations() {
            if (selectedField == null) {
                  return;
            }

            fieldViews[selectedField.width][selectedField.height].setBorder(null);
            ArrayList<Move> allDes = board.getLegalMovesFor(selectedField);
            for (Move m : allDes) {

                  fieldViews[m.getDestination().width][m.getDestination().height]
                          .setBorder(null);

            }

      }

      public ArrayList<Dimension> showAllPiecesThatCanJump() {

            ArrayList<Move> moves = board.getCanJumpFieldsFor(!Constants.COMPUTER);

            Set<Dimension> set = new HashSet<>();

            for (Move move : moves) {
                  Dimension source = move.getSource();
                  set.add(source);
                  paintIt(source, true);

            }
            return new ArrayList<>(set);
      }

      private void paintIt(Dimension d, boolean how) {
            if (how == true) {
                  fieldViews[d.width][d.height].setBorder(BorderFactory
                          .createLineBorder(Color.blue, 2, true));
            } else {
                  fieldViews[d.width][d.height].setBorder(null);
            }
      }

      public void clearCanJumpFields(ArrayList<Dimension> list) {

            for (Dimension d : list) {
                  paintIt(d, false);
            }

            list.clear();

      }

      public void newGame() {

            disselectSelectedField();
            board.newGame();
            layOutTheGame();
            clearCanJumpFields(getallJumpingPieces());

      }

      public void layOutTheGame() {
            for (int row = 0; row < 8; row++) {
                  for (int col = 0; col < 8; col++) {

                        if (row % 2 == col % 2) {

                              if (row < 3) {
                                    fieldViews[row][col].setType(Constants.WHITE);
                              } else if (row > 4) {
                                    fieldViews[row][col].setType(Constants.BLACK);
                              } else {
                                    fieldViews[row][col].setType(Constants.NULL);
                              }

                        }
                  }

            }
      }

      public boolean hasTheOpponentLost() {

            return board.gameOver(!Constants.COMPUTER);

      }

      public ArrayList<Dimension> getallJumpingPieces() {

            return allJumpingPieces;
      }
      private final ArrayList<Dimension> allJumpingPieces = new ArrayList<>();
}
