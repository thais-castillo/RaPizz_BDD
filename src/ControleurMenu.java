import java.awt.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                // Récupération des statistiques depuis la base
                StatistiquesDAO dao = new StatistiquesDAO();
                Statistiques stats = dao.chargerStatistiques();

                VueStatistiques vueStats = new VueStatistiques(stats);
                new ControleurStatistiques(vueStats);

                System.out.println("[Controleur] VueStatistiques créée avec succès.");
            } catch (Throwable ex) {
                System.err.println("[Controleur] Erreur lors de l'ouverture de la page statistiques : ");
                ex.printStackTrace();
            }

            // fermer le menu après ouverture
            System.out.println("[Controleur] Fermeture du menu principal.");
            vue.dispose();
        }
    }

    // Gestion propre des animations au survol de la souris
    // Remplace la méthode gererEffetsHover() à la fin de ton ControleurMenu.java :
    private void gererEffetsHover() {
        // Couleurs de base de ton nouveau design
        Color vertBase = new Color(0, 110, 80);
        Color bleuBase = new Color(35, 90, 160);
        Color rougeBase = new Color(180, 30, 30);

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