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

        // Règle de fidélité
        double montantADebiter = prixCalcule;
        int futursPoints = client.getBonification() + 1;
        boolean estGratuite = false;

        if (client.getBonification() >= 9) { 
            montantADebiter = 0.0; // Le client ne paye rien
            futursPoints = 0;
            estGratuite = true;
        }

        // Vérification du solde du client
        double nouveauSolde = client.getSolde() - montantADebiter;
        if (nouveauSolde < 0) {
            JOptionPane.showMessageDialog(vue, "⚠️ Solde insuffisant !", "Erreur de paiement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Enregistrement en BDD
        // 1. Mise à jour du client (débit + points)
        boolean updateClientOK = clientDAO.mettreAJourSoldeEtFidelite(client.getIdClient(), nouveauSolde, futursPoints);
        
        if (updateClientOK) {
            // 2. Insertion de la livraison (on enregistre le prix calculé, mais le flag gratuit indique la gratuité)
            boolean insertionLivraisonOK = new LivraisonDAO().enregistrerLivraison(
                client.getIdClient(), 
                pizza.getIdPizza(), 
                livreur.getIdLivreur(), 
                vehicule.getIdVehicule(), 
                tailleSQL, 
                prixCalcule, // Reste > 0 pour valider le CHECK SQL
                estGratuite
            );

            if (insertionLivraisonOK) {
                String msgSucces = "Commande validée !\n";
                if (estGratuite) {
                    msgSucces += "🎁 Offert par la maison (10ème pizza) !";
                } else {
                    msgSucces += "Débité : " + String.format("%.2f", montantADebiter) + " €";
                }
                JOptionPane.showMessageDialog(vue, msgSucces, "Succès", JOptionPane.INFORMATION_MESSAGE);
                remplirDonneesFormulaire(); // Rafraîchit l'affichage du solde
            } else {
                JOptionPane.showMessageDialog(vue, "Erreur Livraison SQL.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(vue, "Erreur Client SQL.", "Erreur", JOptionPane.ERROR_MESSAGE);
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