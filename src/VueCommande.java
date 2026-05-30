import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VueCommande extends JDialog {

    // ══════════════════════════════════════════════════════════
    //  TAILLES — multiplicateurs de prix selon le schéma BDD
    //  (taille IN ('Naine', 'Humaine', 'Hogresse'))
    // ══════════════════════════════════════════════════════════
    private static final String[] TAILLES      = { "Naine", "Humaine", "Hogresse" };
    private static final double[] MULTIPLICATEURS = { 0.8,    1.0,      1.3       };
    private static final String[] DESCRIPTIONS = {
        "Petit format — parfait pour une entrée",
        "Taille classique — idéale pour 1 personne",
        "Format géant — pour les grands appétits !"
    };

    // Couleurs (identiques à VueClient)
    private static final Color ROUGE      = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);
    private static final Color VERT       = new Color(0, 110, 80);
    private static final Color GRIS_TEXTE = new Color(100, 90, 80);

    // Données client (simulées — à remplacer par les vraies données BDD)
    // TODO: passer le vrai solde depuis la BDD
    private static final double SOLDE_CLIENT = 50.00;
    private static final String NOM_CLIENT   = "Alice Smith";

    private final String nomPizza;
    private final double prixBase;

    private int tailleSelectionnee = 1; // 0=Naine, 1=Humaine, 2=Hogresse
    private JLabel lblPrixFinal;
    private JLabel lblSoldeApres;
    private JLabel lblAvertissement;
    private JButton btnConfirmer;
    private JPanel[] cartesTailles;

    public VueCommande(Frame parent, String nomPizza, double prixBase) {
        super(parent, "Commander — " + nomPizza, true); // true = modal
        this.nomPizza = nomPizza;
        this.prixBase = prixBase;

        setSize(480, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BEIGE_FOND);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContenu(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        add(root);
        rafraichirPrix(); // calcul initial
        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────
    //  EN-TÊTE
    // ──────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ROUGE);
        p.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel titre = new JLabel("🍕 " + nomPizza);
        titre.setFont(new Font("SansSerif", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        JLabel sous = new JLabel("Choisissez votre taille");
        sous.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sous.setForeground(new Color(255, 220, 200));

        JPanel tp = new JPanel();
        tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
        tp.setOpaque(false);
        tp.add(titre);
        tp.add(sous);

        p.add(tp, BorderLayout.WEST);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  CONTENU PRINCIPAL
    // ──────────────────────────────────────────────────────────
    private JPanel buildContenu() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BEIGE_FOND);
        p.setBorder(new EmptyBorder(20, 24, 10, 24));

        // ── Cartes de taille ──
        JPanel cartesPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        cartesPanel.setOpaque(false);
        cartesTailles = new JPanel[3];

        for (int i = 0; i < 3; i++) {
            cartesTailles[i] = buildCarteTaille(i);
            cartesPanel.add(cartesTailles[i]);
        }
        cartesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        p.add(cartesPanel);
        p.add(Box.createVerticalStrut(24));

        // ── Récapitulatif prix ──
        JPanel recap = new JPanel(new GridLayout(3, 2, 8, 6));
        recap.setBackground(Color.WHITE);
        recap.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));
        recap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        recap.add(labelMuted("Prix de base :"));
        recap.add(labelValeur(String.format("%.2f €", prixBase)));

        recap.add(labelMuted("Prix final :"));
        lblPrixFinal = labelValeur("—");
        lblPrixFinal.setForeground(ROUGE);
        lblPrixFinal.setFont(lblPrixFinal.getFont().deriveFont(Font.BOLD, 18f));
        recap.add(lblPrixFinal);

        recap.add(labelMuted("Solde après commande :"));
        lblSoldeApres = labelValeur("—");
        recap.add(lblSoldeApres);

        p.add(recap);
        p.add(Box.createVerticalStrut(12));

        // ── Avertissement solde insuffisant ──
        lblAvertissement = new JLabel("⚠ Solde insuffisant pour cette commande.");
        lblAvertissement.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAvertissement.setForeground(new Color(180, 30, 30));
        lblAvertissement.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblAvertissement.setVisible(false);
        p.add(lblAvertissement);

        // ── Infos client ──
        p.add(Box.createVerticalStrut(12));
        JLabel infoClient = new JLabel("Client : " + NOM_CLIENT + "  |  Solde actuel : " + String.format("%.2f €", SOLDE_CLIENT));
        infoClient.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoClient.setForeground(GRIS_TEXTE);
        infoClient.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(infoClient);

        return p;
    }

    // ── Carte cliquable pour une taille ──
    private JPanel buildCarteTaille(int index) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(10, 8, 10, 8)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icône taille
        String[] icones = { "🟡", "🟠", "🔴" };
        JLabel icone = new JLabel(icones[index]);
        icone.setFont(new Font("SansSerif", Font.PLAIN, 28));
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nom = new JLabel(TAILLES[index]);
        nom.setFont(new Font("SansSerif", Font.BOLD, 14));
        nom.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><center>" + DESCRIPTIONS[index] + "</center></html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        desc.setForeground(GRIS_TEXTE);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setHorizontalAlignment(SwingConstants.CENTER);

        double prix = prixBase * MULTIPLICATEURS[index];
        JLabel prixLabel = new JLabel(String.format("%.2f €", prix));
        prixLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        prixLabel.setForeground(VERT);
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icone);
        card.add(Box.createVerticalStrut(6));
        card.add(nom);
        card.add(Box.createVerticalStrut(4));
        card.add(desc);
        card.add(Box.createVerticalStrut(6));
        card.add(prixLabel);

        // Clic → sélection
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tailleSelectionnee = index;
                mettreAJourSelectionCartes();
                rafraichirPrix();
            }
        });

        return card;
    }

    // ──────────────────────────────────────────────────────────
    //  PIED DE PAGE
    // ──────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        p.setBackground(new Color(245, 235, 220));
        p.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnAnnuler.addActionListener(e -> dispose());

        btnConfirmer = new JButton("Confirmer la commande →");
        btnConfirmer.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnConfirmer.setBackground(VERT);
        btnConfirmer.setForeground(Color.WHITE);
        btnConfirmer.setFocusPainted(false);
        btnConfirmer.setBorderPainted(false);
        btnConfirmer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmer.addActionListener(e -> confirmerCommande());

        p.add(btnAnnuler);
        p.add(btnConfirmer);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  LOGIQUE — mise à jour de l'affichage
    // ──────────────────────────────────────────────────────────
    private void rafraichirPrix() {
        double prixFinal = prixBase * MULTIPLICATEURS[tailleSelectionnee];
        double soldeApres = SOLDE_CLIENT - prixFinal;
        boolean peutPayer = soldeApres >= 0;

        lblPrixFinal.setText(String.format("%.2f €", prixFinal));

        if (peutPayer) {
            lblSoldeApres.setText(String.format("%.2f €", soldeApres));
            lblSoldeApres.setForeground(VERT);
        } else {
            lblSoldeApres.setText(String.format("%.2f € (insuffisant)", soldeApres));
            lblSoldeApres.setForeground(ROUGE);
        }

        lblAvertissement.setVisible(!peutPayer);
        btnConfirmer.setEnabled(peutPayer);
        btnConfirmer.setBackground(peutPayer ? VERT : new Color(180, 180, 180));
    }

    private void mettreAJourSelectionCartes() {
        for (int i = 0; i < cartesTailles.length; i++) {
            boolean selected = (i == tailleSelectionnee);
            cartesTailles[i].setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(selected ? ROUGE : new Color(220, 210, 195), selected ? 2 : 1, true),
                new EmptyBorder(10, 8, 10, 8)
            ));
            cartesTailles[i].setBackground(selected ? new Color(255, 240, 235) : Color.WHITE);
        }
    }

    private void confirmerCommande() {
        double prixFinal = prixBase * MULTIPLICATEURS[tailleSelectionnee];
        String taille = TAILLES[tailleSelectionnee];
        // TODO: débiter le solde en BDD ici
        dispose();
        new VueLivraison((Frame) getParent(), nomPizza, taille, prixFinal);
    }

    // ──────────────────────────────────────────────────────────
    //  UTILITAIRES
    // ──────────────────────────────────────────────────────────
    private JLabel labelMuted(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(GRIS_TEXTE);
        return l;
    }

    private JLabel labelValeur(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(40, 30, 20));
        return l;
    }
}