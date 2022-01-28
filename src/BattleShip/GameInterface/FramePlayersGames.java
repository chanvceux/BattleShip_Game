package BattleShip.GameInterface;

import BattleShip.Connection.DBHandler;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;

public class FramePlayersGames extends JFrame {


    public FramePlayersGames() {

        setTitle("Результаты игр");

        String [][] inf;
        inf = DBHandler.getAllGames();

        String[] columnNames = {"Первый игрок", "Второй игрок", "Победитель"};

        JTable table = new JTable(inf, columnNames);
        JTableHeader header = table.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new HeaderRenderer());
        getContentPane().add(new JScrollPane(table));

        table.setDefaultEditor(Object.class, null);
        table.setFocusable(false);

        table.setBackground(new Color(0xF3E7E1));
        table.setForeground(new Color(0x7E546C));
        table.setSelectionForeground(new Color(0xF3E7E1));
        table.setSelectionBackground(new Color(0x7E546C));

        setPreferredSize(new Dimension(500, 210));
        setLocationRelativeTo(null);
        setVisible(true);
        pack();

    }


    static class HeaderRenderer extends DefaultTableCellRenderer {
        Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(border);
            label.setBackground(new Color(0x7E546C));
            label.setForeground(new Color(0xF3E7E1));
            return label;
        }
    }
}
