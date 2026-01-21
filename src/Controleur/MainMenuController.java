package Controleur;

import Modele.Board;
import Vue.*;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class MainMenuController implements ActionListener {
    Board board;

    public MainMenuController(Board board){
        this.board = board;
    }

    public void actionPerformed(ActionEvent e) {
        JButton temp = null;
        if(e.getSource() instanceof JButton) {
            temp = (JButton)e.getSource();
        }

        if(temp.getText().equals("Partie 0 joueurs")){
            board.setPlayersNumber((byte)0);
            showBoard();
        }
        if(temp.getText().equals("Partie 1 joueurs")){
            board.setPlayersNumber((byte)1);
            showBoard();
        }
        if(temp.getText().equals("Partie 2 joueurs")){
            board.setPlayersNumber((byte)2);
            showBoard();
        }

        //Charger une partie
        if(temp.getText().equals("Load")){
            DataInputStream load;
            try {
                load = new DataInputStream(new FileInputStream("save.dat"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            for(int r = 0; r < board.row; r++){
                for(int c = 0; c < board.col; c++){
                    try {
                        board.board[r][c] = load.readByte();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        //Sauvegarder une partie
        if(temp.getText().equals("Save")){
            DataOutputStream save;
            try {
                save = new DataOutputStream(new FileOutputStream("save.dat"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            for(byte[] row : board.board){
                for(byte data : row){
                    try {
                        save.writeByte(data);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    private void showBoard() {
        BoardDisplay display = new BoardDisplay(board);
        BoardController bc = new BoardController(board, display);
        JFrame frame = new JFrame("Puissance 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.pack();
        frame.setVisible(true);
    }
}
