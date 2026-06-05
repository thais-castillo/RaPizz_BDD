package Model;
public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private double solde;
    private int bonification;

    public Client(int idClient, String nom, String prenom, double solde, int bonification) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.solde = solde;
        this.bonification = bonification;
    }

    public int getIdClient() { return idClient; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public double getSolde() { return solde; }
    public int getBonification() { return bonification; }

    @Override
    public String toString() {
        return this.prenom + " " + this.nom.toUpperCase() + " (Solde : " + String.format("%.2f", this.solde) + " €)";
    }
}