import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LivraisonDAO {

    public boolean enregistrerLivraison(int idClient, int idPizza, int idLivreur, int idVehicule, String taille, double prixFacture, boolean estGratuit) {
        // Ta structure exacte
        String requete = "INSERT INTO Livraison (date_, heure, duree, taille, prix_pizza, gratuit, Id_Livreur, Id_Client, Id_Vehicule, id_pizza) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return false;

        try (PreparedStatement stmt = cnx.prepareStatement(requete)) {
            long maintenant = System.currentTimeMillis();
            
            // Séparation Date et Heure
            stmt.setDate(1, new Date(maintenant));
            stmt.setTime(2, new Time(maintenant));
            
            // Durée inconnue au moment de la commande -> NULL
            stmt.setNull(3, Types.INTEGER);
            
            stmt.setString(4, taille);
            stmt.setDouble(5, prixFacture);
            stmt.setBoolean(6, estGratuit);
            stmt.setInt(7, idLivreur);
            stmt.setInt(8, idClient);
            stmt.setInt(9, idVehicule);
            stmt.setInt(10, idPizza);

            int lignesInserees = stmt.executeUpdate();
            return lignesInserees > 0;
        } catch (SQLException e) {
            System.err.println("[LivraisonDAO] Erreur lors de l'insertion : " + e.getMessage());
            return false;
        }
    }

    public List<String[]> getLivraisonsEnCours() {
        List<String[]> liste = new ArrayList<>();
        // On filtre explicitement sur "duree IS NULL"
        String requete = "SELECT l.id_livraison, c.prenom, c.nom, l.heure, p.nom AS pizza_nom, v.type AS v_type " +
                        "FROM Livraison l " +
                        "JOIN Client c ON l.Id_Client = c.Id_Client " +
                        "JOIN Pizza p ON l.id_pizza = p.id_pizza " +
                        "JOIN Vehicule v ON l.Id_Vehicule = v.Id_Vehicule " +
                        "WHERE l.duree IS NULL " +
                        "ORDER BY l.heure ASC";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return liste;

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                liste.add(new String[]{
                    String.valueOf(rs.getInt("id_livraison")),
                    rs.getString("prenom") + " " + rs.getString("nom").toUpperCase(),
                    rs.getTime("heure").toString(), // Heure de départ
                    rs.getString("pizza_nom"),
                    rs.getString("v_type")
                });
            }
        } catch (SQLException e) {
            System.err.println("[LivraisonDAO] Erreur livraisons en cours : " + e.getMessage());
        }
        return liste;
    }

    public boolean cloturerLivraison(int idLivraison, int duree) {
    Connection cnx = BaseDeDonnee.getInstance().getDatabase();
    if (cnx == null) return false;

    // Déclarations des variables pour le calcul du remboursement
    int idClient = -1;
    double prixPizza = 0.0;
    boolean dejaGratuit = false;

    try {
        // 1. Récupérer les informations de la livraison actuelle
        String sqlInfos = "SELECT Id_Client, prix_pizza, gratuit FROM Livraison WHERE id_livraison = ?";
        try (PreparedStatement stmtInfos = cnx.prepareStatement(sqlInfos)) {
            stmtInfos.setInt(1, idLivraison);
            try (ResultSet rs = stmtInfos.executeQuery()) {
                if (rs.next()) {
                    idClient = rs.getInt("Id_Client");
                    prixPizza = rs.getDouble("prix_pizza");
                    dejaGratuit = rs.getBoolean("gratuit");
                } else {
                    // Si l'ID n'existe pas dans la table
                    return false;
                }
            }
        }

        // 2. Règle métier : Si durée > 30 min et que la pizza n'était pas déjà offerte (fidélité)
        // Alors elle devient gratuite à cause du retard.
        boolean appliquerRetard = (duree > 30) && !dejaGratuit;

        // 3. Mise à jour de la fiche de livraison (on injecte la durée et le nouvel état du flag gratuit)
        String sqlUpdateLiv = "UPDATE Livraison SET duree = ?, gratuit = ? WHERE id_livraison = ?";
        try (PreparedStatement stmtUpdate = cnx.prepareStatement(sqlUpdateLiv)) {
            stmtUpdate.setInt(1, duree);
            stmtUpdate.setBoolean(2, dejaGratuit || appliquerRetard);
            stmtUpdate.setInt(3, idLivraison);
            stmtUpdate.executeUpdate();
        }

        // 4. Si la pizza passe gratuite pour retard, on crédite instantanément le solde du client
        if (appliquerRetard) {
            String sqlRembourse = "UPDATE Client SET solde = solde + ? WHERE Id_Client = ?";
            try (PreparedStatement stmtRembourse = cnx.prepareStatement(sqlRembourse)) {
                stmtRembourse.setDouble(1, prixPizza);
                stmtRembourse.setInt(2, idClient);
                stmtRembourse.executeUpdate();
            }
        }

        return true;

    } catch (SQLException e) {
        System.err.println("[LivraisonDAO] Erreur lors de la clôture de la livraison : " + e.getMessage());
        return false;
    }
}
}