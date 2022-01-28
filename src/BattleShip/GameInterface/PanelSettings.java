package BattleShip.GameInterface;

import BattleShip.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class PanelSettings extends JPanel {
    private GameField gameField;
    private JPanel panelRadio;
    private JPanel panelPlacement;
    private JRadioButton oneDeck;
    private JRadioButton twoDeck;
    private JRadioButton threeDeck;
    private JRadioButton fourDeck;
    private JRadioButton vertical;
    private JRadioButton horizontal;
    private JButton clearField;
    private ButtonGroup groupDeck;
    private ButtonGroup groupPlacement;
    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);


    public PanelSettings(GameField gameField) {
        this.gameField = gameField;
        setLayout(null);
        this.setPreferredSize(new Dimension(255, 400));
        this.setBackground(new Color(0xFFDACBD0, true));

        panelRadio = new JPanel();
        panelRadio.setBackground(new Color(0xFFDACBD0, true));
        panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));
        panelRadio.setBounds(13, 190, 230, 130);

        panelPlacement = new JPanel();
        panelPlacement.setLayout(new BoxLayout(panelPlacement, BoxLayout.Y_AXIS));
        panelPlacement.setBackground(new Color(0xFFDACBD0, true));
        panelPlacement.setBounds(13, 330, 230, 80);

        clearField = new JButton("ОЧИСТИТЬ ПОЛЕ");
        clearField.setFont(new Font("Calibri", Font.BOLD, 12));
        clearField.setForeground(new Color(0x77706D));
        clearField.setBounds(13, 410, 230, 30);
        clearField.addActionListener(new ActionClearField());

        panelRadio.setBorder(new TitledBorder(new MatteBorder(2, 2, 2, 2, new Color(0xF3E7E1)),
                "КОЛИЧЕСТВО ПАЛУБ", TitledBorder.LEADING, TitledBorder.TOP,
                new Font("Calibri", Font.BOLD, 12), new Color(0xF3E7E1)));

        panelPlacement.setBorder((new TitledBorder(new MatteBorder(2, 2, 2, 2, new Color(0xF3E7E1)),
                "ОРИЕНТАЦИЯ КОРАБЛЯ", TitledBorder.LEADING, TitledBorder.TOP,
                new Font("Calibri", Font.BOLD, 12), new Color(0xF3E7E1))));

        oneDeck = new JRadioButton();
        oneDeck.setSelected(true);
        oneDeck.setFont(new Font("Calibri", Font.BOLD, 12));
        oneDeck.setForeground(new Color(0xFF80708A, true));
        oneDeck.setCursor(cursor);
        setCountOne(4);

        twoDeck = new JRadioButton();
        twoDeck.setFont(new Font("Calibri", Font.BOLD, 12));
        twoDeck.setForeground(new Color(0xFF80708A, true));
        twoDeck.setCursor(cursor);
        setCountTwo(3);

        threeDeck = new JRadioButton();
        threeDeck.setFont(new Font("Calibri", Font.BOLD, 12));
        threeDeck.setForeground(new Color(0xFF80708A, true));
        threeDeck.setCursor(cursor);
        setCountThree(2);

        fourDeck = new JRadioButton();
        fourDeck.setFont(new Font("Calibri", Font.BOLD, 12));
        fourDeck.setForeground(new Color(0xFF80708A, true));
        fourDeck.setCursor(cursor);
        setCountFour(1);

        vertical = new JRadioButton("ВЕРТИКАЛЬНАЯ");
        vertical.setFont(new Font("Calibri", Font.BOLD, 12));
        vertical.setSelected(true);
        vertical.setForeground(new Color(0xFF80708A, true));
        vertical.setCursor(cursor);

        horizontal = new JRadioButton("ГОРИЗОНТАЛЬНАЯ");
        horizontal.setFont(new Font("Calibri", Font.BOLD, 12));
        horizontal.setForeground(new Color(0xFF80708A, true));
        horizontal.setCursor(cursor);

        groupDeck = new ButtonGroup();
        groupPlacement = new ButtonGroup();

        panelRadio.add(oneDeck);
        panelRadio.add(twoDeck);
        panelRadio.add(threeDeck);
        panelRadio.add(fourDeck);

        panelPlacement.add(vertical);
        panelPlacement.add(horizontal);
        add(panelRadio);
        add(panelPlacement);
        add(clearField);
        groupDeck.add(oneDeck);
        groupDeck.add(twoDeck);
        groupDeck.add(threeDeck);
        groupDeck.add(fourDeck);
        groupPlacement.add(vertical);
        groupPlacement.add(horizontal);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Picture.getImage(Picture.INFO.name()), 2, 0, this);
    }

    public void setCountOne(int count) {
        String text = "ОДНА ПАЛУБА: [" + count + "]";
        oneDeck.setText(text);
    }

    public void setCountTwo(int count) {
        String text = "ДВЕ ПАЛУБЫ: [" + count + "]";
        twoDeck.setText(text);
    }

    public void setCountThree(int count) {
        String text = "ТРИ ПАЛУБЫ: [" + count + "]";
        threeDeck.setText(text);
    }

    public void setCountFour(int count) {
        String text = "ЧЕТЫРЕ ПАЛУБЫ: [" + count + "]";
        fourDeck.setText(text);
    }

    public int getCountDeck() {
        if (oneDeck.isSelected()) return 1;
        else if (twoDeck.isSelected()) return 2;
        else if (threeDeck.isSelected()) return 3;
        else if (fourDeck.isSelected()) return 4;
        else return 0;
    }

    public int getPlacement() {
        if (vertical.isSelected()) return 1;
        else if (horizontal.isSelected()) return 2;
        else return 0;
    }

    private class ActionClearField implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameField.loadEmptyMyField();
        }
    }
}


