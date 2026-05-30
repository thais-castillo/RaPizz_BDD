public class Pizza {
    private int idPizza;
    private String nom;
    private double prix;

    // Constructeur complet (utilisé par le DAO)
    public Pizza(int idPizza, String nom, double prix) {
        this.idPizza = idPizza;
        this.nom = nom;
        this.prix = prix;
    }

    // Getters
    public int getIdPizza() { return idPizza; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }

    // Indispensable pour l'affichage propre dans le JComboBox de l'interface
    @Override
    public String toString() {
        return this.nom + " (" + String.format("%.2f", this.prix) + " €)";
    }
}