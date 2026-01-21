package Vue;

import javax.swing.*;
import java.awt.*;

import Controleur.MainMenuController;
import Modele.Board;

public class MainMenu extends JFrame {
    JButton play0 = new JButton("Partie 0 joueurs");
    JButton play1 = new JButton("Partie 1 joueurs");
    JButton play2 = new JButton("Partie 2 joueurs");
    JButton load = new JButton("Load");
    JButton save = new JButton("Save");
    JPanel boutonsMenu = new JPanel();

    public MainMenu(Board board) {
        this.setTitle("Menu principal");
        this.getContentPane().setLayout(new BorderLayout());
        boutonsMenu.setLayout(new GridLayout(2, 3));
        boutonsMenu.add(play0);
        boutonsMenu.add(play1);
        boutonsMenu.add(play2);
        boutonsMenu.add(load);
        boutonsMenu.add(save);
        this.getContentPane().add(BorderLayout.CENTER, boutonsMenu);

        //Ajout controleurs
        MainMenuController c = new MainMenuController(board);
        play0.addActionListener(c);
        play1.addActionListener(c);
        play2.addActionListener(c);
        save.addActionListener(c);
        load.addActionListener(c);


        //Affichage de la fenêtre
        setSize(800,600);
        setLocationRelativeTo(null);
        this.show();
        setVisible(true);
        this.pack();
    }
}
