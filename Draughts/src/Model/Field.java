package Model;

import java.awt.Dimension;

/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>  
 */
public class Field {

      private final Piece piece;
      private final Dimension location;

      public Field(int x, int y, int type) {

            location = new Dimension(x, y);
            piece = new Piece(type);

      }

      public Field(int x, int y) {

            location = new Dimension(x, y);
            piece = new Piece(Constants.NULL);

      }

      public Piece getPiece() {

            return piece;

      }

      public Dimension getLocation() {

            return location;
      }

      public boolean getSide() {

            return piece.getSide();
      }

      public int getKingType() {

            if (piece.getType() == Constants.BLACK
                    || piece.getType() == Constants.BLACK_KING) {
                  return Constants.BLACK_KING;
            } else if (piece.getType() == Constants.WHITE
                    || piece.getType() == Constants.WHITE_KING) {
                  return Constants.WHITE_KING;
            }
            return Constants.NULL;
      }

}
