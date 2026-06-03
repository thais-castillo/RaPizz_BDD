import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ControleurVente {
    private VueVente vue;
    private PizzaDAO pizzaDAO;
    private ClientDAO clientDAO;
    private LivreurDAO livreurDAO;
    private VehiculeDAO vehiculeDAO;

    public ControleurVente(VueVente vue) {
        this.vue = vue;
        
        // Initialisation de toute la couche DAO
        this.pizzaDAO = new PizzaDAO();
        this.clientDAO = new ClientDAO();
        this.livreurDAO = new LivreurDAO();
        this.vehiculeDAO = new VehiculeDAO();

        this.vue.addValiderListener(new ActionValiderVente());
        this.vue.addRetourListener(new ActionRetourMenu());
        
        remplirDonneesFormulaire();
    }

    private void remplirDonneesFormulaire() {
        // 1. Chargement des Pizzas
        vue.getCbPizzas().removeAllItems();
        for (Pizza p : pizzaDAO.getAllPizzas()) { vue.getCbPizzas().addItem(p); }

        // 2. Chargement des Clients
        vue.getCbClients().removeAllItems();
        for (Client c : clientDAO.getAllClients()) { vue.getCbClients().addItem(c); }

        // 3. Chargement des Livreurs
        vue.getCbLivreurs().removeAllItems();
        for (Livreur l : livreurDAO.getAllLivreurs()) { vue.getCbLivreurs().addItem(l); }

        // 4. Chargement des Véhicules
        vue.getCbVehicules().removeAllItems();
        for (Vehicule v : vehiculeDAO.getAllVehicules()) { vue.getCbVehicules().addItem(v); }
    }

    private class ActionValiderVente implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = (Client) vue.getCbClients().getSelectedItem();
        Pizza pizza = (Pizza) vue.getCbPizzas().getSelectedItem();
        String taille = (String) vue.getCbTailles().getSelectedItem();
        Livreur livreur = (Livreur) vue.getCbLivreurs().getSelectedItem();
        Vehicule vehicule = (Vehicule) vue.getCbVehicules().getSelectedItem();

        if (client == null || pizza == null || taille == null || livreur == null || vehicule == null) {
            JOptionPane.showMessageDialog(vue, "Veuillez remplir tous les champs du formulaire.", "Formulaire incomplet", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcul du prix selon la taille
        double prixCalcule = pizza.getPrix();
        if (taille.equals("Naine")) {
            prixCalcule -= 3.0;
        } else if (taille.equals("Ogresse")) {
            prixCalcule += 3.0;
        }
        if (prixCalcule <= 0) prixCalcule = 0.01; // Sécurité pour le CHECK > 0

        // Adaptation pour le 'H' de 'Hogresse' demandé par ta contrainte SQL
        String tailleSQL = taille.equals("Ogresse") ? "Hogresse" : taille;

        // Enregistrement en BDD (fidelite geree en SQL)
        LivraisonResultat resultat = new LivraisonDAO().enregistrerLivraison(
            client.getIdClient(),
            pizza.getIdPizza(),
            livreur.getIdLivreur(),
            vehicule.getIdVehicule(),
            tailleSQL,
            prixCalcule
        );

        if (resultat.isSucces()) {
            String msgSucces = "Commande validee !\n";
            if (resultat.isGratuit()) {
                msgSucces += "Offert par la maison (fidelite) !";
            } else {
                msgSucces += "Debite : " + String.format("%.2f", prixCalcule) + " EUR";
            }
            msgSucces += "\nSolde restant : " + String.format("%.2f", resultat.getSoldeApres()) + " EUR";
            JOptionPane.showMessageDialog(vue, msgSucces, "Succes", JOptionPane.INFORMATION_MESSAGE);
            remplirDonneesFormulaire();
        } else {
            String err = resultat.getErreur();
            if (err != null && err.contains("SOLDE_INSUFFISANT")) {
                JOptionPane.showMessageDialog(vue, "Solde insuffisant.", "Erreur de paiement", JOptionPane.WARNING_MESSAGE);
            } else if (err != null && err.contains("CLIENT_INEXISTANT")) {
                JOptionPane.showMessageDialog(vue, "Client introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vue, "Erreur SQL lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    private class ActionRetourMenu implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vue.dispose(); 
            VueMenu vueMenu = new VueMenu();
            new ControleurMenu(vueMenu);
            vueMenu.setVisible(true);
        }
    }
}