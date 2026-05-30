import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {

    /**
     * Récupère toutes les pizzas du catalogue
     * @return Liste d'objets Pizza
     */
    public List<Pizza> getAllPizzas() {
        List<Pizza> listePizzas = new ArrayList<>();
        String requete = "SELECT id_pizza, nom, prix FROM Pizza ORDER BY nom";

        // Récupération de l'unique connexion via notre Singleton
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();

        if (cnx == null) {
            System.err.println("[PizzaDAO] Impossible d'accéder à la base de données.");
            return listePizzas;
        }

        // Try-with-resources pour fermer automatiquement les flux SQL
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_pizza");
                String nom = rs.getString("nom");
                double prix = rs.getDouble("prix");

                // Instanciation du POJO et ajout à la liste
                listePizzas.add(new Pizza(id, nom, prix));
            }

        } catch (SQLException e) {
            System.err.println("[PizzaDAO] Erreur lors de la récupération des pizzas : " + e.getMessage());
        }

        return listePizzas;
    }
}