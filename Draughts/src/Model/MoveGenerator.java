package Model;

import java.awt.Dimension;
import java.util.ArrayList;

/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>
 */
public class MoveGenerator {

      private final Field[][] Matrix = new Field[8][8];
      private Piece sourcePiece = new Piece();

      public MoveGenerator() {

            for (int i = 0; i < 8; i++) {
                  for (int j = 0; j < 8; j++) {
                        Matrix[i][j] = new Field(i, j);
                  }
            }
      }

      private void setMatrix(Field[][] newMatrix) {
            for (int i = 0; i < 8; i++) {
                  for (int j = 0; j < 8; j++) {
                        Matrix[i][j].getPiece().setType(
                                newMatrix[i][j].getPiece().getType());
                  }
            }
      }

      private ArrayList<Move> canPieceJump(Dimension from, Board board) {

            sourcePiece.setType(Matrix[from.width][from.height].getPiece()
                    .getType());
            ArrayList<Move> list = new ArrayList<>();
            Move move = new Move();

            jumpingMovesDepthFirstSearch(from, board, list, move, from);
            return list;
      }

      public ArrayList<Move> generateMovesFor(Dimension from, Board board) {

            ArrayList<Move> list = new ArrayList<>();
            Move move = new Move();

            jumpingMovesDepthFirstSearch(from, board, list, move, from);
            if (!list.isEmpty()) {
                  return list;
            }

            ArrayList<Move> legalMoves2 = generateMereMovesFor(from, board);

            return legalMoves2;

      }

      private ArrayList<Move> generateMereMovesFor(Dimension from, Board board) {
            setMatrix(board.getGameState());
            ArrayList<Move> legalMoves2 = generateSimpleMoves(from);

            return legalMoves2;

      }

      private ArrayList<Move> generateFirstJumpingMoves(Dimension from,
              Board board) {
            setMatrix(board.getGameState());
            ArrayList<Move> legalMoves = generateJumpingMoves(from);

            return legalMoves;

      }

      private Move jumpingMovesDepthFirstSearch(Dimension source, Board board,
              ArrayList<Move> list, Move MOVE, Dimension initialSource) {

            MoveGenerator anotherGenerator = new MoveGenerator();

            ArrayList<Move> generatedMoves = anotherGenerator
                    .generateFirstJumpingMoves(source, board);

            if (generatedMoves.size() <= 0
                    || (source.width == 7
                    && board.getPieceAt(source).getSide() == Constants.COMPUTER
                    && !source.equals(initialSource) && sourcePiece
                    .getType() != Constants.WHITE_KING)
                    || (source.width == 0
                    && board.getPieceAt(source).getSide() == !Constants.COMPUTER
                    && !source.equals(initialSource) && sourcePiece
                    .getType() != Constants.BLACK_KING)) {
                  return MOVE;
            }

            for (Move move : generatedMoves) {

                  Board newBoard = board.makeMove(move);
                  MOVE.addMove(move);

                  Move nextJump = jumpingMovesDepthFirstSearch(move.getDestination(),
                          newBoard, list, MOVE, initialSource);

                  if (nextJump != null) {
                        Move realMove = new Move();
                        realMove.addMove(nextJump);
                        list.add(realMove);
                  }
                  MOVE.removeLastJumpedPiece();
            }

            return null;

      }

      private ArrayList<Move> generateJumpingMoves(Dimension from) {

            if (Matrix[from.width][from.height].getPiece().getType() == Constants.BLACK_KING
                    || Matrix[from.width][from.height].getPiece().getType() == Constants.WHITE_KING) {
                  return jumpingMovesForKing(from);
            }

            return jumpingMovesForSimplePlayer(from);

      }

      private ArrayList<Move> generateSimpleMoves(Dimension from) {

            if (Matrix[from.width][from.height].getPiece().getType() == Constants.BLACK_KING
                    || Matrix[from.width][from.height].getPiece().getType() == Constants.WHITE_KING) {

                  return simpleMovesForKing(from);
            }

            return simpleMovesForSimplePlayer(from);

      }

      private ArrayList<Move> simpleMovesForSimplePlayer(Dimension from) {

            int x = from.width, y = from.height;
            ArrayList<Move> allMoves = new ArrayList<>();

            if (Matrix[x][y].getPiece().getType() == Constants.BLACK) {

                  Field Right = null, Left = null;

                  if (x - 1 >= 0 && y - 1 >= 0) {
                        Right = Matrix[x - 1][y - 1];
                  }
                  if (y + 1 <= 7 && x - 1 >= 0) {
                        Left = Matrix[x - 1][y + 1];
                  }

                  if (Right != null && Right.getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Right.getLocation()));
                  }

                  if (Left != null && Left.getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Left.getLocation()));
                  }

            } else if (Matrix[x][y].getPiece().getType() == Constants.WHITE) {
                  Field Right = null, Left = null;

                  if (y + 1 < 8 && x + 1 < 8) {
                        Right = Matrix[x + 1][y + 1];
                  }
                  if (y - 1 >= 0 && x + 1 <= 7) {
                        Left = Matrix[x + 1][y - 1];
                  }

                  if (Right != null && Right.getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Right.getLocation()));
                  }
                  if (Left != null && Left.getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Left.getLocation()));
                  }
            }

            return allMoves;

      }

      private ArrayList<Move> simpleMovesForKing(Dimension king) {

            ArrayList<Move> allMoves = new ArrayList<>();

            // get the location of the king piece
            int x = king.width, y = king.height;

            // in the south easterly direction
            int temp_x = x, temp_y = y;
            while (temp_x + 1 <= 7
                    && temp_y + 1 <= 7
                    && Matrix[temp_x + 1][temp_y + 1].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(king, Matrix[temp_x + 1][temp_y + 1]
                          .getLocation()));
                  temp_x++;
                  temp_y++;
            }
            // in the north westerly direction
            int temp_xx = x, temp_yy = y;
            while (temp_xx - 1 >= 0
                    && temp_yy - 1 >= 0
                    && Matrix[temp_xx - 1][temp_yy - 1].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(king, Matrix[temp_xx - 1][temp_yy - 1]
                          .getLocation()));
                  temp_xx--;
                  temp_yy--;
            }
            // in the south westerly direction
            int temp_xxx = x, temp_yyy = y;
            while (temp_xxx - 1 >= 0
                    && temp_yyy + 1 <= 7
                    && Matrix[temp_xxx - 1][temp_yyy + 1].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(king, Matrix[temp_xxx - 1][temp_yyy + 1]
                          .getLocation()));
                  temp_xxx--;
                  temp_yyy++;
            }
            // check the north easterly direction
            int r = x, w = y;
            while (r + 1 <= 7 && w - 1 >= 0
                    && Matrix[r + 1][w - 1].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(king, Matrix[r + 1][w - 1].getLocation()));
                  r++;
                  w--;
            }

            return allMoves;
      }

      private ArrayList<Move> jumpingMovesForKing(Dimension from) {
            int x = from.width, y = from.height;

            ArrayList<Move> allMoves = new ArrayList<>();

            // in the south easterly direction
            int temp_x = x, temp_y = y;
            while (temp_x + 1 <= 7
                    && temp_y + 1 <= 7
                    && Matrix[temp_x + 1][temp_y + 1].getPiece().getType() == Constants.NULL) {
                  temp_x++;
                  temp_y++;
            }
            temp_x++;
            temp_y++;
            if (temp_x <= 7
                    && temp_y <= 7
                    && !(Matrix[temp_x][temp_y].getPiece().getType() == Matrix[x][y]
                    .getPiece().getType() || Matrix[temp_x][temp_y]
                    .getKingType() == Matrix[x][y].getPiece().getType())) {
                  int n = temp_x, m = temp_y;
                  temp_x++;
                  temp_y++;
                  while (temp_x <= 7
                          && temp_y <= 7
                          && Matrix[temp_x][temp_y].getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Matrix[n][m].getLocation(),
                                Matrix[temp_x][temp_y].getLocation()));
                        temp_x++;
                        temp_y++;
                  }

            }

            // in the north westerly direction
            int temp_xx = x, temp_yy = y;
            while (temp_xx - 1 >= 0
                    && temp_yy - 1 >= 0
                    && Matrix[temp_xx - 1][temp_yy - 1].getPiece().getType() == Constants.NULL) {
                  temp_xx--;
                  temp_yy--;
            }
            temp_xx--;
            temp_yy--;
            if (temp_xx > 0
                    && temp_yy > 0
                    && !(Matrix[temp_xx][temp_yy].getPiece().getType() == Matrix[x][y]
                    .getPiece().getType() || Matrix[temp_xx][temp_yy]
                    .getKingType() == Matrix[x][y].getPiece().getType())) {
                  int n = temp_xx, m = temp_yy;
                  temp_xx--;
                  temp_yy--;
                  while (temp_xx >= 0
                          && temp_yy >= 0
                          && Matrix[temp_xx][temp_yy].getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Matrix[n][m].getLocation(),
                                Matrix[temp_xx--][temp_yy--].getLocation()));
                  }

            }
            // in the south westerly direction
            int temp_xxx = x, temp_yyy = y;
            while (temp_xxx - 1 >= 0
                    && temp_yyy + 1 <= 7
                    && Matrix[temp_xxx - 1][temp_yyy + 1].getPiece().getType() == Constants.NULL) {
                  temp_xxx--;
                  temp_yyy++;
            }
            temp_xxx--;
            temp_yyy++;
            if (temp_xxx > 0
                    && temp_yyy < 7
                    && !(Matrix[temp_xxx][temp_yyy].getPiece().getType() == Matrix[x][y]
                    .getPiece().getType() || Matrix[temp_xxx][temp_yyy]
                    .getKingType() == Matrix[x][y].getPiece().getType())) {
                  int n = temp_xxx, m = temp_yyy;
                  temp_xxx--;
                  temp_yyy++;
                  while (temp_xxx >= 0
                          && temp_yyy <= 7
                          && Matrix[temp_xxx][temp_yyy].getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Matrix[n][m].getLocation(),
                                Matrix[temp_xxx--][temp_yyy++].getLocation()));
                  }
            }
            // check the north easterly direction
            int r = x, w = y;
            while (r + 1 <= 7 && w - 1 >= 0
                    && Matrix[r + 1][w - 1].getPiece().getType() == Constants.NULL) {

                  r++;
                  w--;
            }
            r++;
            w--;
            if (r < 7
                    && w > 0
                    && !(Matrix[r][w].getPiece().getType() == Matrix[x][y]
                    .getPiece().getType() || Matrix[r][w].getKingType() == Matrix[x][y]
                    .getPiece().getType())) {
                  int n = r, m = w;
                  r++;
                  w--;
                  while (r <= 7 && w >= 0
                          && Matrix[r][w].getPiece().getType() == Constants.NULL) {
                        allMoves.add(new Move(from, Matrix[n][m].getLocation(),
                                Matrix[r++][w--].getLocation()));

                  }

            }

            return allMoves;
      }

      private ArrayList<Move> jumpingMovesForSimplePlayer(Dimension from) {

            int x = from.width, y = from.height;
            int king = Matrix[x][y].getKingType();

            ArrayList<Move> allMoves = new ArrayList<>();

            if (x + 1 < 7
                    && x + 2 <= 7
                    && y - 1 > 0
                    && y - 2 >= 0
                    && Matrix[x + 1][y - 1].getPiece().getType() != Constants.NULL
                    && Matrix[x + 1][y - 1].getPiece().getType() != king
                    && Matrix[x + 1][y - 1].getPiece().getType() != Matrix[x][y]
                    .getPiece().getType()
                    && Matrix[x + 2][y - 2].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(from, Matrix[x + 1][y - 1].getLocation(),
                          Matrix[x + 2][y - 2].getLocation()));
            }

            if (x + 1 < 7
                    && x + 2 <= 7
                    && y + 1 < 7
                    && y + 2 <= 7
                    && Matrix[x + 1][y + 1].getPiece().getType() != Constants.NULL
                    && Matrix[x + 1][y + 1].getPiece().getType() != king
                    && Matrix[x + 1][y + 1].getPiece().getType() != Matrix[x][y]
                    .getPiece().getType()
                    && Matrix[x + 2][y + 2].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(from, Matrix[x + 1][y + 1].getLocation(),
                          Matrix[x + 2][y + 2].getLocation()));
            }

            if (x - 1 > 0
                    && x - 2 >= 0
                    && y - 1 > 0
                    && y - 2 >= 0
                    && Matrix[x - 1][y - 1].getPiece().getType() != Constants.NULL
                    && Matrix[x - 1][y - 1].getPiece().getType() != king
                    && Matrix[x - 1][y - 1].getPiece().getType() != Matrix[x][y]
                    .getPiece().getType()
                    && Matrix[x - 2][y - 2].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(from, Matrix[x - 1][y - 1].getLocation(),
                          Matrix[x - 2][y - 2].getLocation()));
            }

            if (x - 1 > 0
                    && x - 2 >= 0
                    && y + 1 < 7
                    && y + 2 <= 7
                    && Matrix[x - 1][y + 1].getPiece().getType() != Constants.NULL
                    && Matrix[x - 1][y + 1].getPiece().getType() != king
                    && Matrix[x - 1][y + 1].getPiece().getType() != Matrix[x][y]
                    .getPiece().getType()
                    && Matrix[x - 2][y + 2].getPiece().getType() == Constants.NULL) {
                  allMoves.add(new Move(from, Matrix[x - 1][y + 1].getLocation(),
                          Matrix[x - 2][y + 2].getLocation()));
            }

            return allMoves;
      }

      public ArrayList<Move> areJumpsAvailableFor(Board board, boolean player) {

            ArrayList<Move> jumpingPieces = new ArrayList<>();
            Field[][] gameState = board.getGameState();

            for (int i = 0; i < 8; i++) {
                  for (int j = 0; j < 8; j++) {

                        if (gameState[i][j].getPiece().getType() != Constants.NULL
                                && gameState[i][j].getSide() == player) {
                              ArrayList<Move> jumpingMoves = canPieceJump(
                                      gameState[i][j].getLocation(), board);

                              if (jumpingMoves.size() > 0) {
                                    jumpingPieces.addAll(jumpingMoves);
                              }

                        }
                  }
            }

            return jumpingPieces;
      }

      public ArrayList<Move> generateAllMovesForSide(Board board, boolean player) {

            ArrayList<Move> jumpingMovesFirst = areJumpsAvailableFor(board, player);
            if (!jumpingMovesFirst.isEmpty()) {
                  return jumpingMovesFirst;
            }

            Field[][] gameState = board.getGameState();
            ArrayList<Move> allMoves = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                  for (int j = 0; j < 8; j++) {

                        if (gameState[i][j].getPiece().getType() != Constants.NULL
                                && gameState[i][j].getSide() == player) {
                              ArrayList<Move> moves = generateMereMovesFor(new Dimension(
                                      i, j), board);

                              allMoves.addAll(moves);

                        }
                  }
            }

            return allMoves;

      }

}
