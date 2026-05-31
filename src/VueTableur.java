import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VueTableur extends JFrame {
    public VueTableur(String title, String[] columns, java.util.List<String[]> rows) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        if (rows != null) {
            for (String[] r : rows) model.addRow(r);
        }
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        setVisible(true);
    }
}
