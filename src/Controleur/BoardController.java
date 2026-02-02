package Controleur;

import javax.swing.*;
import java.awt.event.*;
import Modele.Board;
import Vue.BoardDisplay;
import Vue.MainMenu;

public class BoardController extends MouseAdapter{

    private Board board;
    private BoardDisplay display;
    private boolean over = false;

    public BoardController(Board board, BoardDisplay display){
        this.board = board;
        this.display = display;
        display.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(over){
            return;
        }

        int col = e.getX()/100;
        if(col < 0 || col >= board.col){
            return;
        }

        if(board.getPlayersNumber() == 0){
            while(board.checkWin() == 0){
                board.addTokenAlea();
            }
        }

        //Joue automatiquement si c'est au tour de l'ordinateur
        if((board.getPlayersNumber() == 1 && board.player == 2)){
            board.addTokenAlea();
        }

        //Permet au joueur actuel de jouer
        else if((board.getPlayersNumber() == 2) || (board.getPlayersNumber() == 1 && board.player == 1)){
            board.addToken(col);
        }
        byte result = board.checkWin();
        display.repaint();
        if(result != 0){
            System.out.println("test");
            over = true;
            JOptionPane.showMessageDialog(display,
                    "Victoire du joueur "+result+" !");
        }



    }
}
