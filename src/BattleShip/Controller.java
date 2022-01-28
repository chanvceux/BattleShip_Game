package BattleShip;
import BattleShip.Connection.*;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Controller {

    /** Поле объекта класса GameField */
    private GameField gameField;
    /** Поле объекта класса Model */
    private Model model;
    /** Поле объекта класса Connection */
    private Connection connection;
    /** Поле проверки выстрелов */
    public boolean check = false;

    public Controller(GameField gameField, Model model) {
        this.gameField = gameField;
        this.model = model;
    }

    /**
     * Загружает пустое игровое поле – очищает списки кораблей,
     * если таковые имеются, после чего выводит на экран пользователя картинки
     * закрытых боксов для размещения флота.
     */
    public void loadEmptyMyField() {
        model.getShipsOneDeck().clear();
        model.getShipsTwoDeck().clear();
        model.getShipsThreeDeck().clear();
        model.getShipsFourDeck().clear();

        model.setMyField(new Box[Picture.COLUMNS][Picture.ROWS]);

        for (int i = 0; i < Picture.ROWS; i++) {
            for (int j = 0; j < Picture.COLUMNS; j++) {
                if (i == 0 && j == 0) continue;
                else if (i == 0 && j != 0) { //если это первый столбец, то присваиваем значение картинок с буквами
                    model.addBoxInField(model.getMyField(), new Box(Picture.valueOf("SYM" + j), Picture.IMAGE_SIZE * i, Picture.IMAGE_SIZE * j));
                } else if (i != 0 && j == 0) { //если это первая строка присваиваем значение картинок с цифрами
                    model.addBoxInField(model.getMyField(), new Box(Picture.valueOf("NUM" + i), Picture.IMAGE_SIZE * i, Picture.IMAGE_SIZE * j));
                } else { //в остальных случаях значение картинки с пустой клеткой
                    model.addBoxInField(model.getMyField(), new Box(Picture.EMPTY, Picture.IMAGE_SIZE * i, Picture.IMAGE_SIZE * j));
                }
            }
        }
    }

    /**
     * Метод, добавляющий новый корабль.
     * Предварительно осуществляется проверка – не пересекает ли установленный корабль
     * уже имеющиеся. В случае, если всё корректно, передаёт объект класса Ship в класс Model,
     * где он добавляется в список, соответствующий количеству его палуб.
     * @param ship объект класса Ship
     */

    public void addShip(Ship ship) {
        List<Box> boxesOfShip = ship.getBoxesOfShip();
        for (Box boxShip : boxesOfShip) {
            if (checkAround(boxShip, boxesOfShip)) {
                boxesOfShip.clear();
                return;
            }
        }
        if (boxesOfShip.size() != 0) model.addShip(ship);
    }

    /**
     * Метод, удаляющий корабль из списков.
     * @param x координата корабля
     * @param y координата корабля
     * @return объект класса Ship
     */

    public Ship removeShip(int x, int y) {
        List<Ship> allShips = model.getAllShips();
        for (Ship ship : allShips) {
            for (Box box : ship.getBoxesOfShip()) {
                if (x == box.getX() && y == box.getY()) {
                    model.removeShip(ship);
                    return ship;
                }
            }
        }
        return null;
    }

    /**
     * Перебирает значения координат всех имеющихся кораблей, чтобы сравнить,
     * пересекается ли корабль с уже имеющимися.
     * @param box бокс палубы
     * @param boxesOfShip все боксы корабля
     * @return результат размещения корабля
     */
    private boolean checkAround(Box box, List<Box> boxesOfShip) {
        int myX = box.getX();
        int myY = box.getY();
        for (int i = myX - Picture.IMAGE_SIZE; i <= myX + Picture.IMAGE_SIZE; i += Picture.IMAGE_SIZE) {
            for (int j = myY - Picture.IMAGE_SIZE; j <= myY + Picture.IMAGE_SIZE; j += Picture.IMAGE_SIZE) {
                Box boxFromMatrix = model.getBox(model.getMyField(), i, j);
                if (boxFromMatrix != null && boxFromMatrix.getPicture() == Picture.SHIP && !boxesOfShip.contains(boxFromMatrix)) {
                    GameField.callInformationWindow("Выбранное расположение корабля пересекает другой корабль. ");
                    boxesOfShip.clear();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Метод, открывающий пустые клетки вокруг подбитого корабля на поле противника.
     * Он получает список всех кораблей, расставленных противником, после чего,
     * перебирая все его элементы, проверяет – если корабль однопалубный,
     * открываются все координаты рядом. Если подбита только одна палуба – клетки не открываются.
     * Если подбито несколько палуб – открываются пустые клетки вокруг них.
     * @param boxShot бокс палубы
     */

    public void openBoxesAround(Box boxShot) {
        Ship ship = model.getShipOfEnemy(boxShot);
        if (ship != null) {
            check = true;
            if (ship.getCountDeck() == getCountOpenBoxOfShip(ship)) model.openAllBoxesAroundShip(ship);
            else if (getCountOpenBoxOfShip(ship) == 1) return;
            else {
                for (Box box : ship.getBoxesOfShip()) {
                    if (box.isOpen())
                        model.openBoxAroundBoxOfShipEnemy(box.getX(), box.getY(), ship.isHorizontalPlacement());
                }
            }
        } else check = false;

    }

    /**
     * Метод, возвращающий количество открытых (подбитых) боксов (палуб) корабля.
     * @param ship объект класса Ship
     * @return количество открытых боксов
     */
    public int getCountOpenBoxOfShip(Ship ship) {
        int count = 0;
        for (Box box : ship.getBoxesOfShip()) {
            if (box.isOpen()) count++;
        }
        return count;
    }

    /**
     * Проверка на конец игры – количество кораблей в списке не равно нулю, игра продолжается.
     * @return результат проверки
     */
    public boolean checkEndGame() {
        List<Box> allBoxesOfShip = model.getAllBoxesOfShips();
        for (Box box : allBoxesOfShip) {
            if (box.getPicture() == Picture.SHIP) return false;
        }
        return true;
    }

    /**
     * Метод, возвращающий true, в случае, если пользователь добавил все корабли на поле,
     * и false – в случае, если какой-то из кораблей ещё не размещён.
     * @return результат проверки
     */
    public boolean checkFullSetShips() {
        return model.getShipsOneDeck().size() == 4 &&
                model.getShipsTwoDeck().size() == 3 &&
                model.getShipsThreeDeck().size() == 2 &&
                model.getShipsFourDeck().size() == 1;
    }

    /**
     * Создаёт комнату для игры – создаёт новый экземпляр класса Server,
     * в котором осуществляется запуск сервера с указанным портом.
     * @param port порт
     */

    public void createGameRoom(int port) throws IOException {
        Server server = new Server(port);
        server.start();
    }

    /**
     * Создаёт новый экземпляр класса Connection, параметрами которого являются объект
     * класса Socket с вводимым пользователем портом – так игрок подключается к серверу.
     * С использованием класса Message получаем сообщение от сервера – если тип сообщения ACCEPTED,
     * игрок успешно подключён. Следующий шаг – передача серверу игрового поля
     * (координат всех размещённых кораблей). После подключения второго игрока принимается сообщение
     * с информацией о его игровом поле (с пометкой FIELD), и сохраняется.
     * @param port порт
     */

    public void connectToRoom(int port) throws IOException, ClassNotFoundException {
        connection = new Connection(new Socket("localhost", port));
        Message message = connection.receive();
        if (message.getMessageType() == MessageType.ACCEPTED) {
            connection.send(new Message(MessageType.FIELD, model.getMyField(), model.getAllShips()));
            Message messageField = connection.receive();
            if (messageField.getMessageType() == MessageType.FIELD) {
                model.setEnemyField(messageField.getGameField());
                model.setAllShipsOfEnemy(messageField.getListOfAllShips());
            }
        }
    }


    public void disconnectGameRoom() throws IOException {
        connection.send(new Message(MessageType.DISCONNECT));
    }

    public boolean ch;


    /**
     * Метод, осуществляющий отправку координат выстрела на сервер.
     * Проверяет, является ли бокс, по которому пришёлся выстрел, открытым – если нет,
     * то ему присваивается этот статус, а координаты отправляются на сервер.
     */

    public boolean sendMessage(int x, int y) throws IOException {
        Box box = model.getBox(model.getEnemyField(), x, y);
        if (!box.isOpen()) {
            box.setOpen(true);
            openBoxesAround(box);
            ch = check;
            connection.send(new Message(MessageType.SHOT, x, y));
            return true;
        } else return false;
    }

    public void sendName(String playerName) throws IOException {
            connection.send(new Message(MessageType.USERNAME, playerName));
    }

    public boolean checkPoint;

    /**
     * Метод, принимающий сообщения от сервера. Сообщения различаются по их типу – если оно с пометкой SHOT,
     * то по координатам определяется бокс. Если данный бокс пуст, в нём устанавливается картинка с точкой,
     * в противном случае – с подбитым кораблём.
     * Далее осуществляется проверка на конец игры – если она окончена, выводится информационное сообщение,
     * и игра заканчивается. Если соперник досрочно покинул игру, выводится сообщение о технической победе.
     * Каждому пользователю в конце игры выводится уведомление о том, является он победителем, или же проигравшим.
     */

    public boolean receiveMessage() throws IOException, ClassNotFoundException {

        Message message = connection.receive();

        if (message != null) {
            if (message.getMessageType() == MessageType.SHOT) {
                int x = message.getX();
                int y = message.getY();
                Box box = model.getBox(model.getMyField(), x, y);
                if (box.getPicture() == Picture.EMPTY) {
                    box.setPicture(Picture.POINT);
                    checkPoint = false;
                } else if (box.getPicture() == Picture.SHIP) {
                    box.setPicture(Picture.DESTROY_SHIP);
                    checkPoint = true;
                }
                model.addBoxInField(model.getMyField(), box);
                if (checkEndGame()) {
                    connection.send(new Message(MessageType.DEFEAT));
                    GameField.callInformationWindow("Вы проиграли! Все Ваши корабли уничтожены.");
                    return false;
                }
                return true;
            } else if (message.getMessageType() == MessageType.DISCONNECT) {
                connection.send(new Message(MessageType.MY_DISCONNECT));
                GameField.callInformationWindow("Ваш соперник покинул игру. Вы одержали техническую победу!");
                DBHandler.endGame(GameField.playerName);
                return false;
            } else if (message.getMessageType() == MessageType.DEFEAT) {
                connection.send(new Message(MessageType.MY_DISCONNECT));
                System.out.println("Победил " + GameField.playerName);
                DBHandler.updateUserGames(GameField.playerName, true);
                GameField.callInformationWindow("Все корабли противника уничтожены. Вы одержали победу!");
                return false;

            } else return false;
         } else return false;
    }
}
