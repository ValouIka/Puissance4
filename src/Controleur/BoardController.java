package Controleur;

import javax.swing.*;
import java.awt.event.*;
import Modele.Board;
import Vue.BoardDisplay;
import Vue.MainMenu;

public class BoardController extends MouseAdapter {

    private Board board;
    private BoardDisplay display;
    private boolean over = false;

    public BoardController(Board board, BoardDisplay display) {
        this.board = board;
        this.display = display;
        display.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (over) {
            return;
        }

        int col = e.getX() / 100;
        if (col < 0 || col >= board.col) {
            return;
        }

        // Mode 0 joueur : IA contre IA
        if (board.getPlayersNumber() == 0) {
            while (board.checkWin() == 0) {
                int bestCol = board.getBestMove(6);
                board.addToken(bestCol);
                // Après chaque coup, vérifier la victoire
                byte result = board.checkWin();
                if (result != 0) {
                    over = true;
                    JOptionPane.showMessageDialog(display, "Victoire du joueur " + result + " !");
                    break;
                }
                // Changer de joueur manuellement
                board.player = (byte) (board.player == 1 ? 2 : 1);
            }
            display.repaint();
            return;
        }

// Gestion des tours pour 1 ou 2 joueurs
        boolean joueurAJoue = false;

// Cas : ordinateur doit jouer (mode 1 joueur et c'est le tour de l'ordi)
        if (board.getPlayersNumber() == 1 && board.player == 2) {
            int bestCol = board.getBestMove(8);
            board.addToken(bestCol);
            joueurAJoue = true;
        }
// Cas : joueur humain peut jouer (mode 2 joueurs, ou mode 1 joueur et c'est le tour de l'humain)
        else if (board.getPlayersNumber() == 2 || (board.getPlayersNumber() == 1 && board.player == 1)) {
            board.addToken(col);
            joueurAJoue = true;
        }

        if (joueurAJoue) {
            // Vérifier la victoire
            byte result = board.checkWin();
            if (result != 0) {
                over = true;
            } else {
                // Pas de victoire : passer au joueur suivant
                board.player = (byte) (board.player == 1 ? 2 : 1);
            }
            display.repaint();
            if(over){
                JOptionPane.showMessageDialog(display, "Victoire du joueur " + result + " !");
            }
        }

    }

}