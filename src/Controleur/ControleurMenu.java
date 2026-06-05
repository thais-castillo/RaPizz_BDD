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
import Vue.VueAjoutClient;
import javax.swing.JOptionPane;

public class ControleurMenu {
    private VueMenu vue;

    public ControleurMenu(VueMenu vue) {
        this.vue = vue;

        this.vue.addCommanderListener(new ActionCommander());
        this.vue.addStatsListener(new ActionStats());
        this.vue.addQuitterListener(new ActionQuitter());
        this.vue.addLivraisonsListener(new ActionLivraisons());
        this.vue.addAjoutClientListener(new ActionAjoutClient());
        
        new ControleurRechargeClient(vue);

        gererEffetsHover();

        Color orangeBase = new Color(180, 100, 20);
        vue.getBtnRechargeClient().addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                vue.getBtnRechargeClient().setBackground(orangeBase.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                vue.getBtnRechargeClient().setBackground(orangeBase);
            }
        });
    }

    private class ActionCommander implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            vue.dispose(); 
            
            VueVente vueVente = new VueVente();
            ControleurVente controleurVente = new ControleurVente(vueVente);
            vueVente.setVisible(true);
        }
    }

    private class ActionQuitter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class ActionStats implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                StatistiquesDAO dao = new StatistiquesDAO();
                Statistiques stats = dao.chargerStatistiques();

                VueStatistiques vueStats = new VueStatistiques(stats);
                new ControleurStatistiques(vueStats);

                vueStats.setVisible(true);

                vue.dispose();

            } catch (Throwable ex) {
                System.err.println("[Controleur] Erreur critique lors de l'ouverture de la page statistiques : ");
                ex.printStackTrace();
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
        new ControleurSuiviLivraisons(vsec);
        vsec.setVisible(true);

    }
}

    private class ActionAjoutClient implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VueAjoutClient vueAjout = new VueAjoutClient();

            vueAjout.addValiderListener(ev -> {
                String nom    = vueAjout.getNom();
                String prenom = vueAjout.getPrenom();
                Double solde  = vueAjout.getSolde();

                if (nom.trim().isEmpty() || prenom.trim().isEmpty()) {
                    vueAjout.afficherErreur("Le nom et le prénom sont obligatoires.");
                    return;
                }
                if (solde == null) {
                    vueAjout.afficherErreur("Le solde doit être un nombre valide (ex : 20.00).");
                    return;
                }

                try {
                    DAO.ClientDAO dao = new DAO.ClientDAO();
                    dao.ajouterClient(nom, prenom, solde);

                    vueAjout.afficherSucces("Client créé avec succès !");
                    vueAjout.reinitialiser();
                } catch (Exception ex) {
                    System.err.println("[Controleur] Erreur ajout client : " + ex.getMessage());
                    vueAjout.afficherErreur("Erreur : " + ex.getMessage());
                }
            });

            vueAjout.addAnnulerListener(ev -> vueAjout.dispose());
            vueAjout.setVisible(true);
        }
    }

    private void gererEffetsHover() {
        Color vertBase = new Color(0, 110, 80);
        Color bleuBase = new Color(35, 90, 160);
        Color rougeBase = new Color(180, 30, 30);
        Color cyanBase = new Color(30, 130, 140);

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

        Color violetBase = new Color(130, 60, 160);
        vue.getBtnAjoutClient().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vue.getBtnAjoutClient().setBackground(violetBase.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                vue.getBtnAjoutClient().setBackground(violetBase);
            }
        });
    }
}