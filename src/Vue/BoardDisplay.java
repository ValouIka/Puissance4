package Vue;

import java.awt.*;
import javax.swing.*;
import Modele.Board;

import javax.swing.*;
import java.awt.*;

public class BoardDisplay extends JPanel {

    private Board board;
    private int suggestedColumn = -1;

    public BoardDisplay(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(75*board.col, 75*board.row));
    }

    public void setSuggestedColumn(int col) {
        this.suggestedColumn = col;
        repaint();
    }

    public void clearSuggestion() {
        this.suggestedColumn = -1;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cell = 75;

        if (suggestedColumn >= 0 && suggestedColumn < board.col) {
            // Exemple : rectangle semi-transparent au-dessus de la colonne
            g.setColor(new Color(0, 255, 0, 80));
            g.fillRect(suggestedColumn * cell, 0, cell, cell / 3);
            // Petit triangle indicateur
            g.setColor(Color.GREEN.darker());
            int[] xPoints = {suggestedColumn * cell + cell/2,
                    suggestedColumn * cell + cell/2 - 10,
                    suggestedColumn * cell + cell/2 + 10};
            int[] yPoints = {10, 25, 25};
            g.fillPolygon(xPoints, yPoints, 3);
        }


        for (int r = 0; r < board.row; r++) {
            for (int c = 0; c < board.col; c++) {

                g.setColor(Color.BLACK);
                g.fillRect(c * cell, r * cell, cell, cell);

                g.setColor(Color.WHITE);
                g.fillOval(c * cell + 10, r * cell + 10,
                        cell - 20, cell - 20);

                if (board.getValue(r, c) == 1)
                    g.setColor(Color.RED);
                else if (board.getValue(r, c) == 2)
                    g.setColor(Color.YELLOW);
                else if (board.getValue(r, c) == 3)
                    g.setColor(Color.GREEN);
                else
                    continue;

                g.fillOval(c * cell + 10, r * cell + 10,
                        cell - 20, cell - 20);
            }
        }
    }
}
