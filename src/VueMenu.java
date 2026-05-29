import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class VueMenu extends JFrame {
    private JButton btnCommander;
    private JButton btnQuitter;
    private JButton btnStats;
    private JLabel titre;
    private JLabel sousTitre;

    private final Color COLOR_BG = new Color(28, 28, 28);
    private final Color COLOR_PRIMARY = new Color(211, 47, 47);
    private final Color COLOR_TEXT_LIGHT = new Color(245, 245, 245);
    private final Color COLOR_TEXT_MUTED = new Color(175, 175, 175);

    public VueMenu() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("RaPizz - Menu Principal");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;

        // Titre
        gbc.gridy = 0;
        titre = new JLabel("RaPizz");
        titre.setFont(new Font("Montserrat", Font.BOLD, 90));
        titre.setForeground(COLOR_PRIMARY);
        mainPanel.add(titre, gbc);

        // Sous-titre
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 60, 0);
        sousTitre = new JLabel("Les meilleures pizzas de la base de données");
        sousTitre.setFont(new Font("SansSerif", Font.ITALIC, 20));
        sousTitre.setForeground(COLOR_TEXT_MUTED);
        mainPanel.add(sousTitre, gbc);

        // Bouton Commander
        gbc.gridy = 2;
        gbc.insets = new Insets(12, 0, 12, 0);
        btnCommander = createStyledButton("Passer une commande", COLOR_PRIMARY);
        mainPanel.add(btnCommander, gbc);

        // Bouton stats
        gbc.gridy = 3;
        btnStats = createStyledButton("Voir les statistiques", new Color(0, 0, 255));
        mainPanel.add(btnStats, gbc);

        // Bouton Quitter
        gbc.gridy = 4;
        btnQuitter = createStyledButton("Quitter l'application", new Color(70, 70, 70));
        mainPanel.add(btnQuitter, gbc);


        this.add(mainPanel);
        this.setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(300, 55));
        return button;
    }

    // --- CONNEXIONS POUR LE CONTROLEUR ---

    // Permet au contrôleur d'écouter les clics sur le bouton Commander
    public void addCommanderListener(ActionListener listener) {
        btnCommander.addActionListener(listener);
    }

    public void addStatsListener(ActionListener listener) {
        btnStats.addActionListener(listener);
    }

    // Permet au contrôleur d'écouter les clics sur le bouton Quitter
    public void addQuitterListener(ActionListener listener) {
        btnQuitter.addActionListener(listener);
    }

    // [Optionnel] Si tu veux garder l'effet de survol (Hover), on peut ajouter une méthode pour ça
    public void addButtonHoverListeners(MouseListener commanderHover, MouseListener quitterHover) {
        btnCommander.addMouseListener(commanderHover);
        btnQuitter.addMouseListener(quitterHover);
    }
    
    // Getters pour que le contrôleur puisse manipuler les composants si besoin
    public JButton getBtnCommander() { return btnCommander; }
    public JButton getBtnQuitter() { return btnQuitter; }
}