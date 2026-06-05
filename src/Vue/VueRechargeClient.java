package Vue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VueRechargeClient extends JFrame {

    public static class ClientItem {
        private final int    id;
        private final String nom;
        private final double solde;

        public ClientItem(int id, String nom, double solde) {
            this.id    = id;
            this.nom   = nom;
            this.solde = solde;
        }

        public int    id()    { return id; }
        public String nom()   { return nom; }
        public double solde() { return solde; }

        @Override
        public String toString() { return nom + " (#" + id + ")"; }
    }

    private JTextField txtMontant;
    private JButton    btnValider;
    private JButton    btnAnnuler;
    private JLabel     lblMessage;

    private List<ClientItem> clients;
    private ClientItem       clientSelectionne = null;
    private JPanel[]         cartesClients;
    private JPanel           grilleClients;

    private static final Color ROUGE       = new Color(180, 30, 30);
    private static final Color BEIGE_FOND  = new Color(255, 248, 235);
    private static final Color ORANGE      = new Color(180, 100, 20);
    private static final Color VERT        = new Color(0, 110, 80);
    private static final Color TEXTE       = new Color(45, 35, 25);
    private static final Color TEXTE_MUTED = new Color(110, 100, 90);

    public VueRechargeClient(List<ClientItem> clients) {
        this.clients = clients;

        setTitle("RaPizz — Recharger un solde");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 620);
        setMinimumSize(new Dimension(480, 520));
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BEIGE_FOND);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildForm(),    BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);

        add(root);
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ORANGE);
        header.setBorder(new EmptyBorder(20, 35, 20, 35));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel titre = new JLabel("Recharger un solde");
        titre.setFont(new Font("SansSerif", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Sélectionnez un client puis saisissez le montant");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sousTitre.setForeground(new Color(255, 230, 200));

        texts.add(titre);
        texts.add(Box.createVerticalStrut(4));
        texts.add(sousTitre);

        header.add(texts, BorderLayout.WEST);
        return header;
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BEIGE_FOND);
        wrapper.setBorder(new EmptyBorder(20, 30, 10, 30));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));

        JLabel lblSection = new JLabel("SÉLECTION DU CLIENT");
        lblSection.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblSection.setForeground(TEXTE_MUTED);
        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblSection);
        card.add(Box.createVerticalStrut(12));

        grilleClients = new JPanel(new GridLayout(0, 2, 10, 10));
        grilleClients.setBackground(BEIGE_FOND);
        cartesClients = new JPanel[clients.size()];

        for (int i = 0; i < clients.size(); i++) {
            cartesClients[i] = buildCarteClient(i);
            grilleClients.add(cartesClients[i]);
        }

        JScrollPane scroll = new JScrollPane(grilleClients);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scroll.setPreferredSize(new Dimension(0, 200));
        scroll.setBorder(new LineBorder(new Color(220, 210, 195), 1, true));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scroll);

        card.add(Box.createVerticalStrut(20));

        JLabel lblTitreMontant = new JLabel("MONTANT À CRÉDITER");
        lblTitreMontant.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitreMontant.setForeground(TEXTE_MUTED);
        lblTitreMontant.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitreMontant);
        card.add(Box.createVerticalStrut(10));

        txtMontant = addField(card, "Montant à créditer (€) *", "Ex : 15.00");
        card.add(Box.createVerticalStrut(20));

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblMessage);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCarteClient(int index) {
        ClientItem client = clients.get(index);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel icone = new JLabel("👤");
        icone.setFont(new Font("SansSerif", Font.PLAIN, 22));
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNom = new JLabel(client.nom());
        lblNom.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNom.setForeground(TEXTE);
        lblNom.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblId = new JLabel("ID : " + client.id());
        lblId.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblId.setForeground(TEXTE_MUTED);
        lblId.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSolde = new JLabel(String.format("Solde : %.2f €", client.solde()));
        lblSolde.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSolde.setForeground(VERT);
        lblSolde.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icone);
        card.add(Box.createVerticalStrut(5));
        card.add(lblNom);
        card.add(Box.createVerticalStrut(2));
        card.add(lblId);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSolde);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clientSelectionne = client;
                mettreAJourSelectionCartes();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (clientSelectionne != client) {
                    card.setBackground(new Color(255, 248, 235)); // survol léger
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (clientSelectionne != client) {
                    card.setBackground(Color.WHITE);
                }
            }
        });

        return card;
    }

    private void mettreAJourSelectionCartes() {
        for (int i = 0; i < cartesClients.length; i++) {
            boolean selectionne = clients.get(i) == clientSelectionne;
            cartesClients[i].setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(
                    selectionne ? ORANGE : new Color(220, 210, 195),
                    selectionne ? 2 : 1,
                    true
                ),
                new EmptyBorder(10, 12, 10, 12)
            ));
            cartesClients[i].setBackground(selectionne ? new Color(255, 240, 225) : Color.WHITE);
        }
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 14));
        footer.setBackground(new Color(245, 235, 220));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 195, 175)));

        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAnnuler.setBackground(new Color(180, 170, 160));
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setBorderPainted(false);
        btnAnnuler.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnValider = new JButton("Créditer le compte");
        btnValider.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnValider.setBackground(ORANGE);
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);
        btnValider.setBorderPainted(false);
        btnValider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(btnAnnuler);
        footer.add(btnValider);
        return footer;
    }

    private JTextField addField(JPanel parent, String label, String placeholder) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(TEXTE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setPreferredSize(new Dimension(340, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 190, 175), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText(""); field.setForeground(TEXTE);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setForeground(Color.GRAY); field.setText(placeholder);
                }
            }
        });

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(5));
        parent.add(field);
        return field;
    }

    public ClientItem getClientSelectionne() {
        return clientSelectionne;
    }

    public void rafraichirClients(List<ClientItem> nouveauxClients) {
        this.clients = nouveauxClients;
        this.clientSelectionne = null;

        grilleClients.removeAll();
        cartesClients = new JPanel[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            cartesClients[i] = buildCarteClient(i);
            grilleClients.add(cartesClients[i]);
        }

        grilleClients.revalidate();
        grilleClients.repaint();
    }

    public Double getMontant() {
        String v = txtMontant.getText().trim();
        if (v.equals("Ex : 15.00")) return null;
        try { return Double.parseDouble(v.replace(',', '.')); }
        catch (NumberFormatException e) { return null; }
    }

    public void afficherSucces(String message) {
        lblMessage.setForeground(new Color(0, 130, 60));
        lblMessage.setText("✔ " + message);
    }

    public void afficherErreur(String message) {
        lblMessage.setForeground(ROUGE);
        lblMessage.setText("✘ " + message);
    }

    public void reinitialiser() {
        lblMessage.setText(" ");
        resetField(txtMontant, "Ex : 15.00");
        clientSelectionne = null;
        mettreAJourSelectionCartes();
    }

    private void resetField(JTextField f, String ph) {
        f.setForeground(Color.GRAY); f.setText(ph);
    }

    public void addValiderListener(ActionListener l) { btnValider.addActionListener(l); }
    public void addAnnulerListener(ActionListener l) { btnAnnuler.addActionListener(l); }
    public JButton getBtnValider() { return btnValider; }
    public JButton getBtnAnnuler() { return btnAnnuler; }
}