package View;

import Model.Constants;
import java.awt.*;
import javax.swing.*;


/* @author Fabrice Appolinary <fabricyappolinary@gmail.com>  
 */
public class FieldView extends JLabel {

      private int type;
      private final Dimension Location;

      FieldView(int row, int col) {
            setPreferredSize(new Dimension(35, 35));
            setBorder(null);
            setFocusable(true);
            setOpaque(true);
            Location = new Dimension(row, col);
      }

      public void setType(int type) {

            this.type = type;

            reDraw();

      }

      private void reDraw() {
            if (type == Constants.WHITE) {
                  setIcon(new ImageIcon(this.getClass().getResource("wp.png")));
            } else if (type == Constants.BLACK) {
                  ImageIcon icon = new ImageIcon(this.getClass().getResource("bp.png"));
                  setIcon(icon);

            } else if (type == Constants.NULL) {
                  setIcon(null);
            } else if (type == Constants.BLACK_KING) {
                  ImageIcon icon = new ImageIcon(this.getClass().getResource("bk.png"));
                  setIcon(icon);
            } else if (type == Constants.WHITE_KING) {
                  ImageIcon icon = new ImageIcon(this.getClass().getResource("wk.png"));
                  setIcon(icon);
            }
      }

      public int getType() {
            return type;
      }

      public Dimension getDimensionLocation() {

            return Location;
      }

}
