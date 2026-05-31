import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class ControleurSuiviLivraisons {
    private VueSuiviLivraisons vue;
    private LivraisonDAO livraisonDAO;
    private Timer refreshTimer;
    private List<String[]> livraisonsCache;

    public ControleurSuiviLivraisons(VueSuiviLivraisons vue) {
        this.vue = vue;
        this.livraisonDAO = new LivraisonDAO();
        this.vue.addCloturerDirectListener(new ActionCloturerDirect());

        this.vue.addRetourListener(e -> {
            refreshTimer.stop(); // On arrête le timer pour libérer la mémoire
            vue.dispose();
            VueMenu vm = new VueMenu();
            new ControleurMenu(vm);
            vm.setVisible(true);
        });

        // 1. Charger la liste depuis la BDD une première fois
        chargerDonneesDepuisBDD();

        // 2. Lancer le thread d'animation de l'horloge (s'exécute toutes les 1000ms soit 1 seconde)
        refreshTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculerEtMettreAJourHorloges();
            }
        });
        refreshTimer.start();
    }

    private void chargerDonneesDepuisBDD() {
        // On récupère les fiches "durée IS NULL"
        livraisonsCache = livraisonDAO.getLivraisonsEnCours();
        calculerEtMettreAJourHorloges();
    }

    private void calculerEtMettreAJourHorloges() {
        vue.getTableModel().setRowCount(0); // On efface le tableau
        LocalTime maintenant = LocalTime.now();

        for (String[] liv : livraisonsCache) {
            String id = liv[0];
            String client = liv[1];
            String heureDepartStr = liv[2];
            String pizza = liv[3];
            String vehicule = liv[4];

            // Parse de l'heure SQL (format HH:mm:ss)
            LocalTime heureDepart = LocalTime.parse(heureDepartStr);
            
            // Calcul de la durée écoulée
            long secondesEcoulees = Duration.between(heureDepart, maintenant).getSeconds();
            long tempsLimiteSecondes = 30 * 60; // 30 minutes en secondes
            long secondesRestantes = tempsLimiteSecondes - secondesEcoulees;

            String statutTemps;
            if (secondesRestantes > 0) {
                long minutes = secondesRestantes / 60;
                long secondes = secondesRestantes % 60;
                statutTemps = String.format("%02d:%02d restant", minutes, secondes);
            } else {
                long secondesRetard = Math.abs(secondesRestantes);
                long minutes = secondesRetard / 60;
                long secondes = secondesRetard % 60;
                statutTemps = String.format("⚠️ RETARD (%02d:%02d) - Pizza Gratuite", minutes, secondes);
            }

            // Injection des lignes recalculées dans l'interface graphique
            vue.getTableModel().addRow(new Object[]{id, client, pizza, vehicule, heureDepartStr, statutTemps});
        }
    }

    private class ActionCloturerDirect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Puisqu'on clique sur le bouton directement dans la cellule, JTable sait quelle ligne est active
            int ligneSelectionnee = vue.getTableau().getEditingRow();
            
            // Sécurité si le clic n'est pas bien capté pendant le refresh
            if (ligneSelectionnee == -1) {
                ligneSelectionnee = vue.getTableau().getSelectedRow();
            }
            if (ligneSelectionnee == -1) return;

            // 1. Récupération de l'ID de livraison sur la ligne cliquée
            int idLivraison = Integer.parseInt(vue.getTableau().getValueAt(ligneSelectionnee, 0).toString());
            String clientNom = vue.getTableau().getValueAt(ligneSelectionnee, 1).toString();

            // 2. Ouverture d'une boîte de dialogue élégante pour demander la durée
            String dureeStr = JOptionPane.showInputDialog(vue, 
                "Indiquez le temps réel mis par le livreur pour :\nClient : " + clientNom + "\nCommande ID : " + idLivraison,
                "Enregistrement du Retour", 
                JOptionPane.QUESTION_MESSAGE
            );

            // Si l'utilisateur clique sur Annuler ou ferme la fenêtre
            if (dureeStr == null) return; 
            
            dureeStr = dureeStr.trim();
            if (dureeStr.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Vous devez spécifier un nombre de minutes.", "Champ vide", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int duree = Integer.parseInt(dureeStr);

                // 3. Clôture en Base de données et remboursement auto
                boolean succes = livraisonDAO.cloturerLivraison(idLivraison, duree);

                if (succes) {
                    JOptionPane.showMessageDialog(vue, "Livraison clôturée !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    
                    // 4. Force le rechargement immédiat pour enlever la ligne
                    livraisonsCache = livraisonDAO.getLivraisonsEnCours();
                    calculerEtMettreAJourHorloges();
                } else {
                    JOptionPane.showMessageDialog(vue, "Erreur SQL lors de la clôture.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vue, "Veuillez entrer un nombre de minutes valide.", "Erreur Format", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}