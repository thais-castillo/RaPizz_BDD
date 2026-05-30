public class Main {
    public static void main(String[] args) {
        VueMenu vue = new VueMenu();
        new ControleurMenu(vue);
        vue.setVisible(true);
    }
}
