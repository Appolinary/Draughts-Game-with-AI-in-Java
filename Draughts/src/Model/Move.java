package Model;

import java.awt.Dimension;
import java.util.ArrayList;


/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>  
 */
public class Move {

      private Dimension source;
      private Dimension destination;

      private final ArrayList<Dimension> jumpedPieces = new ArrayList<>();

      public Move() {
      }

      public Move(Dimension source, Dimension des) {

            this.source = source;
            this.destination = des;

      }

      public Move(Dimension source, Dimension jumpedPiece, Dimension des) {

            this.source = source;
            this.destination = des;
            jumpedPieces.add(jumpedPiece);

      }

      public Dimension getSource() {

            return source;

      }

      public Dimension getDestination() {

            return destination;
      }

      public ArrayList<Dimension> getJumpedPieces() {

            if (isJump()) {
                  return jumpedPieces;
            }

            return null;
      }

      public boolean isJump() {

            return !jumpedPieces.isEmpty();
      }

      public void addMove(Move newMove) {
            if (!newMove.isJump()) {
                  return;
            }

            jumpedPieces.addAll(newMove.getJumpedPieces());
            destination = newMove.getDestination();
            if (source == null) {
                  source = newMove.getSource();
            }

      }

      public void removeLastJumpedPiece() {
            if (!jumpedPieces.isEmpty()) {
                  jumpedPieces.remove(jumpedPieces.size() - 1);
            }

      }

}
