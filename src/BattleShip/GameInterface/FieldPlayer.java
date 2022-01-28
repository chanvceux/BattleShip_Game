package BattleShip.GameInterface;

import BattleShip.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FieldPlayer extends JPanel {
    private GameField gameField;
    private PanelSettings panelSettings;

    public void setPanelSettings(PanelSettings panelSettings) {
        this.panelSettings = panelSettings;
    }

    public FieldPlayer(GameField gameField) {
        this.setBackground(new Color(0xFFF3E7E1, true));
        this.gameField = gameField;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
        this.addMouseListener(new ActionMouse());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameField.repaintMyField(g);
    }

    private class ActionMouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int countDeck = panelSettings.getCountDeck();
            int placement = panelSettings.getPlacement();
            Ship ship;
            Ship removedShip;
            if (e.getButton() == MouseEvent.BUTTON1 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE)) {
                    switch (placement) {
                        case 1: {
                            ship = new Ship(countDeck, false);
                            ship.createVerticalShip(x, y);
                            gameField.addShip(ship);
                            break;
                        }
                        case 2: {
                            ship = new Ship(countDeck, true);
                            ship.createHorizontalShip(x, y);
                            gameField.addShip(ship);
                            break;
                        }
                    }
            } else if (e.getButton() == MouseEvent.BUTTON3 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) &&
                    (removedShip = gameField.removeShip(x, y)) != null) {
                gameField.changeCountShipOnChoosePanel(removedShip.getCountDeck());
            }
            repaint();
            gameField.changeCountShipOnChoosePanel(countDeck);
        }
    }
}
