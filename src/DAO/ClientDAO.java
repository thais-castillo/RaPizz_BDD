package DAO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.BaseDeDonnee;
import Model.Client;
import Vue.VueRechargeClient;

public class ClientDAO {

    /**
     * Récupère tous les clients de la base de données
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

    /**
     * Appelle la procédure stockée AjouterClient(nom, prenom, solde).
     */
    public void ajouterClient(String nom, String prenom, double solde) throws SQLException {
        if (solde < 0) {
            throw new IllegalArgumentException("Le solde initial ne peut pas être négatif.");
        }

        String sql = "{CALL AjouterClient(?, ?, ?)}";
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) {
            throw new SQLException("Impossible d'accéder à la base de données.");
        }

        try (CallableStatement stmt = cnx.prepareCall(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setDouble(3, solde);
            stmt.execute();
            System.out.println("[ClientDAO] Client ajouté : " + prenom + " " + nom);
        }
    }

    /**
     * Retourne tous les clients pour le sélecteur de la vue recharge.
     * Le nom affiché est "Prénom Nom".
     */
    public List<VueRechargeClient.ClientItem> listerClients() throws SQLException {
        List<VueRechargeClient.ClientItem> liste = new ArrayList<>();
        String sql = "SELECT Id_Client, nom, prenom, solde FROM Client ORDER BY nom, prenom";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) {
            throw new SQLException("Impossible d'accéder à la base de données.");
        }

        try (PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new VueRechargeClient.ClientItem(
                    rs.getInt("Id_Client"),
                    rs.getString("prenom") + " " + rs.getString("nom"), // concaténation prénom + nom
                    rs.getDouble("solde")
                ));
            }
        }
        return liste;
    }

    /**
     * Ajoute le montant au solde actuel du client.
     */
    public void crediterSolde(int idClient, double montant) throws SQLException {
        String sql = "UPDATE Client SET solde = solde + ? WHERE Id_Client = ?";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) {
            throw new SQLException("Impossible d'accéder à la base de données.");
        }

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setDouble(1, montant);
            ps.setInt(2, idClient);
            int lignes = ps.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucun client trouvé avec l'ID " + idClient);
            }
            System.out.println("[ClientDAO] Solde du client " + idClient + " crédité de " + montant + " €.");
        }
    }
}