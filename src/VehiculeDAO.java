import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {
    public List<Vehicule> getAllVehicules() {
        List<Vehicule> liste = new ArrayList<>();
        String requete = "SELECT Id_Vehicule, type, immatricule FROM Vehicule ORDER BY type";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return liste;

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(new Vehicule(
                    rs.getInt("Id_Vehicule"),
                    rs.getString("type"),
                    rs.getString("immatricule")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[VehiculeDAO] Erreur : " + e.getMessage());
        }
        return liste;
    }
}