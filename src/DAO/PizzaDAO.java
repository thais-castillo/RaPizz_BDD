package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.BaseDeDonnee;
import Model.Pizza;

public class PizzaDAO {

    public List<Pizza> getAllPizzas() {
        List<Pizza> listePizzas = new ArrayList<>();
        String requete = "SELECT id_pizza, nom, prix FROM Pizza ORDER BY nom";

        Connection cnx = BaseDeDonnee.getInstance().getDatabase();

        if (cnx == null) {
            System.err.println("[PizzaDAO] Impossible d'accéder à la base de données.");
            return listePizzas;
        }

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_pizza");
                String nom = rs.getString("nom");
                double prix = rs.getDouble("prix");

                listePizzas.add(new Pizza(id, nom, prix));
            }

        } catch (SQLException e) {
            System.err.println("[PizzaDAO] Erreur lors de la récupération des pizzas : " + e.getMessage());
        }

        return listePizzas;
    }
}