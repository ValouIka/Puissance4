package Controleur;

import javax.swing.*;
import java.awt.event.*;
import Modele.Board;
import Vue.BoardDisplay;
import Vue.MainFrame;
import Modele.AIType;

public class BoardController extends MouseAdapter {

    private Board board;
    private BoardDisplay display;
    private boolean over = false;
    private AIType ai_type;
    private int depth;
    private MainFrame mainFrame; // Référence pour mettre à jour l'UI
    public byte humanPlayer = 1; // Par défaut l'humain est joueur 1

    public void setHumanPlayer(byte p) {
        this.humanPlayer = p;
    }

    public BoardController(Board board, BoardDisplay display, AIType ai_type, int depth, MainFrame mainFrame) {
        this.board = board;
        this.display = display;
        this.ai_type = ai_type;
        this.depth = depth;
        this.mainFrame = mainFrame;
        display.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (over) return;

        int col = e.getX() / 75;
        int row = e.getY() / 75;
        if (col < 0 || col >= board.col) return;

        // Mode peinture
        if (board.paint) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                board.addPaintToken(row, col, (byte) 1);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                board.addPaintToken(row, col, (byte) 2);
            } else if (SwingUtilities.isMiddleMouseButton(e)) {
                board.addPaintToken(row, col, (byte) 0);
            }
            display.repaint();
            // Mise à jour des radios après un coup en mode peinture (le joueur change manuellement)
            mainFrame.updateRadiosFromBoard();
            display.clearSuggestion();
            return;
        }

        // Mode 0 joueur : IA contre IA
        if (board.getPlayersNumber() == 0) {
            byte result = 0;
            while (board.checkWin() == 0) {
                int bestCol = getAIMove();
                board.addToken(bestCol);
                result = board.checkWin();
                if (result != 0) {
                    over = true;
                    break;
                }
                board.player = (byte) (board.player == 1 ? 2 : 1);
            }
            display.repaint();
            mainFrame.updateRadiosFromBoard();
            display.clearSuggestion();
            if (over) {
                JOptionPane.showMessageDialog(display, "Victoire du joueur " + result + " !");
            }
            return;
        }

        // Gestion des tours pour 1 ou 2 joueurs
        boolean joueurAJoue = false;

        boolean isHumanTurn = (board.getPlayersNumber() == 2)
                || (board.getPlayersNumber() == 1 && board.player == humanPlayer);
        if (isHumanTurn) {
            board.addToken(col);
            joueurAJoue = true;
        }

        if (joueurAJoue) {
            byte result = board.checkWin();
            if (result != 0) {
                over = true;
            } else {
                board.player = (byte) (board.player == 1 ? 2 : 1);
            }
            display.repaint();
            display.clearSuggestion();
            mainFrame.updateRadiosFromBoard();
            if (over) {
                JOptionPane.showMessageDialog(display, "Victoire du joueur " + result + " !");
            }
        }

        if (board.getPlayersNumber() == 1 && board.player == 2) {
            jouerTourIA();
        }

        if (board.getPlayersNumber() == 1 && board.player != humanPlayer) {
            jouerTourIA();
        }
    }

    // Méthode rendue publique pour être appelée depuis MainFrame
    public void jouerTourIA() {
        if (over) return;

        int bestCol = getAIMove();
        board.addToken(bestCol);

        byte result = board.checkWin();
        if (result != 0) {
            over = true;
        } else {
            board.player = (byte) (board.player == 1 ? 2 : 1);
        }
        display.repaint();
        display.clearSuggestion();
        mainFrame.updateRadiosFromBoard();
        if (over) {
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

    public int getSuggestedMove() {
        if (ai_type == AIType.MINIMAX) {
            return board.getBestMove(depth);
        } else {
            return board.getRandomMove();
        }
    }
}