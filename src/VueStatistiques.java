import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VueStatistiques extends JFrame {

    private static final Color ROUGE = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(22, 22, 24);
    private static final Color SURFACE = new Color(31, 31, 35);
    private static final Color SURFACE_ALT = new Color(39, 39, 44);
    private static final Color SURFACE_SOFT = new Color(45, 45, 52);
    private static final Color VERT = new Color(0, 160, 110);
    private static final Color TEXTE = new Color(245, 245, 245);
    private static final Color TEXTE_MUTED = new Color(175, 175, 180);
    private static final Color BORDER = new Color(62, 62, 70);
    private final Statistiques stats;

    private JButton btnRetour;
    private JPanel contentCenter; // conteneur principal centré interchangeable
    private JButton btnRetourCategories;
    private String selectedCategory = null;

    public VueStatistiques(Statistiques stats) {
        this.stats = stats;
        setTitle("RaPizz — Statistiques");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
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
        setLocationRelativeTo(null);
        setVisible(true);

        // Diagnostics de visibilité pour debug
        System.out.println("[VueStatistiques] visible=" + isVisible() + ", showing=" + isShowing() + ", bounds=" + getBounds());

        // Si la taille est nulle (par ex. problème multi-écran), forcer une taille par défaut
        if (getWidth() <= 0 || getHeight() <= 0) {
            System.out.println("[VueStatistiques] taille invalide détectée, application d'une taille de secours.");
            setSize(1200, 800);
            setLocationRelativeTo(null);
        }

        // Si la fenêtre est positionnée hors écran (coordonnées négatives), recentrer sur l'écran principal
        if (getX() < 0 || getY() < 0) {
            Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            int cx = screen.x + Math.max(0, (screen.width - getWidth()) / 2);
            int cy = screen.y + Math.max(0, (screen.height - getHeight()) / 2);
            System.out.println("[VueStatistiques] fenêtre hors écran détectée, recentrage vers: " + cx + "," + cy);
            setLocation(cx, cy);
        }

        // S'assurer que la fenêtre apparaît devant les autres
        toFront();
        requestFocus();
        setAlwaysOnTop(true);
        setAlwaysOnTop(false);

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

        JLabel valeur = new JLabel(stats.chiffreAffairesTotal);
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
        badge.setBackground(new Color(80, 80, 92));
        badge.setForeground(Color.WHITE);
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
        // recréer les boutons
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
            cards.add(buildInsightCard("Meilleur client", stats.meilleurClient, "", "Client avec le plus de commandes."));
            cards.add(buildInsightCard("Moyenne commandes", lireStatistique(() -> new StatistiquesDAO().lireMoyenneCommandes(), "0,00"), "", "Moyenne de commandes par client."));
            cards.add(buildInsightCard("Clients au-dessus de la moyenne", lireStatistique(() -> new StatistiquesDAO().lireNombreClientsAuDessusMoyenne(), "0"), "", "Nombre de clients ayant dépassé la moyenne."));
            cards.add(buildInsightCard("Client avec meilleur CA", premierClientCA(), "", "Client générant le plus de chiffre d'affaires."));
        } else if ("Livraisons".equals(category)) {
            cards.add(buildInsightCard("Total des commandes", stats.totalCommandes, "", "Nombre total de livraisons enregistrées."));
            cards.add(buildInsightCard("Délai moyen", stats.delaiMoyen, "", "Temps moyen entre commande et livraison."));
            cards.add(buildInsightCard("Livraison la plus rapide", stats.livraisonPlusRapide, "", "Livraison la plus courte relevée."));
            cards.add(buildInsightCard("Jour le plus chargé", lireStatistique(() -> new StatistiquesDAO().lireJourPlusLivraisons(), "N/A"), "", "Date avec le plus de livraisons."));
        } else if ("Livreurs".equals(category)) {
            cards.add(buildInsightCard("Meilleur livreur", stats.meilleurLivreur, "", "Livreur avec le plus de livraisons."));
            cards.add(buildInsightCard("Pire livreur", premierPireLivreurNom(), premierPireLivreurRetards(), "Nombre de retards sur l'ensemble des livraisons."));
            cards.add(buildInsightCard("Véhicule le plus utilisé", stats.vehiculePlusUtilise, "", "Véhicule le plus sollicité."));
            cards.add(buildInsightCard("Véhicules jamais utilisés", lireStatistique(() -> new StatistiquesDAO().lireNombreVehiculesJamaisUtilises(), "0"), "", "Nombre de véhicules sans livraison."));
        } else {
            cards.add(buildInsightCard("Pizza star", stats.pizzaStar, "", "Pizza la plus commandée."));
            cards.add(buildInsightCard("Pizza la moins commandée", lireStatistique(() -> new StatistiquesDAO().lirePizzaMoinsCommandee(), "N/A"), "", "Pizza la moins servie."));
            cards.add(buildInsightCard("Ingrédient favori", stats.ingredientFavori, "", "Ingrédient le plus utilisé."));
            cards.add(buildInsightCard("Pizzas au menu", lireStatistique(() -> Integer.toString(new StatistiquesDAO().getMenu().size()), "0"), "", "Nombre de pizzas disponibles à la carte."));
        }

        JScrollPane scroll = new JScrollPane(cards);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(BEIGE_FOND);
        return scroll;
    }

    private String premierClientCA() {
        try {
            java.util.List<String[]> rows = new StatistiquesDAO().getChiffreAffaireParClient();
            if (!rows.isEmpty() && rows.get(0).length >= 3) {
                String[] row = rows.get(0);
                return row[0] + " " + row[1] + " — " + row[2];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "N/A";
    }

    private String premierPireLivreurNom() {
        try {
            java.util.List<String[]> rows = new StatistiquesDAO().getPireLivreur();
            if (!rows.isEmpty() && rows.get(0).length >= 3) {
                String[] row = rows.get(0);
                return row[0] + " " + row[1];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "N/A";
    }

    private String premierPireLivreurRetards() {
        try {
            java.util.List<String[]> rows = new StatistiquesDAO().getPireLivreur();
            if (!rows.isEmpty() && rows.get(0).length >= 3) {
                return rows.get(0)[2] + " retards";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0 retards";
    }

    private String lireStatistique(StatistiqueFournisseur fournisseur, String defaut) {
        try {
            return fournisseur.lire();
        } catch (Exception ex) {
            ex.printStackTrace();
            return defaut;
        }
    }

    private interface StatistiqueFournisseur {
        String lire() throws Exception;
    }

    // extrait la ligne des KPI pour réutilisation dans showCardsView
    private JPanel buildKpiRow() {
        JPanel kpis = new JPanel(new GridLayout(1, 3, 18, 18));
        kpis.setOpaque(false);
        kpis.add(buildKpiCard("Chiffre d'affaires", stats.chiffreAffairesTotal, "Total encaissé sur toutes les livraisons", VERT));
        kpis.add(buildKpiCard("Commandes", stats.totalCommandes, "Nombre total de livraisons enregistrées", ROUGE));
        kpis.add(buildKpiCard("Délai moyen", stats.delaiMoyen, "Temps moyen observé entre commande et livraison", new Color(35, 90, 160)));
        return kpis;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 24, 28));
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel titre = new JLabel("Statistiques de la pizzeria");
        titre.setFont(new Font("SansSerif", Font.BOLD, 40));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Synthèse des indicateurs clés issus de la base de données");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        sousTitre.setForeground(TEXTE_MUTED);

        texts.add(titre);
        texts.add(sousTitre);

        header.add(texts, BorderLayout.WEST);
        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setBackground(BEIGE_FOND);
        center.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel kpis = new JPanel(new GridLayout(1, 3, 18, 18));
        kpis.setOpaque(false);
       kpis.add(buildKpiCard("Chiffre d'affaires", stats.chiffreAffairesTotal, "Total encaissé sur toutes les livraisons", VERT));
        kpis.add(buildKpiCard("Commandes", stats.totalCommandes, "Nombre total de livraisons enregistrées", ROUGE));
        kpis.add(buildKpiCard("Délai moyen", stats.delaiMoyen, "Temps moyen observé entre commande et livraison", new Color(35, 90, 160)));

        JPanel cards = new JPanel(new GridLayout(2, 3, 18, 18));
        cards.setOpaque(false);
        cards.add(buildInsightCard("Meilleur client", stats.meilleurClient, "", "Client le plus fidèle selon la requête statistique."));
        cards.add(buildInsightCard("Meilleur livreur", stats.meilleurLivreur, "", "Livreur ayant réalisé le plus de livraisons."));
        cards.add(buildInsightCard("Véhicule le plus utilisé", stats.vehiculePlusUtilise, "", "Véhicule associé au plus grand nombre de livraisons."));
        cards.add(buildInsightCard("Pizza star", stats.pizzaStar, "", "Pizza la plus commandée sur la période."));
        cards.add(buildInsightCard("Livraison la plus rapide", stats.livraisonPlusRapide, "", "Livraison la plus courte relevée dans l'historique."));
        cards.add(buildInsightCard("Ingrédient favori", stats.ingredientFavori, "", "Ingrédient qui revient le plus souvent dans les recettes."));

        JScrollPane scroll = new JScrollPane(cards);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BEIGE_FOND);
        scroll.getViewport().setBackground(BEIGE_FOND);

        center.add(kpis, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        return center;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(new Color(24, 24, 28));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));

        btnRetour = new JButton("← Retour au menu");
        stylePrimaryButton(btnRetour, ROUGE);

        footer.add(btnRetour);
        return footer;
    }

    private JPanel buildKpiCard(String titre, String valeur, String description, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTitre = new JLabel(titre.toUpperCase());
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTitre.setForeground(TEXTE_MUTED);
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblValeur.setForeground(accent);
        lblValeur.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><body style='width: 100%'>" + description + "</body></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDesc.setForeground(TEXTE);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitre);
        card.add(Box.createVerticalStrut(8));
        card.add(lblValeur);
        card.add(Box.createVerticalStrut(6));
        card.add(lblDesc);
        return card;
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
        button.setBackground(accent.darker());
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