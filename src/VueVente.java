import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueVente extends JFrame {
    private JComboBox<String> cbClients;
    private JComboBox<Pizza> cbPizzas;
    private JComboBox<String> cbTailles;
    private JComboBox<String> cbLivreurs;
    private JComboBox<String> cbVehicules;
    private JButton btnValider;
    private JButton btnRetour;

    // Thème couleur assorti à votre VueMenu
    private final Color COLOR_BG = new Color(28, 28, 28);
    private final Color COLOR_PRIMARY = new Color(211, 47, 47);
    private final Color COLOR_ACCENT = new Color(56, 142, 60); // Vert pour valider
    private final Color COLOR_TEXT_LIGHT = new Color(245, 245, 245);

    public VueVente() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("RaPizz - Enregistrer une Vente/Livraison");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TITRE ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titre = new JLabel("Nouvelle Vente", SwingConstants.CENTER);
        titre.setFont(new Font("Montserrat", Font.BOLD, 45));
        titre.setForeground(COLOR_PRIMARY);
        mainPanel.add(titre, gbc);

        // Reset pour les champs
        gbc.gridwidth = 1;

        // --- FORMULAIRE ---
        // Client
        addFormRow(mainPanel, gbc, "Client :", cbClients = new JComboBox<>(), 1);
        // Pizza
        addFormRow(mainPanel, gbc, "Pizza :", cbPizzas = new JComboBox<>(), 2);
        // Taille
        String[] tailles = {"Naine", "Humaine", "Ogresse"};
        addFormRow(mainPanel, gbc, "Taille :", cbTailles = new JComboBox<>(tailles), 3);
        cbTailles.setSelectedItem("Humaine"); // Taille par défaut
        // Livreur
        addFormRow(mainPanel, gbc, "Livreur :", cbLivreurs = new JComboBox<>(), 4);
        // Véhicule
        addFormRow(mainPanel, gbc, "Véhicule :", cbVehicules = new JComboBox<>(), 5);

        // --- BOUTONS ---
        // Bouton Valider
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 10, 15);
        btnValider = new JButton("Enregistrer la commande");
        setupButton(btnValider, COLOR_ACCENT);
        mainPanel.add(btnValider, gbc);

        // Bouton Retour
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 15, 10, 15);
        btnRetour = new JButton("Retour au Menu Principal");
        setupButton(btnRetour, new Color(70, 70, 70));
        mainPanel.add(btnRetour, gbc);

        this.add(mainPanel);
        this.setLocationRelativeTo(null);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridy = row;
        
        // Label (Colonne 0)
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(COLOR_TEXT_LIGHT);
        panel.add(label, gbc);

        // Composant / ComboBox (Colonne 1)
        gbc.gridx = 1;
        component.setPreferredSize(new Dimension(350, 40));
        component.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(component, gbc);
    }

    private void setupButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(300, 50));
    }

    // --- LISTENERS POUR LE CONTROLEUR ---
    public void addValiderListener(ActionListener listener) { btnValider.addActionListener(listener); }
    public void addRetourListener(ActionListener listener) { btnRetour.addActionListener(listener); }

    // --- GETTERS (Pour récupérer les sélections de l'employé) ---
    public JComboBox<String> getCbClients() { return cbClients; }
    public JComboBox<Pizza> getCbPizzas() { return cbPizzas; }
    public JComboBox<String> getCbTailles() { return cbTailles; }
    public JComboBox<String> getCbLivreurs() { return cbLivreurs; }
    public JComboBox<String> getCbVehicules() { return cbVehicules; }
}