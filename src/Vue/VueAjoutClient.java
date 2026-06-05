package Vue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueAjoutClient extends JFrame {

    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtSolde;

    private JButton btnValider;
    private JButton btnAnnuler;

    private JLabel lblMessage;

    private static final Color ROUGE       = new Color(180, 30, 30);
    private static final Color BEIGE_FOND  = new Color(255, 248, 235);
    private static final Color VIOLET      = new Color(130, 60, 160);
    private static final Color TEXTE       = new Color(45, 35, 25);
    private static final Color TEXTE_MUTED = new Color(110, 100, 90);

    public VueAjoutClient() {
        setTitle("RaPizz — Ajouter un client");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 480);
        setMinimumSize(new Dimension(440, 420));
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
        header.setBackground(VIOLET);
        header.setBorder(new EmptyBorder(20, 35, 20, 35));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel titre = new JLabel("Nouveau client");
        titre.setFont(new Font("SansSerif", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        JLabel sousTitre = new JLabel("Remplissez les informations pour créer le compte");
        sousTitre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sousTitre.setForeground(new Color(230, 210, 245));

        texts.add(titre);
        texts.add(Box.createVerticalStrut(4));
        texts.add(sousTitre);

        header.add(texts, BorderLayout.WEST);
        return header;
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BEIGE_FOND);
        wrapper.setBorder(new EmptyBorder(30, 40, 10, 40));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 210, 195), 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel lblSection = new JLabel("INFORMATIONS DU CLIENT");
        lblSection.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblSection.setForeground(TEXTE_MUTED);
        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblSection);
        card.add(Box.createVerticalStrut(20));

        txtNom    = addField(card, "Nom *",    "Ex : Dupont");
        card.add(Box.createVerticalStrut(15));
        txtPrenom = addField(card, "Prénom *", "Ex : Marie");
        card.add(Box.createVerticalStrut(15));
        txtSolde  = addField(card, "Solde initial (€) *", "Ex : 20.00");
        card.add(Box.createVerticalStrut(8));

        JLabel note = new JLabel("La date d'abonnement sera définie à aujourd'hui et la bonification à 0.");
        note.setFont(new Font("SansSerif", Font.ITALIC, 11));
        note.setForeground(TEXTE_MUTED);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(note);
        card.add(Box.createVerticalStrut(20));

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblMessage);

        wrapper.add(card);
        return wrapper;
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
                    field.setText("");
                    field.setForeground(TEXTE);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(5));
        parent.add(field);
        return field;
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

        btnValider = new JButton("Créer le compte");
        btnValider.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnValider.setBackground(VIOLET);
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);
        btnValider.setBorderPainted(false);
        btnValider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(btnAnnuler);
        footer.add(btnValider);
        return footer;
    }

    public String getNom() {
        String v = txtNom.getText().trim();
        return v.equals("Ex : Dupont") ? "" : v;
    }

    public String getPrenom() {
        String v = txtPrenom.getText().trim();
        return v.equals("Ex : Marie") ? "" : v;
    }

    public Double getSolde() {
        String v = txtSolde.getText().trim();
        if (v.equals("Ex : 20.00")) return null;
        try {
            return Double.parseDouble(v.replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
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
        resetField(txtNom,    "Ex : Dupont");
        resetField(txtPrenom, "Ex : Marie");
        resetField(txtSolde,  "Ex : 20.00");
    }

    private void resetField(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
    }

    public void addValiderListener(ActionListener listener)  { btnValider.addActionListener(listener); }
    public void addAnnulerListener(ActionListener listener)  { btnAnnuler.addActionListener(listener); }

    public JButton getBtnValider()  { return btnValider; }
    public JButton getBtnAnnuler()  { return btnAnnuler; }
}