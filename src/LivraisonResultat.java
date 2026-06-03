public class LivraisonResultat {
    private final boolean succes;
    private final boolean gratuit;
    private final double soldeApres;
    private final String erreur;

    private LivraisonResultat(boolean succes, boolean gratuit, double soldeApres, String erreur) {
        this.succes = succes;
        this.gratuit = gratuit;
        this.soldeApres = soldeApres;
        this.erreur = erreur;
    }

    public static LivraisonResultat succes(boolean gratuit, double soldeApres) {
        return new LivraisonResultat(true, gratuit, soldeApres, null);
    }

    public static LivraisonResultat erreur(String message) {
        return new LivraisonResultat(false, false, 0.0, message);
    }

    public boolean isSucces() {
        return succes;
    }

    public boolean isGratuit() {
        return gratuit;
    }

    public double getSoldeApres() {
        return soldeApres;
    }

    public String getErreur() {
        return erreur;
    }
}
