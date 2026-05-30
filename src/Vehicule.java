public class Vehicule {
    private int idVehicule;
    private String type;
    private String immatricule;

    public Vehicule(int idVehicule, String type, String immatricule) {
        this.idVehicule = idVehicule;
        this.type = type;
        this.immatricule = immatricule;
    }

    public int getIdVehicule() { return idVehicule; }
    public String getType() { return type; }
    public String getImmatricule() { return immatricule; }

    @Override
    public String toString() {
        return this.type + " [" + this.immatricule + "]";
    }
}