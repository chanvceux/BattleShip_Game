package BattleShip.GameInterface;

import BattleShip.Controller;
import BattleShip.Model;
import BattleShip.GameField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameWelcome extends JFrame {

    Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    GameField gameField = new GameField();
    Model model = new Model();
    Controller controller = new Controller(gameField, model);

    JButton rangsButton;
    JButton newGame;
    JButton playersGames;


    public FrameWelcome() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("BattleShip Game");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 545);
        setResizable(false);
        setLocationRelativeTo(null);

        rangsButton = new JButton("РЕЙТИНГ ИГРОКОВ");
        rangsButton.setFont(new Font("Athelas", Font.BOLD, 20));
        rangsButton.setForeground(new Color(0x756681));
        rangsButton.setOpaque(false);
        rangsButton.setBounds(175, 270, 250, 40);
        rangsButton.addActionListener(new OpenRangesFrame());
        rangsButton.setCursor(handCursor);

        newGame = new JButton("НОВАЯ ИГРА");
        newGame.setFont(new Font("Athelas", Font.BOLD, 20));
        newGame.setForeground(new Color(0x756681));
        newGame.setOpaque(false);
        newGame.setBounds(175, 220, 250, 40);
        newGame.addActionListener(new OpenNewGame());
        newGame.setCursor(handCursor);

        playersGames = new JButton("РЕЗУЛЬТАТЫ ИГР");
        playersGames.setFont(new Font("Athelas", Font.BOLD, 20));
        playersGames.setForeground(new Color(0x756681));
        playersGames.setOpaque(false);
        playersGames.setBounds(175, 320, 250, 40);
        playersGames.addActionListener(new OpenResultsOfGames());
        playersGames.setCursor(handCursor);

        add(rangsButton);
        add(newGame);
        add(playersGames);

        add(new ImageComponent());

        setVisible(true);

    }

    static class ImageComponent extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image image = new ImageIcon("res/img/battleship.png").getImage();
            g.drawImage(image, 0, 0, this);
        }
    }

    private class OpenNewGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameField.setController(controller);
            gameField.setModel(model);
            gameField.init();
            setVisible(false);
        }
    }

    private class OpenRangesFrame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            FramePlayerRanges framePlayerRanges = new FramePlayerRanges();
        }
    }

    private class OpenResultsOfGames implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            FramePlayersGames framePlayersGames = new FramePlayersGames();
        }
    }
}
