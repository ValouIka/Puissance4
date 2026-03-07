package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {

    public JButton btn0 = new JButton("Partie 0 joueurs");
    public JButton btn1 = new JButton("Partie 1 joueurs");
    public JButton btn2 = new JButton("Partie 2 joueurs");
    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(150, 0)); // largeur fixe

        // Ajout des boutons avec un peu d'espacement
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(btn0);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btn1);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btn2);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(btnLoad);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnSave);
    }

    // Pour attacher un seul écouteur à tous les boutons (optionnel)
    public void setButtonListener(ActionListener listener) {
        btn0.addActionListener(listener);
        btn1.addActionListener(listener);
        btn2.addActionListener(listener);
        btnLoad.addActionListener(listener);
        btnSave.addActionListener(listener);
    }
}