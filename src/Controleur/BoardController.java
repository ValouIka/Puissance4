package Controleur;

import javax.swing.*;
import java.awt.event.*;
import Modele.Board;
import Vue.BoardDisplay;
import Modele.AIType;

public class BoardController extends MouseAdapter {

    private Board board;
    private BoardDisplay display;
    private boolean over = false;
    private AIType ai_type;
    private int depth;
    private byte humanToken = 1;

    public BoardController(Board board, BoardDisplay display, AIType ai_type, int depth) {
        this.board = board;
        this.display = display;
        this.ai_type = ai_type;
        this.depth = depth;
        display.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (over) {
            return;
        }

        int col = e.getX() / 75;
        if (col < 0 || col >= board.col) {
            return;
        }

        // Mode 0 joueur : IA contre IA
        if (board.getPlayersNumber() == 0) {
            byte result = 0;
            while (board.checkWin() == 0) {
                int bestCol = getAIMove();
                board.addToken(bestCol);
                // Après chaque coup, vérifier la victoire
                result = board.checkWin();
                if (result != 0) {
                    over = true;
                    break;
                }
                // Changer de joueur manuellement
                board.player = (byte) (board.player == 1 ? 2 : 1);
            }
            display.repaint();
            if(over){
                JOptionPane.showMessageDialog(display, "Victoire du joueur " + result + " !");
            }
            return;
        }

// Gestion des tours pour 1 ou 2 joueurs
        boolean joueurAJoue = false;


// Cas : joueur humain peut jouer (mode 2 joueurs, ou mode 1 joueur et c'est le tour de l'humain)
        if (board.getPlayersNumber() == 2 || (board.getPlayersNumber() == 1 && board.player == 1)) {
            board.addToken(col);
            joueurAJoue = true;
        }

        if (joueurAJoue) {
            // Vérifier la victoire
            System.out.println("Doit jouer: "+getAIMove());
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
        if (board.getPlayersNumber() == 1 && board.player == 2) {
            jouerTourIA();
        }

    }

    private void jouerTourIA() {
        if (over) return;

        int bestCol = getAIMove();
        System.out.println("Doit jouer: "+bestCol);
        board.addToken(bestCol);

        byte result = board.checkWin();
        if (result != 0) {
            over = true;
        } else {
            // Après le coup de l'IA, on repasse au joueur humain (ou à l'autre IA si mode 0, mais ici on est en mode 1)
            board.player = (byte) (board.player == 1 ? 2 : 1);
        }
        display.repaint();
        if(over){
            JOptionPane.showMessageDialog(display, "Victoire du joueur " + result + " !");
        }
    }

    private int getAIMove() {
        if (ai_type == AIType.MINIMAX) {
            return board.getBestMove(depth);
        } else {
            return board.getRandomMove();
        }
    }
}

