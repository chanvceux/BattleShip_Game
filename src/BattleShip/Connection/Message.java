package BattleShip.Connection;
import BattleShip.Box;
import BattleShip.Ship;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    /** Поле координаты x  */
    private int x;
    /** Поле координаты y  */
    private int y;
    /** Поле имени игрока  */
    private String playerName;
    /** Поле типа сообщения  */
    private MessageType messageType;
    /** Массив игрового поля   */
    private Box[][]  gameField;
    /** Список кораблей  */
    private List<Ship> listOfAllShips;


    /**
     * Конструктор класса, создающий сообщение, состоящее из его типа, игрового поля, и всех кораблей противника.
     * @param messageType тип сообщения
     * @param gameField игровое поле
     * @param allShipsOfEnemy корабли противника
     */
    public Message(MessageType messageType, Box[][] gameField, List<Ship> allShipsOfEnemy) {
            this.messageType = messageType;
            this.gameField = gameField;
            this.listOfAllShips = allShipsOfEnemy;
    }

    /**
     * Конструктор класса, создающий сообщение, состоящее только из его типа.
     * @param messageType тип сообщения
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Конструктор класса, создающий сообщение, состоящее из его типа и координат.
     * @param messageType тип сообщения
     * @param x координата корабля
     * @param y координата корабля
     */
    public Message(MessageType messageType, int x, int y) {
        this.x = x;
        this.y = y;
        this.messageType = messageType;
    }

    /**
     * Конструктор класса, создающий сообщение, состоящее из его типа и имени игрока.
     * @param messageType тип сообщения
     * @param playerName имя игрока
     */
    public Message(MessageType messageType, String playerName) {
        this.playerName = playerName;
        this.messageType = messageType;
    }

    /**
     * @return имя игрока
     */

    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return координата корабля
     */
    public int getX() {
        return x;
    }

    /**
     * @return координата корабля
     */
    public int getY() {
        return y;
    }

    /**
     * @return тип сообщения
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * @return игровое поле
     */
    public Box[][] getGameField() {
        return gameField;
    }

    /**
     * @return список кораблей
     */

    public List<Ship> getListOfAllShips() {
        return listOfAllShips;
    }
}
