package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {

    String[] options = {"Random", "MiniMax"};
    Integer[] depthList = {1,2,3,4,5,6,7,8,9,10};

    public JButton btn0 = new JButton("Partie 0 joueurs");
    public JButton btn1 = new JButton("Partie 1 joueurs");
    public JButton btn2 = new JButton("Partie 2 joueurs");
    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");
    public JComboBox<String> CmbAi = new JComboBox<>(options);
    public JComboBox<Integer> depth = new JComboBox<>(depthList);

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(160, 0)); // largeur fixe

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
    }
}