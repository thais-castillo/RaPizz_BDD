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

        // On attache les actions aux boutons de la vue
        this.vue.addCommanderListener(new ActionCommander());
        System.out.println("[ControleurMenu] listener Commander attaché.");
        this.vue.addStatsListener(new ActionStats());
        System.out.println("[ControleurMenu] listener Stats attaché.");
        this.vue.addQuitterListener(new ActionQuitter());
        System.out.println("[ControleurMenu] listener Quitter attaché.");
        
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
    private void gererEffetsHover() {
        Color colorPrimary = new Color(211, 47, 47);
        Color colorAccent = new Color(56, 142, 60); // Vert basilic au survol
        Color colorGrey = new Color(70, 70, 70);

        vue.getBtnCommander().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnCommander().setBackground(colorAccent);
                vue.getBtnCommander().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnCommander().setBackground(colorPrimary);
            }
        });

        vue.getBtnQuitter().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnQuitter().setBackground(colorGrey.brighter());
                vue.getBtnQuitter().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnQuitter().setBackground(colorGrey);
            }
        });

        vue.getBtnStats().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnStats().setBackground(colorAccent.darker());
                vue.getBtnStats().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnStats().setBackground(colorAccent);
            }
        });
    }
}