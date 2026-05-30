import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Types;

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
}