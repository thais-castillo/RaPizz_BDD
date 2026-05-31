import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatistiquesDAO {

    public Statistiques chargerStatistiques() {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();

        if (cnx == null) {
            System.err.println("[StatistiquesDAO] Connexion BDD indisponible.");
            return new Statistiques("N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A");
        }

        try {
            String chiffreAffairesTotal = lireChiffreAffairesTotal(cnx);
            String meilleurClient = lireMeilleurClient(cnx);
            String meilleurLivreur = lireMeilleurLivreur(cnx);
            String totalCommandes = lireTotalCommandes(cnx);
            String delaiMoyen = lireDelaiMoyen(cnx);
            String vehiculePlusUtilise = lireVehiculePlusUtilise(cnx);
            String pizzaStar = lirePizzaStar(cnx);
            String livraisonPlusRapide = lireLivraisonPlusRapide(cnx);
            String ingredientFavori = lireIngredientFavori(cnx);

            return new Statistiques(
                chiffreAffairesTotal,
                meilleurClient,
                meilleurLivreur,
                totalCommandes,
                delaiMoyen,
                vehiculePlusUtilise,
                pizzaStar,
                livraisonPlusRapide,
                ingredientFavori
            );
        } catch (SQLException e) {
            System.err.println("[StatistiquesDAO] Erreur SQL : " + e.getMessage());
            return new Statistiques("N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A");
        }
    }

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
                System.out.println(String.format(java.util.Locale.FRANCE,
                    "[StatistiquesDAO] CA brut=%.2f €, gratuits=%.2f €, net=%.2f €", brut, gratuits, net));
                // On renvoie maintenant le net (brut - gratuits) pour correspondre au chiffre réellement encaissé.
                return String.format(java.util.Locale.FRANCE, "%.2f €", net);
            }
        }
        return "N/A";
    }

private String lireMeilleurClient(Connection cnx) throws SQLException {
    // Tri stable : si plusieurs clients ont le même nombre de commandes,
    // on utilise l'Id_Client (ou nom/prenom) comme critère secondaire pour garantir
    // un résultat déterministe à chaque exécution.
    String requete = "SELECT c.nom, c.prenom, COUNT(*) AS nombre_commandes " +
                     "FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client " +
                     "GROUP BY c.Id_Client, c.nom, c.prenom " +
                     "ORDER BY nombre_commandes DESC, c.Id_Client ASC " +
                     "LIMIT 1";

    try (PreparedStatement stmt = cnx.prepareStatement(requete);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            // Retourne uniquement le nom/prénom sans le nombre entre parenthèses
            return rs.getString("prenom") + " " + rs.getString("nom");
        }
    }
    return "N/A";
}

    private String lireMeilleurLivreur(Connection cnx) throws SQLException {
    // Même principe pour le livreur : ajout d'un critère secondaire pour stabilité
    String requete = "SELECT li.nom, li.prenom, COUNT(*) AS nombre_livraisons " +
                     "FROM Livreur li JOIN Livraison l ON li.Id_Livreur = l.Id_Livreur " +
                     "GROUP BY li.Id_Livreur, li.nom, li.prenom " +
                     "ORDER BY nombre_livraisons DESC, li.Id_Livreur ASC " +
                     "LIMIT 1";

    try (PreparedStatement stmt = cnx.prepareStatement(requete);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            // Retourne uniquement le nom/prénom sans le compteur
            return rs.getString("prenom") + " " + rs.getString("nom");
        }
    }
    return "N/A";
}

    private String lireTotalCommandes(Connection cnx) throws SQLException {
        String requete = "SELECT COUNT(*) AS total FROM Livraison";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("total"));
            }
        }
        return "N/A";
    }

    private String lireDelaiMoyen(Connection cnx) throws SQLException {
        String requete = "SELECT AVG(duree) AS avg_duree FROM Livraison";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double avg = rs.getDouble("avg_duree");
                if (rs.wasNull()) return "N/A";
                return String.format(java.util.Locale.FRANCE, "%.1f min", avg);
            }
        }
        return "N/A";
    }

    private String lireVehiculePlusUtilise(Connection cnx) throws SQLException {
        String requete = "SELECT v.type, v.immatricule, COUNT(*) AS nb " +
                         "FROM Vehicule v, Livraison l " +
                         "WHERE v.Id_Vehicule = l.Id_Vehicule " +
                         "GROUP BY v.Id_Vehicule, v.type, v.immatricule " +
                         "ORDER BY nb DESC " +
                         "LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("type") + " (" + rs.getString("immatricule") + ")";
            }
        }
        return "N/A";
    }

    private String lirePizzaStar(Connection cnx) throws SQLException {
        String requete = "SELECT p.nom, COUNT(*) AS nb " +
                         "FROM Pizza p, Livraison l " +
                         "WHERE p.Id_Pizza = l.Id_Pizza " +
                         "GROUP BY p.Id_Pizza, p.nom " +
                         "ORDER BY nb DESC " +
                         "LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nom");
            }
        }
        return "N/A";
    }

    private String lireLivraisonPlusRapide(Connection cnx) throws SQLException {
        String requete = "SELECT l.date_ AS date_liv, l.heure AS heure_liv, l.duree, p.nom AS nom_pizza " +
                         "FROM Livraison l LEFT JOIN Pizza p ON l.Id_Pizza = p.Id_Pizza " +
                         "ORDER BY l.duree ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int duree = rs.getInt("duree");
                // Retourner uniquement la durée en minutes (ex: "14 min")
                return String.format(java.util.Locale.FRANCE, "%d min", duree);
            }
        }
        return "N/A";
    }

    private String lireIngredientFavori(Connection cnx) throws SQLException {
        String requete = "SELECT i.nom, COUNT(*) AS nb " +
                         "FROM Ingredient i, contient c, Pizza p, Livraison l " +
                         "WHERE i.Id_Ingredient = c.Id_Ingredient " +
                         "AND c.Id_Pizza = p.Id_Pizza " +
                         "AND p.Id_Pizza = l.Id_Pizza " +
                         "GROUP BY i.Id_Ingredient, i.nom " +
                         "ORDER BY nb DESC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(requete);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Retourner uniquement le nom de l'ingrédient sans compteur
                return rs.getString("nom");
            }
        }
        return "N/A";
    }

    // Méthode utilitaire publique pour debug : affiche toutes les livraisons
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

    // Supprime les livraisons dont les IDs sont fournis. Retourne le nombre de lignes supprimées.
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

    // Méthode générique utilitaire : exécute une requête et retourne les résultats sous forme de liste de lignes (chaque ligne = tableau de String)
    private java.util.List<String[]> fetchRows(String sql, String[] cols) throws SQLException {
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
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

    public java.util.List<String[]> getMenu() throws SQLException {
        String sql = "SELECT p.nom, p.prix, i.nom AS ingredient FROM Pizza p JOIN contient c ON p.id_pizza = c.id_pizza JOIN Ingredient i ON c.id_ingredient = i.id_ingredient ORDER BY p.nom";
        String[] cols = {"nom", "prix", "ingredient"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getLivraisonsTable() throws SQLException {
        String sql = "SELECT Id_Livraison, date_, heure, prix_pizza, gratuit, duree, Id_Livreur, Id_Client, Id_Vehicule, id_pizza FROM Livraison ORDER BY date_ DESC, heure DESC";
        String[] cols = {"Id_Livraison", "date_", "heure", "prix_pizza", "gratuit", "duree", "Id_Livreur", "Id_Client", "Id_Vehicule", "id_pizza"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getClientsCommandes() throws SQLException {
        String sql = "SELECT c.prenom, c.nom, COUNT(*) AS nombre_commandes FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client GROUP BY c.Id_Client, c.prenom, c.nom ORDER BY nombre_commandes DESC";
        String[] cols = {"prenom", "nom", "nombre_commandes"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getClientsSupMoyenne() throws SQLException {
        String sql = "SELECT c.nom, c.prenom, COUNT(*) AS nombre_commandes FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client GROUP BY c.Id_Client, c.nom, c.prenom HAVING COUNT(*) > (SELECT AVG(nombre_commandes) FROM (SELECT COUNT(*) AS nombre_commandes FROM Livraison GROUP BY Id_Client) AS commandes_clients)";
        String[] cols = {"nom", "prenom", "nombre_commandes"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getVehiculesNeverUsed() throws SQLException {
        String sql = "SELECT Id_Vehicule, type, immatricule FROM Vehicule WHERE Id_Vehicule NOT IN (SELECT DISTINCT Id_Vehicule FROM Livraison)";
        String[] cols = {"Id_Vehicule", "type", "immatricule"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getPireLivreur() throws SQLException {
        String sql = "SELECT li.nom, li.prenom, COUNT(*) AS nombre_retards FROM Livreur li JOIN Livraison l ON li.Id_Livreur = l.Id_Livreur WHERE l.duree > 30 GROUP BY li.Id_Livreur, li.nom, li.prenom ORDER BY nombre_retards DESC LIMIT 1";
        String[] cols = {"nom", "prenom", "nombre_retards"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getChiffreAffaireParClient() throws SQLException {
        String sql = "SELECT c.nom, c.prenom, SUM(l.prix_pizza) AS chiffre_affaire_client FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client GROUP BY c.Id_Client, c.nom, c.prenom ORDER BY chiffre_affaire_client DESC, c.nom ASC, c.prenom ASC";
        String[] cols = {"nom", "prenom", "chiffre_affaire_client"};
        return fetchRows(sql, cols);
    }

    public java.util.List<String[]> getPizzaStar() throws SQLException {
        String sql = "SELECT p.nom, COUNT(*) AS nombre_commandes FROM Pizza p JOIN Livraison l ON p.Id_Pizza = l.Id_Pizza GROUP BY p.Id_Pizza, p.nom ORDER BY nombre_commandes DESC LIMIT 1";
        String[] cols = {"nom", "nombre_commandes"};
        return fetchRows(sql, cols);
    }

    public String lireMoyenneCommandes() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        String sql = "SELECT AVG(nombre_commandes) AS moyenne_commandes FROM (SELECT COUNT(*) AS nombre_commandes FROM Livraison GROUP BY Id_Client) AS commandes_clients";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double moyenne = rs.getDouble("moyenne_commandes");
                return String.format(java.util.Locale.FRANCE, "%.2f", moyenne);
            }
        }
        return "0,00";
    }

    public String lireJourPlusLivraisons() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        String sql = "SELECT date_, COUNT(*) AS nb_commandes FROM Livraison GROUP BY date_ ORDER BY nb_commandes DESC, date_ ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                java.sql.Date date = rs.getDate("date_");
                return date.toString();
            }
        }
        return "N/A";
    }

    public String lirePizzaMoinsCommandee() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        String sql = "SELECT p.nom, COUNT(*) AS nombre_commandes FROM Pizza p JOIN Livraison l ON p.Id_Pizza = l.Id_Pizza GROUP BY p.Id_Pizza, p.nom ORDER BY nombre_commandes ASC, p.nom ASC LIMIT 1";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nom");
            }
        }
        return "N/A";
    }

    public String lireNombreClientsAuDessusMoyenne() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        String sql = "SELECT COUNT(*) AS nb_clients FROM (SELECT c.Id_Client FROM Client c JOIN Livraison l ON c.Id_Client = l.Id_Client GROUP BY c.Id_Client HAVING COUNT(*) > (SELECT AVG(nombre_commandes) FROM (SELECT COUNT(*) AS nombre_commandes FROM Livraison GROUP BY Id_Client) AS commandes_clients)) AS clients_superieurs";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("nb_clients"));
            }
        }
        return "0";
    }

    public String lireNombreVehiculesJamaisUtilises() throws SQLException {
        Connection cnx = BaseDeDonnee.getInstance().getDatabase();
        String sql = "SELECT COUNT(*) AS nb_vehicules FROM Vehicule WHERE Id_Vehicule NOT IN (SELECT DISTINCT Id_Vehicule FROM Livraison)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Integer.toString(rs.getInt("nb_vehicules"));
            }
        }
        return "0";
    }
}