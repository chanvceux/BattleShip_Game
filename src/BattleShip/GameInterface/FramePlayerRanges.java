package BattleShip.GameInterface;

import BattleShip.Connection.DBHandler;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class FramePlayerRanges extends JFrame {

    public FramePlayerRanges() {

        setTitle("РЕЙТИНГ ИГРОКОВ");

        String [][] playersGames = DBHandler.getAllPlayers();

        String[] columnNames = {"Место в рейтинге", "Игрок", "Количество игр", "Количество побед"};

        JTable table = new JTable(playersGames, columnNames);
        JTableHeader header = table.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new FramePlayersGames.HeaderRenderer());
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
}
