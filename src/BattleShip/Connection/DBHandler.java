package BattleShip.Connection;

import java.sql.*;
import java.sql.Connection;

/**
 * Класс, содержащий методы, описывающие подключение и работу с базой данных.
 * @author Valentina Tevyants
 */
public class DBHandler implements DBConfiguration {

    private static java.sql.Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    /**
     * Метод, реализующий подключение к базе данных.
     * @return соединение
     */
    public static Connection getConnection() {
        String connectionString = "jdbc:mysql://" + DBHost + ":"
                + DBPort + "/" + DBName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver is connected");
        } catch (ClassNotFoundException exception) {
            System.out.println("Something is wrong with driver: " + exception);
        }

        try {
            connection = DriverManager.getConnection(connectionString, DBUser, DBPassword);
            System.out.println("Successfully connected. ");
        } catch (SQLException exception) {
            System.out.println("Sorry, we can't connect you: " + exception);
        }

        return connection;
    }

    /**
     * Осуществляет поиск пользователя по переданному в качестве параметра логину.
     * @param login логин пользователя
     * @return результат поиска пользователя
     */
    public static boolean findPlayer(String login) {

        int count = 0;

        String request = "SELECT COUNT(*) FROM BattleShip_DB.Players WHERE playerName like '" + login + "'";

        try {
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(request);

            while (resultSet.next()) {
                count = resultSet.getInt(1);
                System.out.println("Users found: " + count);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        updateUserGames(login, false);

        return count > 0;
    }

    /**
     * Осуществляет поиск ID пользователя.
     * @param login логин пользователя
     * @return id пользователя по заданному логину
     */
    public static int findPlayerID(String login) {

        int id = 0;

        String request = "SELECT idPlayers FROM BattleShip_DB.Players WHERE playerName like '" + login + "'";

        try {
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(request);

            while (resultSet.next()) {
                id = resultSet.getInt(1);
                System.out.println("Users found: " + id);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return id;
    }

    /**
     * Добавляет новую запись в таблицу GameResults, внося в неё имена игроков.
     * @param playerLogin логин игрока
     * @param enemyLogin логин противника
     */

    public static void startGame(String playerLogin, String enemyLogin) {

        String request = "insert GameResults (idPlayer, idEnemy, idWinner) VALUES (?, ?, 0) ";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setInt(1, findPlayerID(playerLogin));
            preparedStatement.setInt(2, findPlayerID(enemyLogin));
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Получает ID последней игры.
     * @return ID последней игры
     */

    public static int lastID() {

        String request = "SELECT MAX(idGame) FROM GameResults";
        int id = 0;

        try {
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(request);

            while (resultSet.next()) {
                id = resultSet.getInt(1);
                System.out.println("Last game: " + id);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return id;
    }

    /**
     * Записывает результат игры – победителя в таблицу GameResults.
     * @param playerLogin логин победителя
     */
    public static void endGame(String playerLogin) {

        int id = findPlayerID(playerLogin);
        int idGame = lastID();

        String request = "update GameResults set idWinner = " + id + " where idGame = " + idGame;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            System.out.println("USER " + playerLogin + " added as winner. Congrads");
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Удаляет игру, в случае возникновения технических проблем.
     */

    public static void deleteGame() {

        int idGame = lastID();
        String request = "DELETE from GameResults where idGame = " + idGame;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Добавляет нового пользователя в таблицу Players, если он не играл ранее.
     * @param login логин игрока
     */
    public static void addUser(String login) {

        if (!findPlayer(login)) {

            String request = "insert Players (countOfGames, countOfWins, playerName) " +
                    "values (1, 0, ?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(request);
                preparedStatement.setString(1, login);
                preparedStatement.executeUpdate();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Обновляет количество игр и побед у игрока, чьё имя передано в качестве параметра.
     * @param login логин игрока
     * @param ifWins является ли победителем
     */
    public static void updateUserGames(String login, boolean ifWins) {
        String request;

        if (!ifWins) {
            request = "update Players set countOfGames = countOfGames + 1 " +
                    "WHERE playerName = '" + login + "'";
        } else {
            endGame(login);
            request = "update Players set countOfWins = countOfWins + 1 " +
                    "WHERE playerName = '" + login + "'";

        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            System.out.println("USER " + login + " updated (win + 1)");
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Получает список результатов всех проведённых игр.
     * @return список игр
     */
    public static String[][] getAllGames() {

        String playerName;
        String enemyName;
        String winnerName;

        String request = " SELECT pl1.playerName, pl2.playerName, pl3.playerName " +
                " FROM GameResults " +
                " JOIN Players AS pl1 ON GameResults.idPlayer = pl1.idPlayers " +
                " JOIN Players AS pl2 ON GameResults.idEnemy = pl2.idPlayers " +
                " JOIN Players AS pl3 ON GameResults.idWinner = pl3.idPlayers";


        try {
            statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            int i = 0;
            String [][] inf = new String[lastID()][3];
            System.out.println(1);

            while (resultSet.next()) {

                 playerName = resultSet.getString(1);
                 enemyName = resultSet.getString(2);
                 winnerName = resultSet.getString(3);

                 inf[i][0] = playerName;
                 inf[i][1] = enemyName;
                 inf[i][2] = winnerName;

                 i++;
            }

        return inf;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return new String[0][];
    }

    /**
     * Получает список рейтинга игроков из таблицы Players.
     * @return рейтинг игроков
     */

    public static String[][] getAllPlayers() {

        String playerName;
        String countOfGames;
        String countOfWins;

        String request = "SELECT playerName, countOfGames, countOfWins from Players ORDER BY countOfWins DESC, countOfGames ASC";

        try {
            statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            int i = 0;
            String[][] playersGame = new String[lastID()][4];

            while (resultSet.next()) {

                playerName = resultSet.getString(1);
                countOfGames = String.valueOf(resultSet.getInt(2));
                countOfWins = String.valueOf(resultSet.getString(3));

                playersGame[i][0] = String.valueOf(i+1);
                playersGame[i][1] = playerName;
                playersGame[i][2] = countOfGames;
                playersGame[i][3] = countOfWins;

                i++;
            }

            return playersGame;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return new String[0][];
    }
}



