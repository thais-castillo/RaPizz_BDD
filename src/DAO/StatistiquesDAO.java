package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Model.BaseDeDonnee;
import Model.Statistiques;

public class StatistiquesDAO {

    public Statistiques chargerStatistiques() {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();

        if (cnx == null) {
            System.err.println("[StatistiquesDAO] Connexion BDD indisponible.");
            return créerStatistiquesEnErreur();
        }

        try {
            // 1. Chargement des indicateurs de base originaux
            String chiffreAffairesTotal = lireChiffreAffairesTotal(cnx);
            String meilleurClient = lireMeilleurClient(cnx);
            String meilleurLivreur = lireMeilleurLivreur(cnx);
            String totalCommandes = lireTotalCommandes(cnx);
            String delaiMoyen = lireDelaiMoyen(cnx);
            String vehiculePlusUtilise = lireVehiculePlusUtilise(cnx);
            String pizzaStar = lirePizzaStar(cnx);
            String livraisonPlusRapide = lireLivraisonPlusRapide(cnx);
            String ingredientFavori = lireIngredientFavori(cnx);

            // 2. Chargement des extensions
            String moyenneCommandes = extraireMoyenneCommandes(cnx);
            String clientsAuDessusMoyenne = extraireNombreClientsAuDessusMoyenne(cnx);
            String clientMeilleurCA = extraireClientMeilleurCA(cnx);
            String jourPlusCharge = extraireJourPlusLivraisons(cnx);
            
            // Gestion du pire livreur
            List<String[]> pireLivreurRows = getPireLivreur(cnx);
            String pireLivreurNom = "N/A";
            String pireLivreurRetards = "0 retards";
            if (!pireLivreurRows.isEmpty() && pireLivreurRows.get(0).length >= 3) {
                String[] row = pireLivreurRows.get(0);
                pireLivreurNom = row[0] + " " + row[1];
                pireLivreurRetards = row[2] + " retards";
            }

            String vehiculesJamaisUtilises = extraireNombreVehiculesJamaisUtilises(cnx);
            String pizzaMoinsCommandee = extrairePizzaMoinsCommandee(cnx);
            String nombrePizzasMenu = extraireNombrePizzasUnique(cnx);

            // 3. On instancie l'objet global
            return new Statistiques(
                chiffreAffairesTotal, meilleurClient, meilleurLivreur,
                totalCommandes, delaiMoyen, vehiculePlusUtilise,
                pizzaStar, livraisonPlusRapide, ingredientFavori,
                moyenneCommandes, clientsAuDessusMoyenne, clientMeilleurCA,
                jourPlusCharge, pireLivreurNom, pireLivreurRetards,
                vehiculesJamaisUtilises, pizzaMoinsCommandee, nombrePizzasMenu
            );

        } catch (SQLException e) {
            System.err.println("[StatistiquesDAO] Erreur SQL globale lors du chargement : " + e.getMessage());
            e.printStackTrace();
            return créerStatistiquesEnErreur();
        }
    }

    private Statistiques créerStatistiquesEnErreur() {
        return new Statistiques(
            "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A",
            "0,00", "0", "N/A", "N/A", "N/A", "0 retards", "0", "N/A", "0"
        );
    }

    // =========================================================================
    // REQUÊTES DE BASE
    // =========================================================================

    private String lireChiffreAffairesTotal(Connection cnx) throws SQLException {
        String requete = "SELECT COALESCE(SUM(prix_pizza), 0) AS brut, "
                       + "COALESCE(SUM(CASE WHEN gratuit = TRUE THEN prix_pizza ELSE 0 END), 0) AS gratuits "
                       + "FROM Livraison";

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double brut = rs.getDouble("brut");
                double gratuits = rs.getDouble("gratuits");
                double net = brut - gratuits;
                return String.format(java.util.Locale.FRANCE, "%.2f €", net);
            }
        }
        return "0,00 €";
    }

    private String lireMeilleurClient(Connection cnx) throws SQLException {
        String requete = "SELECT c.nom, c.prenom, COUNT(*) AS nombre_commandes " +
                         "FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client " +
                         "GROUP BY c.Id_Client, c.nom, c.prenom " +
                         "ORDER BY nombre_commandes DESC, c.Id_Client ASC " +
                         "LIMIT 1";

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("prenom") + " " + rs.getString("nom");
            }
        }
        return "Aucun";
    }

    private String lireMeilleurLivreur(Connection cnx) throws SQLException {
        String requete = "SELECT li.nom, li.prenom, COUNT(*) AS nombre_livraisons " +
                         "FROM Livreur li JOIN Livraison l ON li.Id_Livreur = l.Id_Livreur " +
                         "GROUP BY li.Id_Livreur, li.nom, li.prenom " +
                         "ORDER BY nombre_livraisons DESC, li.Id_Livreur ASC " +
                         "LIMIT 1";

        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("prenom") + " " + rs.getString("nom");
            }
        }
        return "Aucun";
    }

    private String lireTotalCommandes(Connection cnx) throws SQLException {
        String requete = "SELECT COUNT(*) AS total FROM Livraison";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("total"));
            }
        }
        return "0";
    }

    private String lireDelaiMoyen(Connection cnx) throws SQLException {
        String requete = "SELECT AVG(duree) AS avg_duree FROM Livraison";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double avg = rs.getDouble("avg_duree");
                if (rs.wasNull()) return "0.0 min";
                return String.format(java.util.Locale.FRANCE, "%.1f min", avg);
            }
        }
        return "N/A";
    }

    private String lireVehiculePlusUtilise(Connection cnx) throws SQLException {
        String requete = "SELECT v.type, v.immatricule, COUNT(*) AS nb " +
                         "FROM Vehicule v JOIN Livraison l ON v.Id_Vehicule = l.Id_Vehicule " +
                         "GROUP BY v.Id_Vehicule, v.type, v.immatricule " +
                         "ORDER BY nb DESC " +
                         "LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("type") + " (" + rs.getString("immatricule") + ")";
            }
        }
        return "Aucun";
    }

    private String lirePizzaStar(Connection cnx) throws SQLException {
        String requete = "SELECT p.nom, COUNT(*) AS nb " +
                         "FROM Pizza p JOIN Livraison l ON p.Id_Pizza = l.Id_Pizza " +
                         "GROUP BY p.Id_Pizza, p.nom " +
                         "ORDER BY nb DESC " +
                         "LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nom");
            }
        }
        return "Aucune";
    }

    private String lireLivraisonPlusRapide(Connection cnx) throws SQLException {
        String requete = "SELECT l.duree FROM Livraison l WHERE l.duree IS NOT NULL ORDER BY l.duree ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return String.format(java.util.Locale.FRANCE, "%d min", rs.getInt("duree"));
            }
        }
        return "N/A";
    }

    private String lireIngredientFavori(Connection cnx) throws SQLException {
        String requete = "SELECT i.nom, COUNT(l.Id_Livraison) AS nb " +
                        "FROM Ingredient i " +
                        "JOIN contient c ON i.Id_Ingredient = c.Id_Ingredient " +
                        "JOIN Livraison l ON c.Id_Pizza = l.Id_Pizza " +
                        "GROUP BY i.Id_Ingredient, i.nom " +
                        "ORDER BY nb DESC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nom");
            }
        }
        return "Aucun";
    }

    // =========================================================================
    // EXTENSIONS ASSAINIES
    // =========================================================================

    private String extraireMoyenneCommandes(Connection cnx) throws SQLException {
        String sql = "SELECT AVG(nombre_commandes) AS moyenne_commandes FROM (SELECT COUNT(*) AS nombre_commandes FROM Livraison GROUP BY Id_Client) AS commandes_clients";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return String.format(java.util.Locale.FRANCE, "%.2f", rs.getDouble("moyenne_commandes"));
            }
        }
        return "0,00";
    }

    private String extraireJourPlusLivraisons(Connection cnx) throws SQLException {
        String sql = "SELECT date_, COUNT(*) AS nb_commandes FROM Livraison GROUP BY date_ ORDER BY nb_commandes DESC, date_ ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                java.sql.Date date = rs.getDate("date_");
                return date != null ? date.toString() : "N/A";
            }
        }
        return "N/A";
    }

    private String extrairePizzaMoinsCommandee(Connection cnx) throws SQLException {
        String sql = "SELECT p.nom, COUNT(l.Id_Livraison) AS nombre_commandes FROM Pizza p LEFT JOIN Livraison l ON p.Id_Pizza = l.Id_Pizza GROUP BY p.Id_Pizza, p.nom ORDER BY nombre_commandes ASC, p.nom ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nom");
            }
        }
        return "Aucune";
    }

    private String extraireNombreClientsAuDessusMoyenne(Connection cnx) throws SQLException {
        String sql = "SELECT COUNT(*) AS nb_clients FROM (SELECT Id_Client FROM Livraison GROUP BY Id_Client HAVING COUNT(*) > (SELECT AVG(nombre_commandes) FROM (SELECT COUNT(*) AS nombre_commandes FROM Livraison GROUP BY Id_Client) AS commandes_clients)) AS clients_superieurs";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("nb_clients"));
            }
        }
        return "0";
    }

    private String extraireNombreVehiculesJamaisUtilises(Connection cnx) throws SQLException {
        String sql = "SELECT COUNT(*) AS nb_vehicules FROM Vehicule WHERE Id_Vehicule NOT IN (SELECT DISTINCT Id_Vehicule FROM Livraison)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("nb_vehicules"));
            }
        }
        return "0";
    }

    private String extraireClientMeilleurCA(Connection cnx) throws SQLException {
        String sql = "SELECT c.nom, c.prenom, " +
                    "SUM(CASE WHEN l.gratuit = TRUE THEN 0 ELSE l.prix_pizza END) AS chiffre_affaire_client " +
                    "FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client " +
                    "GROUP BY c.Id_Client, c.nom, c.prenom " +
                    "ORDER BY chiffre_affaire_client DESC, c.nom ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("prenom") + " " + rs.getString("nom") + " — " + String.format(java.util.Locale.FRANCE, "%.2f €", rs.getDouble("chiffre_affaire_client"));
            }
        }
        return "Aucun";
    }

    private String extraireNombrePizzasUnique(Connection cnx) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Pizza";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); 
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("total"));
            }
        }
        return "0";
    }

    public List<String[]> getPireLivreur(Connection cnx) throws SQLException {
        String sql = "SELECT li.nom, li.prenom, COUNT(*) AS nombre_retards FROM Livreur li JOIN Livraison l ON li.Id_Livreur = l.Id_Livreur WHERE l.duree > 30 GROUP BY li.Id_Livreur, li.nom, li.prenom ORDER BY nombre_retards DESC LIMIT 1";
        String[] cols = {"nom", "prenom", "nombre_retards"};
        return fetchRows(cnx, sql, cols);
    }

    // CORRECTION 1 : Changement de l'alias pour éviter les conflits JDBC sur getObject()
    public List<String[]> getMenu(Connection cnx) throws SQLException {
        String sql = "SELECT p.nom, p.prix, i.nom AS ingredient_nom FROM Pizza p JOIN contient c ON p.id_pizza = c.id_pizza JOIN Ingredient i ON c.id_ingredient = i.id_ingredient ORDER BY p.nom";
        String[] cols = {"nom", "prix", "ingredient_nom"};
        return fetchRows(cnx, sql, cols);
    }

    // =========================================================================
    // MÉTHODES UTILITAIRES ET EXTRACTEUR GÉNÉRIQUE
    // =========================================================================

    private List<String[]> fetchRows(Connection cnx, String sql, String[] cols) throws SQLException {
        List<String[]> rows = new java.util.ArrayList<>();
        try (PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String[] row = new String[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    Object o = rs.getObject(cols[i]);
                    row[i] = (o == null) ? "" : o.toString();
                }
                rows.add(row);
            }
        }
        return rows;
    }

    public List<String[]> getPireLivreur() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return new java.util.ArrayList<>();
        return getPireLivreur(cnx);
    }
    
    public List<String[]> getMenu() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return new java.util.ArrayList<>();
        return getMenu(cnx);
    }

    public List<String[]> getChiffreAffaireParClient() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) return new java.util.ArrayList<>();
        
        String sql = "SELECT c.prenom, c.nom, COUNT(l.Id_Livraison) AS nombre_commandes " +
                    "FROM Client c " +
                    "JOIN Livraison l ON c.Id_Client = l.Id_Client " +
                    "GROUP BY c.Id_Client, c.nom, c.prenom " +
                    "ORDER BY nombre_commandes DESC, c.nom ASC";
        
        String[] cols = {"prenom", "nom", "nombre_commandes"};
        return fetchRows(cnx, sql, cols);
    }

    public void printAllLivraisons() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) {
            System.err.println("[StatistiquesDAO] Connexion BDD indisponible pour printAllLivraisons.");
            return;
        }
        String requete = "SELECT Id_Livraison, date_, heure, prix_pizza, gratuit, Id_Livreur, Id_Client, Id_Vehicule, id_pizza FROM Livraison ORDER BY date_, heure";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("[StatistiquesDAO] Liste des livraisons :");
            while (rs.next()) {
                int id = rs.getInt("Id_Livraison");
                String date = rs.getString("date_");
                String heure = rs.getString("heure");
                double prix = rs.getDouble("prix_pizza");
                boolean gratuit = rs.getBoolean("gratuit");
                Object idPizza = rs.getObject("id_pizza");
                System.out.println(String.format("[Livraison] id=%d date=%s heure=%s prix=%.2f gratuit=%s id_pizza=%s",
                    id, date, heure, prix, gratuit, idPizza));
            }
        }
    }

    public int supprimerLivraisonsParIds(int... ids) throws SQLException {
        if (ids == null || ids.length == 0) return 0;
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        if (cnx == null) {
            System.err.println("[StatistiquesDAO] Connexion BDD indisponible pour suppression.");
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            sb.append("?");
            if (i < ids.length - 1) sb.append(",");
        }
        String sql = "DELETE FROM Livraison WHERE Id_Livraison IN (" + sb.toString() + ")";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            for (int i = 0; i < ids.length; i++) stmt.setInt(i + 1, ids[i]);
            int count = stmt.executeUpdate();
            System.out.println("[StatistiquesDAO] Suppression effectuée : " + count + " lignes supprimées.");
            return count;
        }
    }
}