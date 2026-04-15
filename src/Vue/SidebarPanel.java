package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {

    String[] modes = {"Play", "Paint"};
    String[] options = {"Random", "MiniMax"};
    Integer[] depthList = {1,2,3,4,5,6,7,8,9};

    public JButton btn0 = new JButton("Partie 0 joueurs");
    public JButton btn1 = new JButton("Partie 1 joueurs");
    public JButton btn2 = new JButton("Partie 2 joueurs");
    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");
    public JButton confirm = new JButton("Change mode");
    public JComboBox<String> mode = new JComboBox<>(modes);
    public JComboBox<String> CmbAi = new JComboBox<>(options);
    public JComboBox<Integer> depth = new JComboBox<>(depthList);

    // Nouveaux composants radio
    public JRadioButton player1Radio = new JRadioButton("Joueur 1");
    public JRadioButton player2Radio = new JRadioButton("Joueur 2");
    private ButtonGroup playerGroup = new ButtonGroup();

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(160, 0));

        // Grouper les radios
        playerGroup.add(player1Radio);
        playerGroup.add(player2Radio);
        player1Radio.setSelected(true); // par défaut joueur 1

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
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(mode);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(confirm);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Ajout des radios avec un séparateur
        add(new JLabel("Tour du joueur :"));
        add(player1Radio);
        add(player2Radio);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(CmbAi);
        add(depth);
        add(Box.createRigidArea(new Dimension(0, 500)));
    }

    public void setButtonListener(ActionListener listener) {
        btn0.addActionListener(listener);
        btn1.addActionListener(listener);
        btn2.addActionListener(listener);
        btnLoad.addActionListener(listener);
        btnSave.addActionListener(listener);
        confirm.addActionListener(listener);
    }

    // Ajouter un listener spécifique pour les radios
    public void setRadioListener(ActionListener listener) {
        player1Radio.addActionListener(listener);
        player2Radio.addActionListener(listener);
    }

    // Mise à jour de l'état des radios selon le joueur courant
    public void updateRadioSelection(byte currentPlayer) {
        if (currentPlayer == 1) {
            player1Radio.setSelected(true);
        } else if (currentPlayer == 2) {
            player2Radio.setSelected(true);
        }
    }

    // Activer/désactiver les radios selon le mode de jeu
    public void setRadiosEnabled(boolean enabled) {
        player1Radio.setEnabled(enabled);
        player2Radio.setEnabled(enabled);
    }
}