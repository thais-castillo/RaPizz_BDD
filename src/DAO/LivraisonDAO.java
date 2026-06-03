<<<<<<< HEAD:src/LivraisonDAO.java
import java.sql.CallableStatement;
=======
package DAO;
>>>>>>> 886463daec4817e8c4d346eb74a303bdbd87ba16:src/DAO/LivraisonDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Model.BaseDeDonnee;

public class LivraisonDAO {

    public LivraisonResultat enregistrerLivraison(int idClient, int idPizza, int idLivreur, int idVehicule, String taille, double prixFacture) {
        String requete = "{ CALL sp_ajouter_livraison(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return LivraisonResultat.erreur("BDD_INDISPONIBLE");

        try (CallableStatement stmt = cnx.prepareCall(requete)) {
            long maintenant = System.currentTimeMillis();
            
            // Séparation Date et Heure
            stmt.setDate(1, new Date(maintenant));
            stmt.setTime(2, new Time(maintenant));
            
            // Durée inconnue au moment de la commande -> NULL
            stmt.setNull(3, Types.INTEGER);
            
            stmt.setString(4, taille);
            stmt.setDouble(5, prixFacture);
            stmt.setInt(6, idLivreur);
            stmt.setInt(7, idClient);
            stmt.setInt(8, idVehicule);
            stmt.setInt(9, idPizza);
            stmt.registerOutParameter(10, Types.BOOLEAN);
            stmt.registerOutParameter(11, Types.DECIMAL);

            stmt.execute();

            boolean estGratuit = stmt.getBoolean(10);
            double soldeApres = stmt.getDouble(11);
            return LivraisonResultat.succes(estGratuit, soldeApres);
        } catch (SQLException e) {
            System.err.println("[LivraisonDAO] Erreur lors de l'appel de la procedure : " + e.getMessage());
            return LivraisonResultat.erreur(e.getMessage());
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

        try {
            String sqlUpdateLiv = "UPDATE Livraison SET duree = ? WHERE id_livraison = ?";
            try (PreparedStatement stmtUpdate = cnx.prepareStatement(sqlUpdateLiv)) {
                stmtUpdate.setInt(1, duree);
                stmtUpdate.setInt(2, idLivraison);
                int updated = stmtUpdate.executeUpdate();
                return updated > 0;
            }
        } catch (SQLException e) {
            System.err.println("[LivraisonDAO] Erreur lors de la clôture de la livraison : " + e.getMessage());
            return false;
        }
    }
}