import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    /**
     * Récupère tous les clients de la base de données
     * @return Liste d'objets Client
     */
    public List<Client> getAllClients() {
        List<Client> listeClients = new ArrayList<>();
        String requete = "SELECT Id_Client, nom, prenom, solde, bonification FROM Client ORDER BY nom, prenom";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();

        if (cnx == null) {
            System.err.println("[ClientDAO] Impossible d'accéder à la base de données.");
            return listeClients;
        }

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id_Client");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                double solde = rs.getDouble("solde");
                int bonification = rs.getInt("bonification");

                listeClients.add(new Client(id, nom, prenom, solde, bonification));
            }

        } catch (SQLException e) {
            System.err.println("[ClientDAO] Erreur lors de la récupération des clients : " + e.getMessage());
        }

        return listeClients;
    }
    
    public boolean mettreAJourSoldeEtFidelite(int idClient, double nouveauSolde, int nouveauxPoints) {
        String requete = "UPDATE Client SET solde = ?, bonification = ? WHERE Id_Client = ?";
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
    
        if (cnx == null) return false;

        try (PreparedStatement stmt = cnx.prepareStatement(requete)) {
            stmt.setDouble(1, nouveauSolde);
            stmt.setInt(2, nouveauxPoints);
            stmt.setInt(3, idClient);
        
            int lignesModifiees = stmt.executeUpdate();
            return lignesModifiees > 0;
        } catch (SQLException e) {
            System.err.println("[ClientDAO] Erreur lors de l'update client : " + e.getMessage());
            return false;
        }
    }
}