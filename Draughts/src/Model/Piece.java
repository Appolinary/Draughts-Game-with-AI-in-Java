package Model;

/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>  
 */
public class Piece {

      private int type;

      public Piece() {
      }

      public Piece(int ttype) {

            type = ttype;
      }

      public void setType(int type) {

            this.type = type;
      }

      public int getType() {

            return type;
      }

      public boolean getSide() {

            if (type == Constants.WHITE || type == Constants.WHITE_KING) {
                  return Constants.COMPUTER;
            }

            return !Constants.COMPUTER;
      }
}
