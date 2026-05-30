import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControleurVente {
    private VueVente vue;
    
    // Étape 1 : Déclaration du DAO
    private PizzaDAO pizzaDAO;

    public ControleurVente(VueVente vue) {
        this.vue = vue;
        
        // Étape 2 : Instanciation du DAO
        this.pizzaDAO = new PizzaDAO();

        // Liaison des boutons aux classes d'action
        this.vue.addValiderListener(new ActionValiderVente());
        this.vue.addRetourListener(new ActionRetourMenu());
        
        // Étape 3 : Remplissage des données (Vraies + Simulées pour l'instant)
        remplirDonneesFormulaire();
    }

    private void remplirDonneesFormulaire() {
        // --- DONNÉES RÉELLES (PIZZAS) ---
        // On récupère la liste des pizzas depuis la base via le DAO
        List<Pizza> cataloguePizzas = pizzaDAO.getAllPizzas();
        
        // On vide le ComboBox par sécurité
        vue.getCbPizzas().removeAllItems();
        
        // On ajoute chaque objet Pizza dans le JComboBox
        for (Pizza p : cataloguePizzas) {
            vue.getCbPizzas().addItem(p);
        }

        // --- SIMULATIONS TEMPORAIRES (En attendant les autres DAO) ---
        // (Ces lignes seront supprimées au fur et à mesure)
        vue.getCbClients().addItem("Jean Dupont (Solde: 45.00€)");
        vue.getCbLivreurs().addItem("Marco Simpson");
        vue.getCbVehicules().addItem("Moto - AA-123-BB");
    }

    // Action : Enregistrer la commande
    private class ActionValiderVente implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[ControleurVente] Clic sur Valider.");
            
            // Exemple : Comment récupérer la pizza sélectionnée par l'employé
            Pizza pizzaSelectionnee = (Pizza) vue.getCbPizzas().getSelectedItem();
            
            if (pizzaSelectionnee != null) {
                System.out.println("Pizza choisie : " + pizzaSelectionnee.getNom());
                System.out.println("Prix de base : " + pizzaSelectionnee.getPrix() + " €");
                System.out.println("ID en BDD : " + pizzaSelectionnee.getIdPizza());
            }
        }
    }

    // Action : Retour en arrière
    private class ActionRetourMenu implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vue.dispose(); 
            VueMenu vueMenu = new VueMenu();
            ControleurMenu controleurMenu = new ControleurMenu(vueMenu);
            vueMenu.setVisible(true);
        }
    }
}