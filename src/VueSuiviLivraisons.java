import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueSuiviLivraisons extends JFrame {

    private JTable tableau;
    private DefaultTableModel model;
    private JButton btnRetour;
    private ActionListener actionClotureListener; // Stocke l'action pour les boutons du tableau

    private static final Color ROUGE = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);

    public VueSuiviLivraisons() {
        setTitle("RaPizz — Tableau de Bord des Livreurs en Route");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BEIGE_FOND);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        add(root);
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ROUGE);
        header.setBorder(new EmptyBorder(25, 40, 25, 40));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel titre = new JLabel("Livreurs en Cours de Route");
        titre.setFont(new Font("SansSerif", Font.BOLD, 45));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Suivi live des temps de livraison (Limite de 30 min) — Cliquez directement sur Clôturer au retour d'un livreur");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        sousTitre.setForeground(new Color(210, 245, 245));

        texts.add(titre);
        texts.add(Box.createVerticalStrut(4));
        texts.add(sousTitre);
        header.add(texts, BorderLayout.WEST);
        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BEIGE_FOND);
        center.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Ajout d'une colonne "Action" à la fin
        String[] colonnes = {"ID Commande", "Client à Livrer", "Pizza", "Véhicule", "Heure de Départ", "Temps Restant / Statut", "Action"};
        
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne du bouton "Action" est cliquable/éditable
            }
        };

        tableau = new JTable(model);
        tableau.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableau.setRowHeight(35); // Légèrement plus haut pour accueillir confortablement le bouton
        
        // Configuration du bouton directement dans la cellule de la colonne 6
        tableau.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableau.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(new LineBorder(new Color(220, 210, 195)));
        center.add(scroll, BorderLayout.CENTER);

        return center;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 14));
        footer.setBackground(new Color(245, 235, 220));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        btnRetour = new JButton("← Retour au menu");
        btnRetour.setBackground(ROUGE);
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRetour.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.add(btnRetour);
        return footer;
    }

    public DefaultTableModel getTableModel() { return model; }
    public JTable getTableau() { return tableau; }
    public void addRetourListener(ActionListener l) { btnRetour.addActionListener(l); }
    
    // Remplacement de l'ancien listener par le nouveau système pour le bouton du tableau
    public void addCloturerDirectListener(ActionListener l) { this.actionClotureListener = l; }
    public ActionListener getActionClotureListener() { return actionClotureListener; }

    // --- INNER CLASSES POUR RENDRE LE BOUTON INTERACTIF DANS LE TABLEAU ---

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(0, 110, 80)); // Vert
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("✓ Clôturer");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(0, 110, 80));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
            
            button.addActionListener(e -> {
                fireEditingStopped(); // Arrête l'édition immédiatement lors du clic
                if (actionClotureListener != null) {
                    // Déclenche l'événement vers le contrôleur
                    actionClotureListener.actionPerformed(e);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("✓ Clôturer");
            return button;
        }
    }
}