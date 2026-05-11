import javax.swing.*;
import java.awt.*;

public class VueMenu extends JFrame {
    private JButton btnCommander;
    private JButton btnQuitter;

    public VueMenu() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);

        this.setTitle("Menu Principal");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        btnCommander = new JButton("Commander");
        btnQuitter = new JButton("Quitter");

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.add(btnCommander);
        panel.add(btnQuitter);

        JLabel titre = new JLabel("RaPizz");
        titre.setFont(new Font("SansSerif", Font.BOLD, 70));
        titre.setForeground(new Color(0, 51, 102));

        add(panel);
        this.setVisible(true);
    }

}
