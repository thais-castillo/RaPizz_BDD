import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;

public class VueLivraison extends JDialog {

    private static final Color ROUGE      = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);
    private static final Color VERT       = new Color(0, 110, 80);

    public VueLivraison(Frame parent, String nomPizza, String taille, double prix) {
        super(parent, "Commande confirmée !", true);

        int minutes = new Random().nextInt(46) + 5; // entre 5 et 50 min

        setSize(550, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BEIGE_FOND);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContenu(nomPizza, taille, prix, minutes), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────
    //  EN-TÊTE
    // ──────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(VERT);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel icone = new JLabel("✅");
        icone.setFont(new Font("SansSerif", Font.PLAIN, 36));
        icone.setBorder(new EmptyBorder(0, 0, 0, 14));

        JLabel titre = new JLabel("Commande confirmée !");
        titre.setFont(new Font("SansSerif", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);

        JLabel sous = new JLabel("Votre pizza est en cours de préparation");
        sous.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sous.setForeground(new Color(180, 230, 200));

        JPanel textes = new JPanel();
        textes.setLayout(new BoxLayout(textes, BoxLayout.Y_AXIS));
        textes.setOpaque(false);
        textes.add(titre);
        textes.add(sous);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.add(icone, BorderLayout.WEST);
        inner.add(textes, BorderLayout.CENTER);

        p.add(inner, BorderLayout.CENTER);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  CONTENU
    // ──────────────────────────────────────────────────────────
    private JPanel buildContenu(String nomPizza, String taille, double prix, int minutes) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BEIGE_FOND);
        p.setBorder(new EmptyBorder(24, 28, 16, 28));

        // ── Bloc ETA ──
        JPanel etaCard = new JPanel();
        etaCard.setLayout(new BoxLayout(etaCard, BoxLayout.Y_AXIS));
        etaCard.setBackground(Color.WHITE);
        etaCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));
        etaCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel lblEtaTitre = new JLabel("🛵  Temps de livraison estimé");
        lblEtaTitre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEtaTitre.setForeground(new Color(100, 90, 80));
        lblEtaTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMinutes = new JLabel(minutes + " minutes");
        lblMinutes.setFont(new Font("SansSerif", Font.BOLD, 38));
        lblMinutes.setForeground(ROUGE);
        lblMinutes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHeure = new JLabel("Votre pizza devrait arriver vers " + heureEstimee(minutes));
        lblHeure.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblHeure.setForeground(new Color(120, 110, 100));
        lblHeure.setAlignmentX(Component.LEFT_ALIGNMENT);

        etaCard.add(lblEtaTitre);
        etaCard.add(Box.createVerticalStrut(4));
        etaCard.add(lblMinutes);
        etaCard.add(lblHeure);

        p.add(etaCard);
        p.add(Box.createVerticalStrut(16));

        // ── Récap commande ──
        JPanel recap = new JPanel(new GridLayout(3, 2, 8, 6));
        recap.setBackground(Color.WHITE);
        recap.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));
        recap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        recap.add(labelMuted("Pizza :"));
        recap.add(labelValeur(nomPizza));
        recap.add(labelMuted("Taille :"));
        recap.add(labelValeur(taille));
        recap.add(labelMuted("Montant débité :"));
        JLabel lblPrix = labelValeur(String.format("%.2f €", prix));
        lblPrix.setForeground(ROUGE);
        recap.add(lblPrix);

        p.add(recap);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  PIED DE PAGE
    // ──────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        p.setBackground(new Color(245, 235, 220));
        p.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        JButton btnRetour = new JButton("← Retour au menu");
        btnRetour.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRetour.setBackground(ROUGE);
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setBorderPainted(false);
        btnRetour.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> dispose());

        p.add(btnRetour);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  UTILITAIRES
    // ──────────────────────────────────────────────────────────
    private String heureEstimee(int minutes) {
        java.time.LocalTime maintenant = java.time.LocalTime.now().plusMinutes(minutes);
        return String.format("%02d:%02d", maintenant.getHour(), maintenant.getMinute());
    }

    private JLabel labelMuted(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(new Color(100, 90, 80));
        return l;
    }

    private JLabel labelValeur(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(40, 30, 20));
        return l;
    }
}