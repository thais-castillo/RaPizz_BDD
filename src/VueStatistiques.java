import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VueStatistiques extends JFrame {

    private static final Color ROUGE = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);
    private static final Color VERT = new Color(0, 110, 80);
    private static final Color TEXTE = new Color(45, 35, 25);
    private static final Color TEXTE_MUTED = new Color(110, 100, 90);

    private JButton btnRetour;

    public VueStatistiques() {
        setTitle("RaPizz — Statistiques");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BEIGE_FOND);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
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

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setBackground(BEIGE_FOND);
        center.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel kpis = new JPanel(new GridLayout(1, 3, 18, 18));
        kpis.setOpaque(false);
        kpis.add(buildKpiCard("Chiffre d'affaires", "2 480,50 €", "Total encaissé sur toutes les livraisons", VERT));
        kpis.add(buildKpiCard("Commandes", "156", "Nombre total de livraisons enregistrées", ROUGE));
        kpis.add(buildKpiCard("Délai moyen", "28 min", "Temps moyen observé entre commande et livraison", new Color(35, 90, 160)));

        JPanel cards = new JPanel(new GridLayout(2, 3, 18, 18));
        cards.setOpaque(false);
        cards.add(buildInsightCard("Meilleur client", "Marie Dupont", "24 commandes", "Client le plus fidèle selon la requête statistique."));
        cards.add(buildInsightCard("Meilleur livreur", "Yanis Martin", "38 livraisons", "Livreur ayant réalisé le plus de livraisons."));
        cards.add(buildInsightCard("Véhicule le plus utilisé", "Scooter S-204", "18 trajets", "Véhicule associé au plus grand nombre de livraisons."));
        cards.add(buildInsightCard("Pizza star", "Margherita", "41 commandes", "Pizza la plus commandée sur la période."));
        cards.add(buildInsightCard("Livraison la plus rapide", "14 min", "Client + centre-ville", "Livraison la plus courte relevée dans l'historique."));
        cards.add(buildInsightCard("Ingrédient favori", "Mozzarella", "72 utilisations", "Ingrédient qui revient le plus souvent dans les recettes."));

        JScrollPane scroll = new JScrollPane(cards);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BEIGE_FOND);

        center.add(kpis, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        return center;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(new Color(245, 235, 220));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        btnRetour = new JButton("← Retour au menu");
        btnRetour.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRetour.setBackground(ROUGE);
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setBorderPainted(false);
        btnRetour.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(btnRetour);
        return footer;
    }

    private JPanel buildKpiCard(String titre, String valeur, String description, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
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
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
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

    public void addRetourListener(java.awt.event.ActionListener listener) {
        btnRetour.addActionListener(listener);
    }
}