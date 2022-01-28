package BattleShip.GameInterface;

import BattleShip.GameField;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelButtons extends JPanel {
    private GameField gameField;
    private JTextArea infoField;
    private JTextField userName;
    private JButton startGameButton;
    private JButton exitButton;
    private JButton restartGameButton;

    public PanelButtons(GameField gameField) {
        this.gameField = gameField;
        setLayout(null);
        setPreferredSize(new Dimension(400, 50));
        setBackground(new Color(0xFFDACBD0, true));

        userName = new JTextField();
        setUserName("");
        userName.setEnabled(false);
        userName.setFont(new Font("Calibri", Font.ITALIC, 10));
        userName.setBackground(new Color(0xFFDACBD0, true));
        userName.setBounds(130, 0, 70, 20);

        infoField = new JTextArea();
        setTextInfo("  РАССТАВЛЯЕМ КОРАБЛИ");
        infoField.setBorder(new MatteBorder(2, 2, 2 , 2, new Color(0xF5EDE9)));
        infoField.setOpaque(false);
        infoField.setForeground(new Color(0xFFFFFF));
        infoField.setEditable(false);
        infoField.setBackground(new Color(0xF6EFEB));
        infoField.setBounds(16, 20, 180, 20);

        startGameButton = new JButton("НАЧАТЬ ИГРУ");
        startGameButton.setFont(new Font("Calibri", Font.BOLD, 15));
        startGameButton.setForeground(new Color(0xFF80708A, true));
        startGameButton.setBounds(210, 5, 150, 40);
        startGameButton.addActionListener(new ActionButtonStartClass());

        exitButton = new JButton("СДАТЬСЯ");
        exitButton.setFont(new Font("Calibri", Font.BOLD, 15));
        exitButton.setForeground(new Color(0xFF80708A, true));
        exitButton.setBounds(370, 5, 150, 40);
        exitButton.addActionListener(new ActionButtonDisconnect());
        exitButton.setEnabled(false);

        restartGameButton = new JButton("ИГРАТЬ ЕЩЁ");
        restartGameButton.setFont(new Font("Calibri", Font.BOLD, 15));
        restartGameButton.setBounds(530, 5, 150, 40);
        restartGameButton.setForeground(new Color(0xFF80708A, true));
        restartGameButton.setEnabled(false);
        restartGameButton.addActionListener(new ActionButtonRestartGame());

        add(infoField);
        add(userName);
        add(startGameButton);
        add(exitButton);
        add(restartGameButton);
    }

    public JButton getRestartGameButton() {
        return restartGameButton;
    }

    public JButton getStartGameButton() {
        return startGameButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public void setTextInfo(String text) {
        infoField.setText(text.toUpperCase());
    }

    public void setUserName(String text) {
        userName.setText(text);
    }

    private class ActionButtonStartClass implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameField.startGame();
        }
    }

    private class ActionButtonDisconnect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameField.disconnectGameRoom();
            exitButton.setEnabled(false);
            restartGameButton.setEnabled(true);
        }
    }
    private class ActionButtonRestartGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameField.init();
        }
    }
}
