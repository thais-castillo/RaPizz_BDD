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

        // On attache les actions aux boutons de la vue
        this.vue.addCommanderListener(new ActionCommander());
        this.vue.addQuitterListener(new ActionQuitter());
        
        // On gère le style interactif (hover) directement depuis le contrôleur
        gererEffetsHover();
    }

    // Classe interne pour l'action du bouton "Commander"
    private class ActionCommander implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[Controleur] Clic sur Commander détecté.");
            
            // 1. Fermer ou masquer la vue actuelle
            vue.dispose(); 
            
            // 2. Ouvrir la vue suivante (VueClient)
            VueClient vueClient = new VueClient();
            ControleurClient controleurClient = new ControleurClient(vueClient);
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
    }
}