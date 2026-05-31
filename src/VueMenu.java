import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueMenu extends JFrame {
    private JButton btnCommander;
    private JButton btnStats;
    private JButton btnQuitter;

    // Constantes graphiques unifiées
    private static final Color ROUGE = new Color(180, 30, 30);
    private static final Color BEIGE_FOND = new Color(255, 248, 235);
    private static final Color VERT = new Color(0, 110, 80);
    private static final Color TEXTE = new Color(45, 35, 25);
    private static final Color TEXTE_MUTED = new Color(110, 100, 90);

    public VueMenu() {
        setTitle("RaPizz — Gestion Employé");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BEIGE_FOND);

        // Construction des sections
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

        JLabel titre = new JLabel("RaPizz");
        titre.setFont(new Font("SansSerif", Font.BOLD, 45));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Portail de gestion des commandes et livraisons de la franchise");
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
        center.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Panneau central sous forme de carte blanche élégante
        JPanel menuCard = new JPanel();
        menuCard.setLayout(new BoxLayout(menuCard, BoxLayout.Y_AXIS));
        menuCard.setBackground(Color.WHITE);
        menuCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(35, 50, 35, 50)
        ));

        JLabel lblSection = new JLabel("ACTIONS DISPONIBLES");
        lblSection.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSection.setForeground(TEXTE_MUTED);
        lblSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuCard.add(lblSection);
        menuCard.add(Box.createVerticalStrut(25));

        // Boutons
        btnCommander = createStyledMenuButton("Passer une commande", VERT);
        btnStats = createStyledMenuButton("Consulter les statistiques", new Color(35, 90, 160));
        
        menuCard.add(btnCommander);
        menuCard.add(Box.createVerticalStrut(15));
        menuCard.add(btnStats);

        center.add(menuCard);
        return center;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 14));
        footer.setBackground(new Color(245, 235, 220));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        btnQuitter = new JButton("Quitter l'application");
        btnQuitter.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnQuitter.setBackground(ROUGE);
        btnQuitter.setForeground(Color.WHITE);
        btnQuitter.setFocusPainted(false);
        btnQuitter.setBorderPainted(false);
        btnQuitter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(btnQuitter);
        return footer;
    }

    private JButton createStyledMenuButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setMaximumSize(new Dimension(320, 50));
        btn.setPreferredSize(new Dimension(320, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void addCommanderListener(ActionListener listener) { btnCommander.addActionListener(listener); }
    public void addStatsListener(ActionListener listener) { btnStats.addActionListener(listener); }
    public void addQuitterListener(ActionListener listener) { btnQuitter.addActionListener(listener); }

    public JButton getBtnCommander() { return btnCommander; }
    public JButton getBtnStats() { return btnStats; }
    public JButton getBtnQuitter() { return btnQuitter; }
}