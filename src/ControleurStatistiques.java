import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurStatistiques {
    private final VueStatistiques vue;

    public ControleurStatistiques(VueStatistiques vue) {
        this.vue = vue;
        this.vue.addRetourListener(new ActionRetour());
    }

    private class ActionRetour implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vue.dispose();

            VueMenu vueMenu = new VueMenu();
            new ControleurMenu(vueMenu);
            vueMenu.setVisible(true);
        }
    }
}