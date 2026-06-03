package Vue;
import javax.swing.*;
import javax.swing.border.*;

import Model.Client;
import Model.Livreur;
import Model.Pizza;
import Model.Vehicule;

import java.awt.*;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class VueVente extends JFrame {
    private JComboBox<Client> cbClients;
    private JComboBox<Pizza> cbPizzas;
    private JComboBox<String> cbTailles;
    private JComboBox<Livreur> cbLivreurs;
    private JComboBox<Vehicule> cbVehicules;
    private JButton btnValider;
    private JButton btnRetour;
    private JButton btnCatalogue;

    private static final Color ROUGE = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);
    private static final Color VERT = new Color(0, 110, 80);
    private static final Color TEXTE = new Color(45, 35, 25);
    private static final Color TEXTE_MUTED = new Color(110, 100, 90);

    public VueVente() {
        setTitle("RaPizz — Prise de Commande");
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

        JLabel titre = new JLabel("Nouvelle Vente");
        titre.setFont(new Font("SansSerif", Font.BOLD, 45));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Saisie des détails de la pizza commandée par le client abonné");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        sousTitre.setForeground(new Color(255, 230, 220));

        texts.add(titre);
        texts.add(Box.createVerticalStrut(4));
        texts.add(sousTitre);

        header.add(texts, BorderLayout.WEST);

        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BEIGE_FOND);
        center.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Formulaire encapsulé dans une belle carte blanche
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(35, 45, 35, 45)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Instanciation des composants
        cbClients = new JComboBox<>();
        cbPizzas = new JComboBox<>();
        cbTailles = new JComboBox<>(new String[]{"Naine", "Humaine", "Ogresse"});
        cbTailles.setSelectedItem("Humaine");
        cbLivreurs = new JComboBox<>();
        cbVehicules = new JComboBox<>();

        // Ajout des lignes du formulaire
        addFormRow(formCard, gbc, "Sélectionner le Client :", cbClients, 0);
        addFormRow(formCard, gbc, "Pizza demandée :", cbPizzas, 1);
        addFormRow(formCard, gbc, "Format de taille :", cbTailles, 2);
        addFormRow(formCard, gbc, "Livreur désigné :", cbLivreurs, 3);
        addFormRow(formCard, gbc, "Véhicule utilisé :", cbVehicules, 4);

        // Bouton de validation de commande intégré directement au bas de la carte
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 15, 0, 15);
        btnValider = new JButton("Valider et enregistrer la livraison");
        btnValider.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnValider.setBackground(VERT);
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);
        btnValider.setBorderPainted(false);
        btnValider.setPreferredSize(new Dimension(300, 45));
        btnValider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formCard.add(btnValider, gbc);

        center.add(formCard);

        // Bouton catalogue place sous la carte formulaire
        GridBagConstraints gbcCat = new GridBagConstraints();
        gbcCat.gridy = 1;
        gbcCat.insets = new Insets(18, 0, 0, 0);

        btnCatalogue = new JButton("Consulter le catalogue de la pizzeria");
        btnCatalogue.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCatalogue.setBackground(Color.WHITE);
        btnCatalogue.setForeground(ROUGE);
        btnCatalogue.setFocusPainted(false);
        btnCatalogue.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCatalogue.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ROUGE, 1, true),
            new EmptyBorder(10, 24, 10, 24)
        ));
        btnCatalogue.addActionListener(e -> ouvrirCataloguePDF());
        center.add(btnCatalogue, gbcCat);

        return center;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComboBox<?> comboBox, int row) {
        gbc.gridy = row;
        gbc.gridwidth = 1;
        
        // Label (Gauche)
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXTE);
        panel.add(label, gbc);

        // ComboBox (Droite)
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboBox.setPreferredSize(new Dimension(350, 38));
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        panel.add(comboBox, gbc);
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 14));
        footer.setBackground(new Color(245, 235, 220));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        btnRetour = new JButton("← Annuler et retour menu");
        btnRetour.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRetour.setBackground(ROUGE);
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setBorderPainted(false);
        btnRetour.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(btnRetour);
        return footer;
    }

    public void addValiderListener(ActionListener listener) { btnValider.addActionListener(listener); }
    public void addRetourListener(ActionListener listener) { btnRetour.addActionListener(listener); }

    /**
     * Ouvre le catalogue PDF de la pizzeria avec le lecteur PDF par défaut du système.
     */
    private void ouvrirCataloguePDF() {
        File pdf = new File("src/assets/catalogue_rapizz.pdf");
        if (!pdf.exists()) {
            JOptionPane.showMessageDialog(this,
                "Le fichier catalogue est introuvable.\n(" + pdf.getAbsolutePath() + ")",
                "Catalogue non trouvé",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            JOptionPane.showMessageDialog(this,
                "Impossible d'ouvrir le PDF automatiquement sur ce système.",
                "Non supporté",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Desktop.getDesktop().open(pdf);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'ouverture du catalogue : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public JComboBox<Client> getCbClients() { return cbClients; }
    public JComboBox<Pizza> getCbPizzas() { return cbPizzas; }
    public JComboBox<String> getCbTailles() { return cbTailles; }
    public JComboBox<Livreur> getCbLivreurs() { return cbLivreurs; }
    public JComboBox<Vehicule> getCbVehicules() { return cbVehicules; }
}