package BattleShip;

import BattleShip.Connection.DBHandler;
import BattleShip.GameInterface.PanelSettings;
import BattleShip.GameInterface.FieldEnemy;
import BattleShip.GameInterface.FieldPlayer;
import BattleShip.GameInterface.PanelButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;


public class GameField extends JFrame {

    /** Поле объекта класса Controller */
    private Controller controller;
    /** Поле объекта класса Model */
    private Model model;
    /** Поле объекта класса FieldPlayer */
    private FieldPlayer myField;
    /** Поле объекта класса FieldEnemy */
    private FieldEnemy fieldEnemy;
    /** Поле объекта класса PanelSettings */
    private PanelSettings panelSettings;
    /** Поле объекта класса PanelButtons */
    private PanelButtons panelButtons;
    /** Поле имени игрока */
    public static String playerName;


    /**
     * Конструктор класса, вызовом метода init() устанавливающий основные параметры реализуемой панели.
     */
    public GameField() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Battle Sea");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setResizable(false);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Метод, размещающий все описанные ранее панели в одном фрейме.
     */
    public void init() {
        if (fieldEnemy != null) {
            remove(fieldEnemy);
            remove(myField);
            remove(panelButtons);
        }
        setBackground(Color.WHITE);
        controller.loadEmptyMyField();
        add(panelSettings = new PanelSettings(this), BorderLayout.WEST);
        add(myField = new FieldPlayer(this), BorderLayout.EAST);
        add(panelButtons = new PanelButtons(this), BorderLayout.SOUTH);
        myField.setPanelSettings(panelSettings);
        pack();
        revalidate();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * Метод, выводящий всплывающее окно с предупреждением на экран.
     * @param message значение содержимого для отображения
     */

    public static void callInformationWindow(String message) {
        JOptionPane.showMessageDialog(
                null, message,
                "Внимание!", JOptionPane.ERROR_MESSAGE
        );
    }

    public void loadEmptyMyField() {
        controller.loadEmptyMyField();
        myField.repaint();
        panelSettings.setCountOne(4);
        panelSettings.setCountTwo(3);
        panelSettings.setCountThree(2);
        panelSettings.setCountFour(1);
    }

    /**
     * Вызывает метод класса Controller для добавления корабля в список существующих.
     * @param ship корабль
     */
    public void addShip(Ship ship) {
        controller.addShip(ship);
    }

    /**
     * Вызывает метод удаления корабля и возвращает его координаты
     * для корректировки количества оставшихся к расположению кораблей.
     * @param x координата корабля
     * @param y координата корабля
     */

    public Ship removeShip(int x, int y) {
        return controller.removeShip(x, y);
    }

    /**
     * Вызывает методы изменения количества отображаемых к расположению кораблей.
     * Перебирает списки существующих кораблей, разделённых по количеству палуб, и передаёт результаты.
     * @param countDeck количество палуб корабля
     */
    public void changeCountShipOnChoosePanel(int countDeck) {
        switch (countDeck) {
            case 1 -> {
                panelSettings.setCountOne(4 - model.getShipsOneDeck().size());
            }
            case 2 -> {
                panelSettings.setCountTwo(3 - model.getShipsTwoDeck().size());
            }
            case 3 -> {
                panelSettings.setCountThree(2 - model.getShipsThreeDeck().size());
            }
            case 4 -> {
                panelSettings.setCountFour(1 - model.getShipsFourDeck().size());
            }
        }
        panelSettings.revalidate();
    }

    /**
     * Отрисовывает и перерисовывает игровое поле в зависимости от того, как изменяется ход игры.
     * В массив matrix получает матрицу поля, после чего, перебирая каждый элемент, воспроизводит его
     * на игровом пространстве.
     */
    public void repaintMyField(Graphics g) {
        Box[][] matrix = model.getMyField();
        for (Box[] boxes : matrix) {
            for (int j = 0; j < boxes.length; j++) {
                Box box = boxes[j];
                if (box == null) continue;
                g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), myField);
            }
        }
    }

    /**
     * Перерисовывает поле соперника – то поле, в котором совершаются выстрелы по вражеским кораблям.
     * Проверяется, был ли до этого открыт выбранный бокс, и в зависимости от того, как расположены
     * корабли со стороны второго игрока, меняет изображение и значение самого бокса.
     */

    public void repaintEnemyField(Graphics g) {
        Box[][] matrix = model.getEnemyField();
        for (Box[] boxes : matrix) {
            for (Box box : boxes) {
                if (box == null) continue;
                if ((box.getPicture() == Picture.EMPTY || box.getPicture() == Picture.SHIP)) {
                    if (box.isOpen() && box.getPicture() == Picture.EMPTY) {
                        g.drawImage(Picture.getImage(Picture.POINT.name()), box.getX(), box.getY(), fieldEnemy);
                    } else if ((box.isOpen() && box.getPicture() == Picture.SHIP)) {
                        g.drawImage(Picture.getImage(Picture.DESTROY_SHIP.name()), box.getX(), box.getY(), fieldEnemy);
                    } else g.drawImage(Picture.getImage(Picture.CLOSED.name()), box.getX(), box.getY(), fieldEnemy);
                } else g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), fieldEnemy);
            }
        }
    }

    /**
     * Метод, вызываемый при нажатии кнопки «Начать игру».
     * Осуществляется проверка на полный комплект расставленных кораблей.
     * Если всё на местах, пользователю выводится диалоговое окно для ввода номера комнаты (порта) и
     * имени – эти данные передаются сер-веру и в базу данных, для сохранения в список и таблицу.
     * Выводится уведомление о том, что игра начнётся после того, как подключится второй соперник;
     * игрок подключается к сер-веру. После подключения окно обновляется – к нему присоединяется панель
     * EnemyField и изменяется статус игры.
     * Если пользователь добавил не все корабли на поле, ему выводится соответствующее информационное сообщение.
     */
    public void startGame() {

        if (controller.checkFullSetShips()) {
            String[] options = {"НОВАЯ ИГРА", "ПОДКЛЮЧИТЬСЯ К ИГРЕ"};
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Введите код комнаты: ");
            JTextField roomCode = new JTextField(25);
            JLabel label2 = new JLabel("Введите имя:  ");
            JTextField userName = new JTextField(25);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(label1);
            panel.add(roomCode);
            panel.add(label2);
            panel.add(userName);

            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Создание комнаты:",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            try {
                if ((!Objects.equals(userName.getText(), null) && (!Objects.equals(roomCode.getText(), null)))) {
                    if (selectedOption == 0) {

                        int port = Integer.parseInt(roomCode.getText().trim());
                        controller.createGameRoom(port);
                        playerName = userName.getText().trim();
                        panelButtons.setUserName(playerName);
                        DBHandler.addUser(playerName);

                        panelButtons.setTextInfo("   ОЖИДАЕМ СОПЕРНИКА");
                        panelButtons.revalidate();

                        GameField.callInformationWindow("Ожидаем соперника: после того как соперник подключится к комнате, появится уведомление. Затем начнется игра. Ваш ход первый.");
                        controller.connectToRoom(port);
                        controller.sendName(playerName);

                        GameField.callInformationWindow("Второй игрок подключился! Можно начинать сражение.");
                        refreshGuiAfterConnect();

                        panelButtons.setTextInfo("       СЕЙЧАС ВАШ ХОД");
                        panelButtons.getExitButton().setEnabled(true);
                        fieldEnemy.addListener();

                    } else if (selectedOption == 1) {

                        int port = Integer.parseInt(roomCode.getText().trim());
                        controller.connectToRoom(port);
                        playerName = userName.getText().trim();
                        DBHandler.addUser(playerName);
                        panelButtons.setUserName(playerName);
                        controller.sendName(playerName);

                        GameField.callInformationWindow("Вы успешно подключились к комнате. Ваш соперник ходит первым.");
                        refreshGuiAfterConnect();
                        panelButtons.setTextInfo(" СЕЙЧАС ХОД СОПЕРНИКА");
                        new ReceiveThread().start();
                    }
                }
            } catch (Exception e) {
                GameField.callInformationWindow("Проверьте корректность ввода данных для подключения к комнате. ");
                e.printStackTrace();
            }
        } else GameField.callInformationWindow("Разместите все корабли на поле. ");
    }

    /**
     * Метод, вызываемый при нажатии на кнопку «Сдаться». Сдавшийся игрок отключается от комнаты и терпит техническое поражение.
     * При возникновении ошибок со стороны программы, за невозможностью продолжать сражение, результаты игры удаляются из базы данных.
     */
    public void disconnectGameRoom() {
        try {
            controller.disconnectGameRoom();
            GameField.callInformationWindow("Вы отключились от комнаты. Игра окончена. Вы потерпели техническое поражение.");
            fieldEnemy.removeListener();
        } catch (Exception e) {
            GameField.callInformationWindow("Произошла ошибка при отключении от комнаты.");
            DBHandler.deleteGame();
        }
    }

    /**
     * После подключения к комнате окно видоизменяется – убирается возможность продол-жать регистрировать
     * нажатия клиента на уже заполненное поле с кораблями, а также появляется панель противника, на которой
     * осуществляется отправка выстрелов по против-нику. Удаляется панель настроек, поле перерисовывается.
     */

    public void refreshGuiAfterConnect() {
        MouseListener[] listeners = myField.getMouseListeners();
        for (MouseListener lis : listeners) {
            myField.removeMouseListener(lis);
        }
        panelSettings.setVisible(false);
        remove(panelSettings);
        add(fieldEnemy = new FieldEnemy(this), BorderLayout.WEST);
        fieldEnemy.repaint();
        pack();
        panelButtons.getStartGameButton().setEnabled(false);
        revalidate();
    }


    /**
     * Метод, отправляющий координаты выстрела на сервер.
     * Вызывается после регистрации нажатия игроком на поле противника.
     * Если сообщение удачно отправлено, поле противни-ка перерисовывается,
     * и в зависимости от того, попал игрок или нет, ему добавляется ход.
     * В противном случае, наоборот – отнимается возможность ходить, и статус меняется на ход противника.
     * Запускается поток, ожидающий ответ от сервера.
     * @param x координата корабля
     * @param y координата корабля
     */

    public void sendShot(int x, int y) {
        try {
                boolean isSendShot = controller.sendMessage(x, y);
                System.out.println(controller.ch);
                if (isSendShot) {
                    fieldEnemy.repaint();
                    fieldEnemy.removeListener();
                    panelButtons.setTextInfo(" СЕЙЧАС ХОД СОПЕРНИКА");
                    panelButtons.getExitButton().setEnabled(false);
                    new ReceiveThread().start();

                    if (controller.ch) {
                        fieldEnemy.addListener();
                        panelButtons.setTextInfo("       СЕЙЧАС ВАШ ХОД");
                    }
                }

        } catch (Exception e) {
            DBHandler.deleteGame();
            GameField.callInformationWindow("Произошла ошибка при отправке выстрела.");
            e.printStackTrace();
        }
    }

    /**
     * Класс-поток, ожидающий сообщение от сервера. Принимает значения выстрелов,
     * и, в зависимости от того, было ли попадание удачным, и закончена ли игра, устанавливает
     * соответствующие статусы. Также управляет возможностью игроков осуществлять ходы.
     */
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                int k = 0;
                    boolean continueGame = controller.receiveMessage();
                    if (!controller.checkEndGame()) {
                        while (true) {
                            if (continueGame) {
                                myField.repaint();
                                if (controller.checkPoint) {
                                    fieldEnemy.repaint();
                                    continueGame = controller.receiveMessage();

                                } else {
                                    myField.repaint();
                                    fieldEnemy.repaint();
                                    panelButtons.setTextInfo("       СЕЙЧАС ВАШ ХОД");
                                    panelButtons.getExitButton().setEnabled(true);
                                    fieldEnemy.addListener();
                                    this.interrupt();
                                    break;
                                }
                            }  else {
                                myField.repaint();
                                fieldEnemy.repaint();
                                System.out.println(controller.checkEndGame() + "" + k);
                                System.out.println("конец игры второго " + Thread.activeCount());
                                panelButtons.setTextInfo("         ИГРА ОКОНЧЕНА");
                                panelButtons.getExitButton().setEnabled(false);
                                fieldEnemy.removeListener();
                                panelButtons.getRestartGameButton().setEnabled(true);
                                break;
                            }
                        }
                    }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("Произошла ошибка при приеме сообщения от сервера");
                DBHandler.deleteGame();
                e.printStackTrace();
            }
        }
    }
}
