package Controleur;
import java.awt.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import DAO.StatistiquesDAO;
import Model.Statistiques;
import Vue.VueMenu;
import Vue.VueStatistiques;
import Vue.VueSuiviLivraisons;
import Vue.VueVente;
import javax.swing.JOptionPane;

public class ControleurMenu {
    private VueMenu vue;
    // private ModelePizz bdd; // Tu pourras l'ajouter plus tard ici !

    public ControleurMenu(VueMenu vue) {
        this.vue = vue;

        System.out.println("[ControleurMenu] initialisation du contrôleur menu.");

        // À remplacer dans le constructeur de ControleurMenu.java :
        this.vue.addCommanderListener(new ActionCommander());
        this.vue.addStatsListener(new ActionStats());
        this.vue.addQuitterListener(new ActionQuitter());
        this.vue.addLivraisonsListener(new ActionLivraisons());
        
        // On gère le style interactif (hover) directement depuis le contrôleur
        gererEffetsHover();
    }

    // À remplacer dans votre ControleurMenu.java :
    private class ActionCommander implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[Controleur] Changement de vue : Direction l'enregistrement des ventes.");
            
            // 1. Fermer le menu principal
            vue.dispose(); 
            
            // 2. Ouvrir l'interface de vente pour l'employé
            VueVente vueVente = new VueVente();
            ControleurVente controleurVente = new ControleurVente(vueVente);
            vueVente.setVisible(true); // On l'affiche
        }
    }

    // Classe interne pour l'action du bouton "Quitter"
    private class ActionQuitter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[Controleur] Fermeture de l'application.");
            System.exit(0);
        }
    }

    // Classe interne pour l'action du bouton "Voir les statistiques"
    private class ActionStats implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[Controleur] Clic détecté sur Stats — tentative d'ouverture.");
            try {
                // 1. Récupération des statistiques depuis la base
                StatistiquesDAO dao = new StatistiquesDAO();
                Statistiques stats = dao.chargerStatistiques();

                // 2. Création de la vue et de son contrôleur
                VueStatistiques vueStats = new VueStatistiques(stats);
                new ControleurStatistiques(vueStats);

                // 3. Rendre la nouvelle vue VISIBLE (Ligne indispensable !)
                vueStats.setVisible(true);

                System.out.println("[Controleur] VueStatistiques créée et affichée avec succès.");
                
                // 4. Fermer le menu principal seulement si la création a réussi
                System.out.println("[Controleur] Fermeture du menu principal.");
                vue.dispose();

            } catch (Throwable ex) {
                System.err.println("[Controleur] Erreur critique lors de l'ouverture de la page statistiques : ");
                ex.printStackTrace();
                
                // Optionnel : Alerter l'utilisateur plutôt que de laisser un écran figé
                JOptionPane.showMessageDialog(vue, 
                    "Impossible de charger les statistiques.\nVérifiez la console ou l'état de la base de données.", 
                    "Erreur de chargement", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ActionLivraisons implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        vue.dispose();
        VueSuiviLivraisons vsec = new VueSuiviLivraisons();
        new ControleurSuiviLivraisons(vsec); // Le tableau de bord temps réel
        vsec.setVisible(true);
    }
}

    // Gestion propre des animations au survol de la souris
    // Remplace la méthode gererEffetsHover() à la fin de ton ControleurMenu.java :
    private void gererEffetsHover() {
        // Couleurs de base de ton nouveau design
        Color vertBase = new Color(0, 110, 80);
        Color bleuBase = new Color(35, 90, 160);
        Color rougeBase = new Color(180, 30, 30);
        Color cyanBase = new Color(30, 130, 140);

        // Effet Hover pour le bouton Commander (Vert)
        vue.getBtnCommander().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnCommander().setBackground(vertBase.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnCommander().setBackground(vertBase);
            }
        });

        // Effet Hover pour le bouton Stats (Bleu)
        vue.getBtnStats().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnStats().setBackground(bleuBase.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnStats().setBackground(bleuBase);
            }
        });

        vue.getBtnLivraisons().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnLivraisons().setBackground(cyanBase.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnLivraisons().setBackground(cyanBase);
            }
        });

        // Effet Hover pour le bouton Quitter (Rouge)
        vue.getBtnQuitter().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnQuitter().setBackground(rougeBase.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnQuitter().setBackground(rougeBase);
            }
        });
    }
}