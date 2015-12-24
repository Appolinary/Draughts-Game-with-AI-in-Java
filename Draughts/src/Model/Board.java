package Model;

import java.awt.Dimension;
import java.util.ArrayList;

/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>
 */
public class Board {

      private final Field[][] gameState = new Field[8][8];
      private final MoveGenerator generator = new MoveGenerator();

      private static final int PostionPower[][] = {{4, 0, 4, 0, 4, 0, 4, 0},
      {0, 3, 0, 3, 0, 3, 0, 4}, {4, 0, 2, 0, 2, 0, 3, 0},
      {0, 3, 0, 2, 0, 2, 0, 4}, {4, 0, 2, 0, 2, 0, 3, 0},
      {0, 3, 0, 2, 0, 2, 0, 4}, {4, 0, 3, 0, 3, 0, 3, 0},
      {0, 4, 0, 4, 0, 4, 0, 4}};

      public Board() {

            for (int row = 0; row < 8; row++) {
                  for (int col = 0; col < 8; col++) {

                        if (row % 2 == col % 2) {
                              gameState[row][col] = new Field(row, col);

                              if (row < 3) {
                                    gameState[row][col].getPiece().setType(Constants.WHITE);
                              } else if (row > 4) {
                                    gameState[row][col].getPiece().setType(Constants.BLACK);
                              }
                        } else {
                              gameState[row][col] = new Field(row, col);

                        }
                  }
            }

      }

      public Board(Field[][] state) {

            setUpGame(state);

      }

      public Piece getPieceAt(Dimension Location) {

            return gameState[Location.width][Location.height].getPiece();

      }

      private void setUpGame(Field[][] state) {

            for (int i = 0; i < 8; i++) {
                  for (int j = 0; j < 8; j++) {
                        gameState[i][j] = new Field(i, j);
                        gameState[i][j].getPiece().setType(
                                state[i][j].getPiece().getType());

                  }
            }
      }

      public Field[][] getGameState() {
            return gameState;

      }

      public Board makeMove(Move move) {
            Board newBoard = new Board(getGameState());
            newBoard.makeActualMove(move);

            return newBoard;

      }

      public void makeActualMove(Move move) {

            if (move.isJump()) {
                  processJumps(move);
                  return;
            }

            int x = move.getSource().width, y = move.getSource().height;
            int r = move.getDestination().width, w = move.getDestination().height;

            if (gameState[x][y].getPiece().getType() == Constants.WHITE && r == 7) {
                  gameState[7][w].getPiece().setType(Constants.WHITE_KING);
            } else if (gameState[x][y].getPiece().getType() == Constants.BLACK
                    && r == 0) {
                  gameState[0][w].getPiece().setType(Constants.BLACK_KING);
            } else {
                  gameState[r][w].getPiece().setType(
                          gameState[x][y].getPiece().getType());
            }

            gameState[x][y].getPiece().setType(Constants.NULL);

      }

      private void processJumps(Move move) {

            int x = move.getSource().width, y = move.getSource().height; // source
            int r = move.getDestination().width, w = move.getDestination().height; // destination

            if (gameState[x][y].getPiece().getType() == Constants.WHITE && r == 7) {
                  gameState[7][w].getPiece().setType(Constants.WHITE_KING);
            } else if ((gameState[x][y].getPiece().getType() == (Constants.BLACK))
                    && r == 0) {
                  gameState[0][w].getPiece().setType(Constants.BLACK_KING);
            } else {
                  gameState[r][w].getPiece().setType(
                          gameState[x][y].getPiece().getType());
            }

            gameState[x][y].getPiece().setType(Constants.NULL);

            ArrayList<Dimension> jumpedPieces = move.getJumpedPieces();

            for (Dimension d : jumpedPieces) {
                  gameState[d.width][d.height].getPiece().setType(Constants.NULL);
            }

      }

      public ArrayList<Move> getAllLegalMovesForPlayer(boolean player) {

            return generator.generateAllMovesForSide(this, player);

      }

      public ArrayList<Move> getLegalMovesFor(Dimension source) {

            return generator.generateMovesFor(source, this);
      }

      public ArrayList<Move> getCanJumpFieldsFor(boolean player) {

            return generator.areJumpsAvailableFor(this, player);
      }

      public BestChoice MinMax(Board board, boolean player, int depth, int alpha,
              int beta) {
            
             if (depth == Constants.MAXIMUM_TREE_DEPTH) {
                  return new BestChoice(board.evaluationFunction(), null);
            }
             
              if (board.gameOver(player)) {
                  if (player == Constants.COMPUTER) {
                        return new BestChoice(Constants.MINUS_INFINITY, null);
                  } else {
                        return new BestChoice(Constants.PLUS_INFINITY, null);
                  }
            }         

            if (player == Constants.COMPUTER) {

                  BestChoice AlphaChoice = new BestChoice(player);
                  ArrayList<Move> legalMoves = board
                          .getAllLegalMovesForPlayer(player);
                  for (int i = 0; i < legalMoves.size(); i++) {

                        Board newBoard = board.makeMove(legalMoves.get(i));
                        BestChoice choice = MinMax(newBoard, !player, depth + 1, alpha,
                                beta);

                        if (choice.getScore() > alpha) {
                              alpha = choice.getScore();
                              AlphaChoice = new BestChoice(alpha, legalMoves.get(i));
                        }
                        if (alpha >= beta) {
                              return AlphaChoice;
                        }
                        if (choice.getScore() < alpha && i == 0) {
                              alpha = choice.getScore();
                              AlphaChoice = new BestChoice(alpha, legalMoves.get(i));
                        }
                  }

                  return AlphaChoice;
            } else {
                  BestChoice BetaChoice = new BestChoice(player);
                  ArrayList<Move> legalMoves = board
                          .getAllLegalMovesForPlayer(player);
                  for (int i = 0; i < legalMoves.size(); i++) {

                        Board newBoard = board.makeMove(legalMoves.get(i));
                        BestChoice choice = MinMax(newBoard, !player, depth + 1, alpha,
                                beta);

                        if (choice.getScore() < beta) {
                              beta = choice.getScore();
                              BetaChoice = new BestChoice(beta, legalMoves.get(i));
                        }
                        if (beta <= alpha) {
                              return new BestChoice(beta, legalMoves.get(i));
                        }
                        if (choice.getScore() > beta && i == 0) {
                              beta = choice.getScore();
                              BetaChoice = new BestChoice(beta, legalMoves.get(i));
                        }
                  }

                  return BetaChoice;
            }

      }

      private int evaluationFunction() {

            int WhiteForce = 0;
            int enemyForce = 0; // black
            int ourforce = 0;
            int enemyforce = 0;
            int ourkingsforce = 0;
            int enemyskingforce = 0;

            for (int i = 0; i < 8; i++) {
                  for (int j = 0; j < 8; j++) {

                        if (gameState[i][j].getPiece().getType() != Constants.NULL) {
                              if (gameState[i][j].getPiece().getType() == Constants.WHITE) {
                                    WhiteForce += calculateValue(gameState[i][j]);
                                    ourforce++;
                              } else if (gameState[i][j].getPiece().getType() == Constants.WHITE_KING) {
                                    WhiteForce += calculateValue(gameState[i][j]);
                                    ourkingsforce++;
                              } else if (gameState[i][j].getPiece().getType() == Constants.BLACK) {
                                    enemyForce += calculateValue(gameState[i][j]);
                                    enemyforce++;
                              } else if (gameState[i][j].getPiece().getType() == Constants.BLACK_KING) {
                                    enemyForce += calculateValue(gameState[i][j]);
                                    enemyskingforce++;
                              }

                        }
                  }
            }

            WhiteForce  *= ((ourforce * 15) + (ourkingsforce * 28));
            enemyForce  *= ((enemyforce * 15) + (enemyskingforce * 28));

            return WhiteForce - enemyForce;

      }

      private int calculateValue(Field piece) {
            int value = 0;
            int x = piece.getLocation().width;
            int y = piece.getLocation().height;

            if (piece.getPiece().getType() == Constants.BLACK) {
                  if (x >= 0 && x <= 4) {
                        value = 7;
                  } else {
                        value = 5;
                  }
            } else if (piece.getPiece().getType() == Constants.WHITE) {
                  if (x >= 4 && x <= 7) {
                        value = 7;
                  } else {
                        value = 5;
                  }
            }

            if (value != 0) {
                  return (value * PostionPower[x][y] * 20)
                          + (movesFromThatPosition(piece.getLocation()) * 15);
            }

            return (170 *( PostionPower[x][y]))
                    + (movesFromThatPosition(piece.getLocation()) * 25);
      }

      private int movesFromThatPosition(Dimension d) {

            ArrayList<Move> allMoves = getLegalMovesFor(d);
            int a = allMoves.size();
            int mult = 10;
            if(!allMoves.isEmpty() && allMoves.get(0).isJump())
                   a+=2;
            return a * mult;

      }


      public boolean gameOver(boolean player) {
            ArrayList<Move> list = getAllLegalMovesForPlayer(player);

            return list.isEmpty();

      }

      public void newGame() {
            for (int row = 0; row < 8; row++) {
                  for (int col = 0; col < 8; col++) {

                        if (row % 2 == col % 2) {

                              if (row < 3) {
                                    gameState[row][col].getPiece().setType(Constants.WHITE);
                              } else if (row > 4) {
                                    gameState[row][col].getPiece().setType(Constants.BLACK);
                              } else {
                                    gameState[row][col].getPiece().setType(Constants.NULL);
                              }
                        }
                  }
            }

      }
}
