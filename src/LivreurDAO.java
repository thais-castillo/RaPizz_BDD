import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreurDAO {
    public List<Livreur> getAllLivreurs() {
        List<Livreur> liste = new ArrayList<>();
        String requete = "SELECT Id_Livreur, nom, prenom FROM Livreur ORDER BY nom";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return liste;

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(new Livreur(
                    rs.getInt("Id_Livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LivreurDAO] Erreur : " + e.getMessage());
        }
        return liste;
    }
}