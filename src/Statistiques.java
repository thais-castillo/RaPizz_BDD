public class Statistiques {
    public final String chiffreAffairesTotal;
    public final String meilleurClient;
    public final String meilleurLivreur;

    // Ajout d'autres champs utiles pour la vue
    public final String totalCommandes;
    public final String delaiMoyen;
    public final String vehiculePlusUtilise;
    public final String pizzaStar;
    public final String livraisonPlusRapide;
    public final String ingredientFavori;

    public Statistiques(String chiffreAffairesTotal,
                       String meilleurClient,
                       String meilleurLivreur,
                       String totalCommandes,
                       String delaiMoyen,
                       String vehiculePlusUtilise,
                       String pizzaStar,
                       String livraisonPlusRapide,
                       String ingredientFavori) {
        this.chiffreAffairesTotal = chiffreAffairesTotal;
        this.meilleurClient = meilleurClient;
        this.meilleurLivreur = meilleurLivreur;
        this.totalCommandes = totalCommandes;
        this.delaiMoyen = delaiMoyen;
        this.vehiculePlusUtilise = vehiculePlusUtilise;
        this.pizzaStar = pizzaStar;
        this.livraisonPlusRapide = livraisonPlusRapide;
        this.ingredientFavori = ingredientFavori;
    }
}