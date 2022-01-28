package BattleShip.Connection;

/**
 * Данный интерфейс хранит информацию для подключения к базе данных
 * @author Valentina Tevyants
 */
public interface DBConfiguration {
    /** Поле хоста*/
    String DBHost = "localHost";
    /** Поле порта*/
    String DBPort = "3306";
    /** Поле логина*/
    String DBUser = "root";
    /** Поле пароля*/
    String DBPassword = "rserver1928";
    /** Поле названия базы данных*/
    String DBName = "BattleShip_DB";
}
