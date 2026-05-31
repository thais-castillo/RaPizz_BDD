import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteLivraisons {
    public static void main(String[] args) {
        StatistiquesDAO dao = new StatistiquesDAO();
        try {
            // supprimer les deux lignes de test
            int deleted = dao.supprimerLivraisonsParIds(21,22);
            System.out.println("Suppression renvoyée : " + deleted);

            // afficher le récapitulatif CA après suppression
            Connection cnx = BaseDeDonnee.getInstance().getDatabase();
            String requete = "SELECT COALESCE(SUM(prix_pizza),0) AS brut, "
                    + "COALESCE(SUM(CASE WHEN gratuit = TRUE THEN prix_pizza ELSE 0 END),0) AS gratuits, "
                    + "COALESCE(SUM(CASE WHEN gratuit = FALSE OR gratuit IS NULL THEN prix_pizza ELSE 0 END),0) AS net "
                    + "FROM Livraison";
            try (PreparedStatement stmt = cnx.prepareStatement(requete);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double brut = rs.getDouble("brut");
                    double gratuits = rs.getDouble("gratuits");
                    double net = rs.getDouble("net");
                    System.out.println(String.format(java.util.Locale.FRANCE, "Après suppression -> brut=%.2f €, gratuits=%.2f €, net=%.2f €", brut, gratuits, net));
                }
            }

            // lister les livraisons restantes
            dao.printAllLivraisons();

        } catch (SQLException e) {
            System.err.println("Erreur durant suppression : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
