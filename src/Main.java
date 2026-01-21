import Controleur.BoardController;
import Modele.Board;
import Vue.BoardDisplay;
import Vue.MainMenu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Board board = new Board(6,7);
        MainMenu menu = new MainMenu(board);




    }
}

