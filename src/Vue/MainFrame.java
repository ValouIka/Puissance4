package Vue;

import Modele.Board;
import Controleur.BoardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainFrame extends JFrame implements ActionListener {

    private SidebarPanel sidebar;
    private JPanel centerPanel;          // contiendra le BoardDisplay
    private Board currentBoard;           // plateau actuellement affiché
    private BoardDisplay currentDisplay;   // vue actuelle

    public MainFrame() {
        setTitle("Puissance 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Création du menu latéral
        sidebar = new SidebarPanel();
        sidebar.setButtonListener(this);   // cette classe écoute les boutons
        add(sidebar, BorderLayout.WEST);

        // Panneau central (vide au démarrage)
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Cliquez sur un mode de jeu pour commencer", SwingConstants.CENTER), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        setSize(840, 710);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (source == sidebar.btn0) {
            demarrerPartie((byte) 0);
        } else if (source == sidebar.btn1) {
            demarrerPartie((byte) 1);
        } else if (source == sidebar.btn2) {
            demarrerPartie((byte) 2);
        } else if (source == sidebar.btnLoad) {
            chargerPartie();
        } else if (source == sidebar.btnSave) {
            sauvegarderPartie();
        }
    }

    private void demarrerPartie(byte nbJoueurs) {
        // Créer un nouveau plateau
        Board board = new Board(9,9);
        board.setPlayersNumber(nbJoueurs);

        // Créer la vue associée
        BoardDisplay display = new BoardDisplay(board);

        // Remplacer l'ancien contenu du centre par le nouveau plateau
        centerPanel.removeAll();
        centerPanel.add(display, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();

        // Attacher le contrôleur de souris
        new BoardController(board, display);

        // Garder une référence pour la sauvegarde/chargement
        currentBoard = board;
        currentDisplay = display;
    }

    private void sauvegarderPartie() {
        if (currentBoard == null) {
            JOptionPane.showMessageDialog(this, "Aucune partie en cours !");
            return;
        }

        try (DataOutputStream save = new DataOutputStream(new FileOutputStream("save.dat"))) {
            for (byte[] row : currentBoard.board) {
                for (byte val : row) {
                    save.writeByte(val);
                }
            }
            JOptionPane.showMessageDialog(this, "Partie sauvegardée.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage());
        }
    }

    private void chargerPartie() {
        // On crée un nouveau plateau pour éviter de mélanger avec l'ancien
        Board board = new Board(9,9);
        try (DataInputStream load = new DataInputStream(new FileInputStream("save.dat"))) {
            for (int r = 0; r < board.row; r++) {
                for (int c = 0; c < board.col; c++) {
                    board.board[r][c] = load.readByte();
                }
            }
            // Important : re-créer l'affichage avec ce plateau chargé
            BoardDisplay display = new BoardDisplay(board);

            centerPanel.removeAll();
            centerPanel.add(display, BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();

            new BoardController(board, display);

            currentBoard = board;
            currentDisplay = display;

            JOptionPane.showMessageDialog(this, "Partie chargée.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Aucune sauvegarde trouvée.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}