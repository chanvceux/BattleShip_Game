package BattleShip.GameInterface;

import BattleShip.Picture;
import BattleShip.GameField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class FieldEnemy extends JPanel {
    private GameField gameField;

    public FieldEnemy(GameField gameField) {
        this.gameField = gameField;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(0xFFF3E7E1, true));
        gameField.repaintEnemyField(g);
    }

    public void addListener() {
        addMouseListener(new ActionMouse());
    }

    public void removeListener() {
        MouseListener[] listeners = getMouseListeners();
        for (MouseListener lis : listeners) {
            removeMouseListener(lis);
        }
    }


    private class ActionMouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            if (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) {
                gameField.sendShot(x, y);
            }
        }
    }
}
