package BattleShip;

import java.util.ArrayList;
import java.util.List;

public class Model {

    /** Поле игрока */
    private Box[][] myField = new Box[Picture.COLUMNS][Picture.ROWS];
    /** Поле противника */
    private Box[][] enemyField = new Box[Picture.COLUMNS][Picture.ROWS];
    /** Список однопалубных кораблей */
    private List<Ship> shipsOneDeck = new ArrayList<>();
    /** Список двухпалубных кораблей */
    private List<Ship> shipsTwoDeck = new ArrayList<>();
    /** Список трёхпалубных кораблей */
    private List<Ship> shipsThreeDeck = new ArrayList<>();
    /** Список четырёхпалубных кораблей */
    private List<Ship> shipsFourDeck = new ArrayList<>();
    /** Список всех кораблей противника */
    private List<Ship> allShipsOfEnemy = new ArrayList<>();


    public void setAllShipsOfEnemy(List<Ship> allShipsOfEnemy) {
        this.allShipsOfEnemy = allShipsOfEnemy;
    }

    /**
     * @return список всех кораблей игрока
     */
    public List<Ship> getAllShips() {
        List<Ship> allBoxesOfShips = new ArrayList<>();
        allBoxesOfShips.addAll(shipsFourDeck);
        allBoxesOfShips.addAll(shipsThreeDeck);
        allBoxesOfShips.addAll(shipsTwoDeck);
        allBoxesOfShips.addAll(shipsOneDeck);
        return allBoxesOfShips;
    }

    /**
     * @return список всех координат кораблей
     */
    public List<Box> getAllBoxesOfShips() {
        List<Box> allBoxes = new ArrayList<>();
        List<Ship> allShips = getAllShips();
        for (Ship ship : allShips) {
            allBoxes.addAll(ship.getBoxesOfShip());
        }
        return allBoxes;
    }

    /**
     * @return список однопалубных кораблей
     */
    public List<Ship> getShipsOneDeck() {
        return shipsOneDeck;
    }

    /**
     * @return список двухпалубных кораблей
     */
    public List<Ship> getShipsTwoDeck() {
        return shipsTwoDeck;
    }

    /**
     * @return список трехпалубных кораблей
     */
    public List<Ship> getShipsThreeDeck() {
        return shipsThreeDeck;
    }

    /**
     * @return список четырехпалубных кораблей
     */
    public List<Ship> getShipsFourDeck() {
        return shipsFourDeck;
    }

    /**
     * @return игровое поле пользователя
     */
    public Box[][] getMyField() {
        return myField;
    }

    /**
     * Устанавливает поле игрока
     */
    public void setMyField(Box[][] myField) {
        this.myField = myField;
    }

    /**
     * @return игровое поле противника
     */
    public Box[][] getEnemyField() {
        return enemyField;
    }

    /**
     * Устанавливает поле противника
     */
    public void setEnemyField(Box[][] enemyField) {
        this.enemyField = enemyField;
    }

    /**
     * Добавляет боксы на игровое поле
     */
    public void addBoxInField(Box[][] fieldBox, Box box) {
        int i = box.getX() / Picture.IMAGE_SIZE;
        int j = box.getY() / Picture.IMAGE_SIZE;
        fieldBox[i][j] = box;
    }

    /**
     * @return получает боксы игрового поля
     */
    public Box getBox(Box[][] field, int x, int y) {
        int i = x / Picture.IMAGE_SIZE;
        int j = y / Picture.IMAGE_SIZE;
        int length = field.length - 1;
        if (!(i > length || j > length)) {
            return field[i][j];
        }
        return null;
    }


    /**
     * В зависимости от того, какие координаты переданы, вычисляются соседние боксы – если корабль
     * подбит, необходимо установить им статус открытых – для того, чтобы в дальнейшем поместить в них
     * изображения точек.
     */
    public void openBoxAroundBoxOfShipEnemy(int x, int y, boolean isHorizontalPlacement) {
        if (isHorizontalPlacement) {
            Box boxUp = getBox(enemyField, x, y - Picture.IMAGE_SIZE);
            if (boxUp != null) boxUp.setOpen(true);
            Box boxDown = getBox(enemyField, x, y + Picture.IMAGE_SIZE);
            if (boxDown != null) boxDown.setOpen(true);
        }
        else {
            Box boxLeft = getBox(enemyField, x - Picture.IMAGE_SIZE, y);
            if (boxLeft != null) boxLeft.setOpen(true);
            Box boxRight = getBox(enemyField, x + Picture.IMAGE_SIZE, y);
            if (boxRight != null) boxRight.setOpen(true);
        }
    }

    /**
     * Открывает все боксы вокруг корабля в случае, если корабль подбит полностью.
     * Берутся значения первой и последней координат, и все соседние боксы принимают статус открытых.
     */

    public void openAllBoxesAroundShip(Ship ship) {
        Box startBox = ship.getBoxesOfShip().get(0);
        Box endBox = ship.getBoxesOfShip().get(ship.getCountDeck() - 1);
        for (int i = startBox.getX() - Picture.IMAGE_SIZE; i <= startBox.getX() + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = startBox.getY() - Picture.IMAGE_SIZE; j <= startBox.getY() + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box box = getBox(enemyField, i, j);
                if (box != null) box.setOpen(true);
            }
        }
        for (int i = endBox.getX() - Picture.IMAGE_SIZE; i <= endBox.getX() + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = endBox.getY() - Picture.IMAGE_SIZE; j <= endBox.getY() + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box box = getBox(enemyField, i, j);
                if (box != null) box.setOpen(true);
            }
        }
    }

    /**
     * Добавляет корабли пользователя при их размещении на поле, в зависимости от того,
     * какая палубность выбрана. В случае, если ограниченное количество кораблей превышено,
     * выводит информационное сообщение.
     */

    public void addShip(Ship ship) {
        int countDeck = ship.getCountDeck();
        switch (countDeck) {
            case 1 -> {
                if (shipsOneDeck.size() < 4) {
                    shipsOneDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else GameField.callInformationWindow("Перебор однопалубных. Максимально возможно - 4.");
            }
            case 2 -> {
                if (shipsTwoDeck.size() < 3) {
                    shipsTwoDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else GameField.callInformationWindow("Перебор двухпалубных. Максимально возможно - 3.");
            }
            case 3 -> {
                if (shipsThreeDeck.size() < 2) {
                    shipsThreeDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else GameField.callInformationWindow("Перебор трехпалубных. Максимально возможно - 2.");
            }
            case 4 -> {
                if (shipsFourDeck.size() < 1) {
                    shipsFourDeck.add(ship);
                    for (Box box : ship.getBoxesOfShip()) {
                        addBoxInField(myField, box);
                    }
                } else GameField.callInformationWindow("Четырехпалубный уже добавлен. Максимально возможно - 1.");
            }
        }
    }

    /**
     * Возвращает корабль противника, если координаты, по которым был произведён выстрел,
     * совпадают с координатами бокса одного из кораблей противника.
     * @return корабль противника
     */
    public Ship getShipOfEnemy(Box boxShot) {
        for (Ship ship : allShipsOfEnemy) {
            for (Box box : ship.getBoxesOfShip()) {
                if (boxShot.getX() == box.getX() && boxShot.getY() == box.getY()) {
                    return ship;
                }
            }
        }
        return null;
    }

    /**
     * Удаляет корабль из списков, в зависимости от его палубности.
     * @param ship корабль игрока
     */
    public void removeShip(Ship ship) {
        if (shipsOneDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsOneDeck.remove(ship);
            }
        } else if (shipsTwoDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsTwoDeck.remove(ship);
            }
        } else if (shipsThreeDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsThreeDeck.remove(ship);
            }
        } else if (shipsFourDeck.contains(ship)) {
            for (Box box : ship.getBoxesOfShip()) {
                box.setPicture(Picture.EMPTY);
                addBoxInField(myField, box);
                shipsFourDeck.remove(ship);
            }
        }
    }
}
