package Controleur;

import Vue.VueMenu;
import Vue.VueRechargeClient;
import Vue.VueRechargeClient.ClientItem;
import DAO.ClientDAO;

import javax.swing.JOptionPane;
import java.util.List;

public class ControleurRechargeClient {

    private final VueMenu     vueMenu;
    private VueRechargeClient vue;

    public ControleurRechargeClient(VueMenu vueMenu) {
        this.vueMenu = vueMenu;
        vueMenu.addRechargeClientListener(e -> ouvrirVue());
    }

    private void ouvrirVue() {
        try {
            // 1. Charger la liste des clients depuis la BDD
            ClientDAO dao = new ClientDAO();
            List<ClientItem> clients = dao.listerClients();

            // 2. Créer la vue avec la liste
            vue = new VueRechargeClient(clients);

            vue.addAnnulerListener(ev -> {
                vue.reinitialiser();
                vue.setVisible(false);
            });

            vue.addValiderListener(ev -> {
                ClientItem client = vue.getClientSelectionne();
                Double montant    = vue.getMontant();

                if (client == null) {
                    vue.afficherErreur("Veuillez sélectionner un client.");
                    return;
                }
                if (montant == null || montant <= 0) {
                    vue.afficherErreur("Montant invalide (doit être > 0).");
                    return;
                }

                try {
                    dao.crediterSolde(client.id(), montant);
                    vue.afficherSucces(String.format(
                        "%s rechargé de %.2f €.", client, montant
                    ));
                    vue.reinitialiser();

                    // Recharge la liste depuis la BDD et met à jour les cartes
                    vue.rafraichirClients(dao.listerClients());

                } catch (Exception ex) {
                    vue.afficherErreur("Erreur BDD : " + ex.getMessage());
                }
            });

            vue.setVisible(true);

        } catch (Exception ex) {
            // Affiche l'erreur à l'écran pour pouvoir déboguer
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                vueMenu,
                "Impossible d'ouvrir la vue de rechargement :\n"
                    + ex.getClass().getSimpleName() + " — " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}