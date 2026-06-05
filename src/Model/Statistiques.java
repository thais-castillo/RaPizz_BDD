package Model;
public class Statistiques {
    private final String chiffreAffairesTotal;
    private final String meilleurClient;
    private final String meilleurLivreur;
    private final String totalCommandes;
    private final String delaiMoyen;
    private final String vehiculePlusUtilise;
    private final String pizzaStar;
    private final String livraisonPlusRapide;
    private final String ingredientFavori;
    
    private final String moyenneCommandes;
    private final String clientsAuDessusMoyenne;
    private final String clientMeilleurCA;
    private final String jourPlusCharge;
    private final String pireLivreurNom;
    private final String pireLivreurRetards;
    private final String vehiculesJamaisUtilises;
    private final String pizzaMoinsCommandee;
    private final String nombrePizzasMenu;

    public Statistiques(String chiffreAffairesTotal, String meilleurClient, String meilleurLivreur,
                        String totalCommandes, String delaiMoyen, String vehiculePlusUtilise,
                        String pizzaStar, String livraisonPlusRapide, String ingredientFavori,
                        String moyenneCommandes, String clientsAuDessusMoyenne, String clientMeilleurCA,
                        String jourPlusCharge, String pireLivreurNom, String pireLivreurRetards,
                        String vehiculesJamaisUtilises, String pizzaMoinsCommandee, String nombrePizzasMenu) {
        this.chiffreAffairesTotal = chiffreAffairesTotal;
        this.meilleurClient = meilleurClient;
        this.meilleurLivreur = meilleurLivreur;
        this.totalCommandes = totalCommandes;
        this.delaiMoyen = delaiMoyen;
        this.vehiculePlusUtilise = vehiculePlusUtilise;
        this.pizzaStar = pizzaStar;
        this.livraisonPlusRapide = livraisonPlusRapide;
        this.ingredientFavori = ingredientFavori;
        this.moyenneCommandes = moyenneCommandes;
        this.clientsAuDessusMoyenne = clientsAuDessusMoyenne;
        this.clientMeilleurCA = clientMeilleurCA;
        this.jourPlusCharge = jourPlusCharge;
        this.pireLivreurNom = pireLivreurNom;
        this.pireLivreurRetards = pireLivreurRetards;
        this.vehiculesJamaisUtilises = vehiculesJamaisUtilises;
        this.pizzaMoinsCommandee = pizzaMoinsCommandee;
        this.nombrePizzasMenu = nombrePizzasMenu;
    }

    public String getChiffreAffairesTotal() { return chiffreAffairesTotal; }
    public String getMeilleurClient() { return meilleurClient; }
    public String getMeilleurLivreur() { return meilleurLivreur; }
    public String getTotalCommandes() { return totalCommandes; }
    public String getDelaiMoyen() { return delaiMoyen; }
    public String getVehiculePlusUtilise() { return vehiculePlusUtilise; }
    public String getPizzaStar() { return pizzaStar; }
    public String getLivraisonPlusRapide() { return livraisonPlusRapide; }
    public String getIngredientFavori() { return ingredientFavori; }
    public String getMoyenneCommandes() { return moyenneCommandes; }
    public String getClientsAuDessusMoyenne() { return clientsAuDessusMoyenne; }
    public String getClientMeilleurCA() { return clientMeilleurCA; }
    public String getJourPlusCharge() { return jourPlusCharge; }
    public String getPireLivreurNom() { return pireLivreurNom; }
    public String getPireLivreurRetards() { return pireLivreurRetards; }
    public String getVehiculesJamaisUtilises() { return vehiculesJamaisUtilises; }
    public String getPizzaMoinsCommandee() { return pizzaMoinsCommandee; }
    public String getNombrePizzasMenu() { return nombrePizzasMenu; }
}