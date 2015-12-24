package Model;


/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>
 */
public class BestChoice {

      private final int value;
      private final Move move;

      public BestChoice(boolean player) {

            value = 0;

            move = null;

      }

      public BestChoice(int score, Move bestMove) {
            value = score;
            move = bestMove;

      }

      public int getScore() {
            return value;
      }

      public Move getBestMove() {
            return move;
      }

}
