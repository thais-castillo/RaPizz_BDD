import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class VueClient extends JFrame {

    // ══════════════════════════════════════════════════════════
    //  DONNÉES PIZZAS — remplace les URL par tes vraies images
    // ══════════════════════════════════════════════════════════
    private static final Object[][] PIZZAS = {
        // { "Nom",           prix,  "URL_IMAGE" }
        { "Margherita",    8.50,  "assets/margherita_pizza.png"},
        { "Regina",        9.00,  "https://REMPLACER_ICI/regina.jpg"        },
        { "Pepperoni",     9.50,  "https://REMPLACER_ICI/pepperoni.jpg"     },
        { "Végétarienne", 10.00,  "https://REMPLACER_ICI/vegetarienne.jpg"  },
        { "Hawaiienne",   11.00,  "https://REMPLACER_ICI/hawaiienne.jpg"    },
        { "Quatre Fromages",12.00,"https://REMPLACER_ICI/quatre_fromages.jpg"},
        { "Calzone",      11.50,  "https://REMPLACER_ICI/calzone.jpg"       },
        { "BBQ Chicken",  12.50,  "https://REMPLACER_ICI/bbq_chicken.jpg"   },
        { "Chèvre Miel",  13.00,  "https://REMPLACER_ICI/chevre_miel.jpg"   },
        { "Mexicaine",    12.00,  "https://REMPLACER_ICI/mexicaine.jpg"     },
        { "Carnivore",    13.50,  "https://REMPLACER_ICI/carnivore.jpg"     },
        { "Vegan Delight",11.00,  "https://REMPLACER_ICI/vegan_delight.jpg" },
    };

    // Taille de chaque carte pizza
    private static final int CARD_W = 200;
    private static final int CARD_H = 230;
    private static final int IMG_W  = 200;
    private static final int IMG_H  = 130;

    // Couleurs de la charte
    private static final Color ROUGE      = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);
    private static final Color CARTE_FOND = Color.WHITE;
    private static final Color TEXTE_PRIX = new Color(0, 110, 80);

    public VueClient() {
        setTitle("RaPizz — Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // plein écran
        setBackground(BEIGE_FOND);

        // ── Panneau racine ─────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BEIGE_FOND);

        // ── En-tête ────────────────────────────────────────────
        JPanel header = buildHeader();
        root.add(header, BorderLayout.NORTH);

        // ── Grille de pizzas ───────────────────────────────────
        JPanel grid = buildPizzaGrid();
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BEIGE_FOND);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        root.add(scroll, BorderLayout.CENTER);

        // ── Pied de page ───────────────────────────────────────
        JPanel footer = buildFooter();
        root.add(footer, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────
    //  EN-TÊTE
    // ──────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ROUGE);
        p.setBorder(new EmptyBorder(18, 30, 18, 30));

        JLabel titre = new JLabel("🍕 RaPizz");
        titre.setFont(new Font("SansSerif", Font.BOLD, 48));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Notre menu du moment");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sousTitre.setForeground(new Color(255, 220, 200));

        JPanel titrePanel = new JPanel();
        titrePanel.setLayout(new BoxLayout(titrePanel, BoxLayout.Y_AXIS));
        titrePanel.setOpaque(false);
        titrePanel.add(titre);
        titrePanel.add(sousTitre);

        p.add(titrePanel, BorderLayout.WEST);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  GRILLE DE CARTES PIZZA
    // ──────────────────────────────────────────────────────────
    private JPanel buildPizzaGrid() {
        JPanel grid = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 16));
        grid.setBackground(BEIGE_FOND);
        grid.setBorder(new EmptyBorder(20, 20, 20, 20));

        for (Object[] pizza : PIZZAS) {
            grid.add(buildPizzaCard(
                (String) pizza[0],
                (double) pizza[1],
                (String) pizza[2]
            ));
        }
        return grid;
    }

    // ──────────────────────────────────────────────────────────
    //  CARTE INDIVIDUELLE
    // ──────────────────────────────────────────────────────────
    private JPanel buildPizzaCard(String nom, double prix, String imageUrl) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARTE_FOND);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(10, 10, 12, 10)
        ));
        card.setPreferredSize(new Dimension(CARD_W, CARD_H));
        card.setMaximumSize(new Dimension(CARD_W, CARD_H));

        // ── Image ──
        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setPreferredSize(new Dimension(IMG_W, IMG_H));
        imgLabel.setMinimumSize(new Dimension(IMG_W, IMG_H));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Chargement de l'image en arrière-plan pour ne pas bloquer l'UI
        new Thread(() -> {
            ImageIcon icon = loadImage(imageUrl, IMG_W, IMG_H);
            SwingUtilities.invokeLater(() -> {
                imgLabel.setIcon(icon);
                if (icon == null) imgLabel.setText("🍕"); // fallback si pas d'image
                card.revalidate();
            });
        }).start();

        // ── Nom ──
        JLabel nomLabel = new JLabel(nom);
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomLabel.setForeground(new Color(50, 30, 10));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Prix ──
        JLabel prixLabel = new JLabel(String.format("%.2f €", prix));
        prixLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        prixLabel.setForeground(TEXTE_PRIX);
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Bouton Commander ──
        JButton btnCmd = new JButton("Commander");
        btnCmd.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCmd.setBackground(ROUGE);
        btnCmd.setForeground(Color.WHITE);
        btnCmd.setFocusPainted(false);
        btnCmd.setBorderPainted(false);
        btnCmd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCmd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // TODO: brancher la logique de commande ici
        btnCmd.addActionListener(e ->
            new VueCommande(
                (java.awt.Frame) SwingUtilities.getWindowAncestor(VueClient.this),
                nom,
                prix
            )
        );

        card.add(imgLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(nomLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(prixLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(btnCmd);

        return card;
    }

    // ──────────────────────────────────────────────────────────
    //  CHARGEMENT + REDIMENSIONNEMENT D'UNE IMAGE DEPUIS UNE URL
    // ──────────────────────────────────────────────────────────
    private ImageIcon loadImage(String imageUrl, int w, int h) {
        try {
            ImageIcon raw = new ImageIcon(new URL(imageUrl));
            // Attendre que l'image soit chargée
            MediaTracker tracker = new MediaTracker(new JLabel());
            tracker.addImage(raw.getImage(), 0);
            tracker.waitForAll();
            Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null; // L'emoji fallback sera affiché
        }
    }

    // ──────────────────────────────────────────────────────────
    //  PIED DE PAGE
    // ──────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        p.setBackground(new Color(245, 235, 220));
        p.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        JButton btnClient = new JButton("Mon compte");
        btnClient.setFont(new Font("SansSerif", Font.PLAIN, 13));
        // TODO: btnClient.addActionListener(e -> new VueClient(idClient));

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnQuitter.addActionListener(e -> System.exit(0));

        p.add(btnClient);
        p.add(btnQuitter);
        return p;
    }

    // ──────────────────────────────────────────────────────────
    //  WRAPLAYOUT — layout qui passe à la ligne automatiquement
    // ──────────────────────────────────────────────────────────
    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) {
                    Container parent = target.getParent();
                    if (parent != null) targetWidth = parent.getSize().width;
                }
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - insets.left - insets.right;

                int x = 0, y = insets.top + vgap, rowH = 0;

                for (Component c : target.getComponents()) {
                    if (!c.isVisible()) continue;
                    Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                    if (x != 0 && x + d.width > maxWidth) {
                        y += rowH + vgap;
                        x = 0; rowH = 0;
                    }
                    x += d.width + hgap;
                    rowH = Math.max(rowH, d.height);
                }
                y += rowH + vgap + insets.bottom;
                return new Dimension(maxWidth, y);
            }
        }
    }
}