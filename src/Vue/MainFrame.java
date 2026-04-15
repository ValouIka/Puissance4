package Vue;

import Modele.AIType;
import Modele.Board;
import Controleur.BoardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainFrame extends JFrame implements ActionListener {

    private SidebarPanel sidebar;
    private JPanel centerPanel;
    private Board currentBoard;
    private BoardDisplay currentDisplay;
    private BoardController currentController; // Nouvelle référence
    private byte currentPlayerNb;

    public MainFrame() {
        setTitle("Puissance 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sidebar = new SidebarPanel();
        sidebar.setButtonListener(this);
        sidebar.setRadioListener(this); // Écoute les radios
        add(sidebar, BorderLayout.WEST);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Cliquez sur un mode de jeu pour commencer", SwingConstants.CENTER), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        setSize(840, 710);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

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
        } else if (source == sidebar.confirm) {
            setPaintMode();
        } if (source == sidebar.player1Radio || source == sidebar.player2Radio) {
            if (currentBoard != null && !currentBoard.paint) {
                byte newPlayer = (source == sidebar.player1Radio) ? (byte)1 : (byte)2;
                if (currentBoard.player != newPlayer) {
                    // Si on est en mode 1 joueur, on change aussi le humanPlayer si l'utilisateur le souhaite
                    if (currentBoard.getPlayersNumber() == 1) {
                        // On considère que le joueur sélectionné devient l'humain
                        currentController.setHumanPlayer(newPlayer);
                        // Si on passe au joueur IA, on déclenche son tour
                        if (currentBoard.player != newPlayer) {
                            currentBoard.player = newPlayer;
                            updateRadiosFromBoard();
                            currentDisplay.repaint();
                            if (currentBoard.player != currentController.humanPlayer) {
                                currentController.jouerTourIA();
                            }
                        }
                    } else {
                        // Mode 2 joueurs ou peinture : simple changement de joueur
                        currentBoard.player = newPlayer;
                        updateRadiosFromBoard();
                        currentDisplay.repaint();
                    }
                }
            }
        }
    }

    private void demarrerPartie(byte nbJoueurs) {
        String selectedAi = (String) sidebar.CmbAi.getSelectedItem();
        AIType aiType = "MiniMax".equals(selectedAi) ? AIType.MINIMAX : AIType.RANDOM;
        int depth = (Integer) sidebar.depth.getSelectedItem();

        Board board = new Board(9, 9);
        board.setPlayersNumber(nbJoueurs);
        currentPlayerNb = nbJoueurs;

        BoardDisplay display = new BoardDisplay(board);
        centerPanel.removeAll();
        centerPanel.add(display, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();

        BoardController controller = new BoardController(board, display, aiType, depth, this);
        currentBoard = board;
        currentDisplay = display;
        currentController = controller;

        // Activer les radios seulement si le mode le permet (2 joueurs ou peinture)
        boolean radiosEnabled = (nbJoueurs == 2) || board.paint || (nbJoueurs == 1);
        sidebar.setRadiosEnabled(radiosEnabled);
        updateRadiosFromBoard();
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
        String selectedAi = (String) sidebar.CmbAi.getSelectedItem();
        AIType aiType = "MiniMax".equals(selectedAi) ? AIType.MINIMAX : AIType.RANDOM;
        int depth = (Integer) sidebar.depth.getSelectedItem();

        Board board = new Board(9, 9);
        try (DataInputStream load = new DataInputStream(new FileInputStream("save.dat"))) {
            for (int r = 0; r < board.row; r++) {
                for (int c = 0; c < board.col; c++) {
                    board.board[r][c] = load.readByte();
                }
            }
            board.setPlayersNumber(currentPlayerNb);
            // Le joueur courant n'est pas sauvegardé dans le fichier, on le met à 1 par défaut
            board.player = 1;

            BoardDisplay display = new BoardDisplay(board);
            centerPanel.removeAll();
            centerPanel.add(display, BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();

            BoardController controller = new BoardController(board, display, aiType, depth, this);
            currentBoard = board;
            currentDisplay = display;
            currentController = controller;

            boolean radiosEnabled = (currentPlayerNb == 2) || board.paint || (currentPlayerNb == 1);
            sidebar.setRadiosEnabled(radiosEnabled);
            updateRadiosFromBoard();

            JOptionPane.showMessageDialog(this, "Partie chargée.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Aucune sauvegarde trouvée.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
        }
    }

    private void setPaintMode() {
        if (currentBoard != null) {
            String selectedPaintMode = (String) sidebar.mode.getSelectedItem();
            currentBoard.paint = "Paint".equals(selectedPaintMode);
            // Activer/désactiver les radios en mode peinture
            boolean radiosEnabled = (currentBoard.getPlayersNumber() == 2) || currentBoard.paint || (currentBoard.getPlayersNumber() == 1);
            sidebar.setRadiosEnabled(radiosEnabled);
            updateRadiosFromBoard();
        }
    }

    // Méthode publique pour mettre à jour les radios depuis le Board (appelée par le contrôleur)
    public void updateRadiosFromBoard() {
        if (currentBoard != null) {
            sidebar.updateRadioSelection(currentBoard.player);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}