package Controleur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import DAO.LivraisonDAO;
import DAO.ClientDAO;
import DAO.VehiculeDAO;
import Model.Client;
import Model.Livreur;
import DAO.LivreurDAO;
import Model.Pizza;
import DAO.PizzaDAO;
import Model.Vehicule;
import Vue.VueMenu;
import Vue.VueVente;

public class ControleurVente {
    private VueVente vue;
    private PizzaDAO pizzaDAO;
    private ClientDAO clientDAO;
    private LivreurDAO livreurDAO;
    private VehiculeDAO vehiculeDAO;

    public ControleurVente(VueVente vue) {
        this.vue = vue;
        
        this.pizzaDAO = new PizzaDAO();
        this.clientDAO = new ClientDAO();
        this.livreurDAO = new LivreurDAO();
        this.vehiculeDAO = new VehiculeDAO();

        this.vue.addValiderListener(new ActionValiderVente());
        this.vue.addRetourListener(new ActionRetourMenu());
        
        remplirDonneesFormulaire();
    }

    private void remplirDonneesFormulaire() {

        vue.getCbPizzas().removeAllItems();
        for (Pizza p : pizzaDAO.getAllPizzas()) { vue.getCbPizzas().addItem(p); }

        vue.getCbClients().removeAllItems();
        for (Client c : clientDAO.getAllClients()) { vue.getCbClients().addItem(c); }

        vue.getCbLivreurs().removeAllItems();
        for (Livreur l : livreurDAO.getAllLivreurs()) { vue.getCbLivreurs().addItem(l); }

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

        double prixCalcule = pizza.getPrix();
        if (taille.equals("Naine")) {
            prixCalcule -= 3.0;
        } else if (taille.equals("Ogresse")) {
            prixCalcule += 3.0;
        }
        if (prixCalcule <= 0) prixCalcule = 0.01;

        String tailleSQL = taille.equals("Ogresse") ? "Hogresse" : taille;

        double montantADebiter = prixCalcule;
        int futursPoints = client.getBonification() + 1;
        boolean estGratuite = false;

        if (client.getBonification() >= 9) { 
            montantADebiter = 0.0;
            futursPoints = 0;
            estGratuite = true;
        }

        double nouveauSolde = client.getSolde() - montantADebiter;
        if (nouveauSolde < 0) {
            JOptionPane.showMessageDialog(vue, "⚠️ Solde insuffisant !", "Erreur de paiement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean updateClientOK = clientDAO.mettreAJourSoldeEtFidelite(client.getIdClient(), nouveauSolde, futursPoints);
        
        if (updateClientOK) {
            boolean insertionLivraisonOK = new LivraisonDAO().enregistrerLivraison(
                client.getIdClient(), 
                pizza.getIdPizza(), 
                livreur.getIdLivreur(), 
                vehicule.getIdVehicule(), 
                tailleSQL, 
                prixCalcule,
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
                remplirDonneesFormulaire();
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