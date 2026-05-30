import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDeDonnee {
    private static BaseDeDonnee instance = null;
    private Connection cnx;

    private BaseDeDonnee() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            this.cnx = DriverManager.getConnection("jdbc:mariadb://dwarves.iut-fbleau.fr/bribant", "bribant", "Chocolat");
            System.out.println("[BDD] Connexion réussie à dwarves !");
        } catch (ClassNotFoundException e) {
            System.err.println("Mauvais classpath : le driver MariaDB est manquant.");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public static BaseDeDonnee getInstance() {
        try {
            if (instance == null || instance.getDatabase() == null || instance.getDatabase().isClosed()) {
                instance = new BaseDeDonnee();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return instance;
    }

    public Connection getDatabase() {
        return this.cnx;
    }

    public void closeDatabase() {
        try {
            if (this.cnx != null && !this.cnx.isClosed()) {
                this.cnx.close();
                System.out.println("[BDD] Connexion fermée proprement.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}