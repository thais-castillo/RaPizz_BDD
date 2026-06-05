package Model;
public class Pizza {
    private int idPizza;
    private String nom;
    private double prix;

    public Pizza(int idPizza, String nom, double prix) {
        this.idPizza = idPizza;
        this.nom = nom;
        this.prix = prix;
    }

    public int getIdPizza() { return idPizza; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }

    @Override
    public String toString() {
        return this.nom + " (" + String.format("%.2f", this.prix) + " €)";
    }
}