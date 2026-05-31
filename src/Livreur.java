public class Livreur {
    private int idLivreur;
    private String nom;
    private String prenom;

    public Livreur(int idLivreur, String nom, String prenom) {
        this.idLivreur = idLivreur;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getIdLivreur() { return idLivreur; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }

    @Override
    public String toString() {
        return this.prenom + " " + this.nom.toUpperCase();
    }
}