package Vue;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import DAO.StatistiquesDAO;
import Model.Statistiques;
import java.awt.*;

public class VueStatistiques extends JFrame {

    // Palette de couleurs unifiée harmonisée avec le reste de l'appli (Thème Clair)
    private static final Color ROUGE = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235); // Fond clair de l'application
    private static final Color SURFACE = Color.WHITE;                  // Cartes blanches épurées
    private static final Color SURFACE_ALT = Color.WHITE;              // Cartes blanches épurées
    private static final Color SURFACE_SOFT = new Color(245, 235, 220);// Boutons secondaires
    private static final Color VERT = new Color(0, 140, 90);           // Vert lisible sur fond blanc
    private static final Color TEXTE = new Color(45, 35, 25);          // Texte principal sombre
    private static final Color TEXTE_MUTED = new Color(110, 100, 90);  // Texte secondaire atténué
    private static final Color BORDER = new Color(220, 210, 195);      // Bordures légères de séparation
    private final Statistiques stats;

    private JButton btnRetour;
    private JPanel contentCenter; // conteneur principal centré interchangeable
    private JButton btnRetourCategories;
    private String selectedCategory = null;

    public VueStatistiques(Statistiques stats) {
        this.stats = stats;
        setTitle("RaPizz — Statistiques");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Boutons de sélection affichés au centre (écran d'entrée)
        JButton btnClients = new JButton("Clients");
        JButton btnLivraisons = new JButton("Livraisons");
        JButton btnLivreurs = new JButton("Livreurs");
        JButton btnCommandes = new JButton("Commandes");

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BEIGE_FOND);

        root.add(buildHeader(), BorderLayout.NORTH);
        // centre interchangeable : d'abord la grille de boutons, puis les cartes
        contentCenter = new JPanel(new BorderLayout());
        contentCenter.setBackground(BEIGE_FOND);
        contentCenter.setBorder(new EmptyBorder(24, 24, 24, 24));
        contentCenter.add(buildCategorySelectionPanel(btnClients, btnLivraisons, btnLivreurs, btnCommandes), BorderLayout.CENTER);
        root.add(contentCenter, BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        add(root);
        
        // Ajustements pour l'affichage plein écran stable
        setSize(1280, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        root.revalidate();
        root.repaint();
        setVisible(true);

        // S'assurer que la fenêtre apparaît devant les autres
        toFront();
        requestFocus();

        // Listeners : afficher les cartes/statistiques dans le même affichage
        btnClients.addActionListener(e -> showCategoryView("Clients"));
        btnLivraisons.addActionListener(e -> showCategoryView("Livraisons"));
        btnLivreurs.addActionListener(e -> showCategoryView("Livreurs"));
        btnCommandes.addActionListener(e -> showCategoryView("Commandes"));
    }

    private JPanel buildCategorySelectionPanel(JButton c, JButton l, JButton lv, JButton cmd) {
        JPanel outer = new JPanel(new BorderLayout(0, 18));
        outer.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(buildSelectionInfoCard(), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 18, 18));
        buttons.setOpaque(false);

        Dimension bsize = new Dimension(280, 92);
        styleCategoryButton(c, ROUGE, bsize);
        styleCategoryButton(l, new Color(35, 90, 160), bsize);
        styleCategoryButton(lv, VERT, bsize);
        styleCategoryButton(cmd, new Color(120, 80, 200), bsize);

        buttons.add(c);
        buttons.add(l);
        buttons.add(lv);
        buttons.add(cmd);

        JPanel middle = new JPanel(new BorderLayout(0, 14));
        middle.setOpaque(false);
        JLabel title = new JLabel("Choisis une famille de statistiques");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXTE);
        JLabel subtitle = new JLabel("Les cartes suivantes regroupent les requêtes du script par catégorie.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXTE_MUTED);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(title);
        text.add(subtitle);
        middle.add(text, BorderLayout.NORTH);
        middle.add(buttons, BorderLayout.CENTER);

        outer.add(topRow, BorderLayout.NORTH);
        outer.add(middle, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildSelectionInfoCard() {
        JPanel card = new JPanel();
        card.setBackground(SURFACE_ALT);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(18, 22, 18, 22)
        ));
        card.setLayout(new BorderLayout(18, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("Chiffre d'affaires");
        titre.setFont(new Font("SansSerif", Font.BOLD, 13));
        titre.setForeground(TEXTE);

        JLabel valeur = new JLabel(stats.getChiffreAffairesTotal());
        valeur.setFont(new Font("SansSerif", Font.BOLD, 34));
        valeur.setForeground(VERT);

        JLabel desc = new JLabel("Montant net encaissé sur l'ensemble des livraisons");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(TEXTE_MUTED);

        left.add(titre);
        left.add(Box.createVerticalStrut(4));
        left.add(valeur);
        left.add(Box.createVerticalStrut(4));
        left.add(desc);

        JLabel badge = new JLabel("STAT");
        badge.setOpaque(true);
        badge.setBackground(new Color(210, 200, 185));
        badge.setForeground(TEXTE);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(new EmptyBorder(8, 12, 8, 12));

        card.add(left, BorderLayout.CENTER);
        card.add(badge, BorderLayout.EAST);
        return card;
    }

    private void showCategoryView(String category) {
        this.selectedCategory = category;
        contentCenter.removeAll();

        JPanel top = new JPanel(new BorderLayout(12, 12));
        top.setOpaque(false);
        btnRetourCategories = new JButton("← Choix catégories");
        styleSecondaryButton(btnRetourCategories);
        btnRetourCategories.addActionListener(e -> showCategorySelection());
        top.add(btnRetourCategories, BorderLayout.WEST);
        top.add(buildCategoryTitle(category), BorderLayout.CENTER);
        contentCenter.add(top, BorderLayout.NORTH);
        contentCenter.add(buildCategoryCards(category), BorderLayout.CENTER);
        contentCenter.revalidate();
        contentCenter.repaint();
    }

    private void showCategorySelection() {
        selectedCategory = null;
        contentCenter.removeAll();
        JButton btnClients = new JButton("Clients");
        JButton btnLivraisons = new JButton("Livraisons");
        JButton btnLivreurs = new JButton("Livreurs");
        JButton btnCommandes = new JButton("Commandes");
        btnClients.addActionListener(e -> showCategoryView("Clients"));
        btnLivraisons.addActionListener(e -> showCategoryView("Livraisons"));
        btnLivreurs.addActionListener(e -> showCategoryView("Livreurs"));
        btnCommandes.addActionListener(e -> showCategoryView("Commandes"));
        contentCenter.add(buildCategorySelectionPanel(btnClients, btnLivraisons, btnLivreurs, btnCommandes), BorderLayout.CENTER);
        contentCenter.revalidate();
        contentCenter.repaint();
    }

    private JPanel buildCategoryTitle(String category) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(category);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(TEXTE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Cartes de synthèse liées à la catégorie sélectionnée");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXTE_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(subtitle);
        return panel;
    }

    private JComponent buildCategoryCards(String category) {
        JPanel cards = new JPanel(new GridLayout(2, 2, 18, 18));
        cards.setOpaque(false);

        if ("Clients".equals(category)) {
            JComponent clientsSection = buildClientsSection();
            return wrapScrollable(clientsSection);
        } else if ("Livraisons".equals(category)) {
            cards.add(buildInsightCard("Total des commandes", stats.getTotalCommandes(), "", "Nombre total de livraisons enregistrées."));
            cards.add(buildInsightCard("Délai moyen", stats.getDelaiMoyen(), "", "Temps moyen entre commande et livraison."));
            cards.add(buildInsightCard("Livraison la plus rapide", stats.getLivraisonPlusRapide(), "", "Livraison la plus courte relevée."));
            cards.add(buildInsightCard("Jour le plus chargé", stats.getJourPlusCharge(), "", "Date avec le plus de livraisons."));
        } else if ("Livreurs".equals(category)) {
            cards.add(buildInsightCard("Meilleur livreur", stats.getMeilleurLivreur(), "", "Livreur avec le plus de livraisons."));
            cards.add(buildInsightCard("Pire livreur", premierPireLivreurNom(), premierPireLivreurRetards(), "Nombre de retards sur l'ensemble des livraisons."));
            cards.add(buildInsightCard("Véhicule le plus utilisé", stats.getVehiculePlusUtilise(), "", "Véhicule le plus sollicité."));
            cards.add(buildInsightCard("Véhicules jamais utilisés", stats.getVehiculesJamaisUtilises(), "", "Nombre de véhicules sans livraison."));
        } else {
            cards.add(buildInsightCard("Pizza star", stats.getPizzaStar(), "", "Pizza la plus commandée."));
            cards.add(buildInsightCard("Pizza la moins commandée", stats.getPizzaMoinsCommandee(), "", "Pizza la moins servie."));
            cards.add(buildInsightCard("Ingrédient favori", stats.getIngredientFavori(), "", "Ingrédient le plus utilisé."));
            cards.add(buildInsightCard("Pizzas au menu", stats.getNombrePizzasMenu(), "", "Nombre de pizzas disponibles à la carte."));
        }

        return wrapScrollable(cards);
    }

    private JComponent buildClientsSection() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // 1. Grille supérieure contenant les 4 cartes
        JPanel cards = new JPanel(new GridLayout(2, 2, 18, 18));
        cards.setOpaque(false);
        cards.add(buildInsightCard("Meilleur client", stats.getMeilleurClient(), "", "Client avec le plus de commandes."));
        cards.add(buildInsightCard("Moyenne commandes", stats.getMoyenneCommandes(), "", "Moyenne de commandes par client."));
        cards.add(buildInsightCard("Clients au-dessus de la moyenne", stats.getClientsAuDessusMoyenne(), "", "Nombre de clients ayant dépassé la moyenne."));
        cards.add(buildInsightCard("Client avec meilleur CA", premierClientCA(), "", "Client générant le plus de chiffre d'affaires."));

        container.add(cards);
        container.add(Box.createVerticalStrut(24)); // Un peu plus d'espace sous les cartes

        // 2. Zone des titres textuels
        JLabel titre = new JLabel("Commandes par client");
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        titre.setForeground(TEXTE);
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sous = new JLabel("Nombre total de commandes par client");
        sous.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sous.setForeground(TEXTE_MUTED);
        sous.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(titre);
        container.add(Box.createVerticalStrut(4));
        container.add(sous);
        container.add(Box.createVerticalStrut(14));

        // 3. CORRECTION : Encapsulation dans un wrapper en BorderLayout
        // Cela force le JScrollPane à se déployer sur toute la largeur disponible
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] cols = {"Prénom", "Nom", "#Commandes"};
        java.util.List<String[]> rows = lireClientsCommandes();
        JScrollPane tablePanel = buildTablePanel(cols, rows);
        
        tableWrapper.add(tablePanel, BorderLayout.CENTER);
        container.add(tableWrapper);

        return container;
    }

    private java.util.List<String[]> lireClientsCommandes() {
        try {
            return new StatistiquesDAO().getChiffreAffaireParClient();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private JScrollPane buildTablePanel(String[] columns, java.util.List<String[]> rows) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (rows != null) {
            for (String[] r : rows) {
                model.addRow(r);
            }
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setBackground(SURFACE_ALT);
        table.setForeground(TEXTE);
        table.setGridColor(BORDER);
        table.setSelectionBackground(SURFACE_SOFT);
        table.setSelectionForeground(TEXTE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setBackground(SURFACE);
        header.setForeground(TEXTE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(8, 8, 8, 8)
        ));
        scroll.setBackground(BEIGE_FOND);
        scroll.getViewport().setBackground(SURFACE_ALT);
        
        scroll.setPreferredSize(new Dimension(scroll.getPreferredSize().width, 400));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        return scroll;
    }

    private JScrollPane wrapScrollable(JComponent content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(BEIGE_FOND);
        return scroll;
    }

    private String premierClientCA() {
        return stats.getClientMeilleurCA();
    }

    private String premierPireLivreurNom() {
        return stats.getPireLivreurNom();
    }

    private String premierPireLivreurRetards() {
        return stats.getPireLivreurRetards();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ROUGE);
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel titre = new JLabel("Statistiques de la pizzeria");
        titre.setFont(new Font("SansSerif", Font.BOLD, 40));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Synthèse des indicateurs clés issus de la base de données");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        sousTitre.setForeground(new Color(255, 230, 220));

        texts.add(titre);
        texts.add(sousTitre);

        header.add(texts, BorderLayout.WEST);
        return header;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(new Color(245, 235, 220));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));

        btnRetour = new JButton("← Retour au menu");
        stylePrimaryButton(btnRetour, ROUGE);

        footer.add(btnRetour);
        return footer;
    }

    private JPanel buildInsightCard(String titre, String valeur, String sousValeur, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SURFACE_ALT);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitre.setForeground(TEXTE);
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblValeur.setForeground(ROUGE);
        lblValeur.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSousValeur = new JLabel(sousValeur);
        lblSousValeur.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSousValeur.setForeground(VERT);
        lblSousValeur.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><body style='width: 100%'>" + description + "</body></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDesc.setForeground(TEXTE_MUTED);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitre);
        card.add(Box.createVerticalStrut(8));
        card.add(lblValeur);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSousValeur);
        card.add(Box.createVerticalStrut(10));
        card.add(lblDesc);
        return card;
    }

    private void stylePrimaryButton(JButton button, Color baseColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(SURFACE_SOFT);
        button.setForeground(TEXTE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    private void styleCategoryButton(JButton button, Color accent, Dimension size) {
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(accent);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }

    public void addRetourListener(java.awt.event.ActionListener listener) {
        btnRetour.addActionListener(listener);
    }
}