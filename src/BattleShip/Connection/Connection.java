package BattleShip.Connection;
import java.io.*;
import java.net.Socket;

/**
 * Данный класс реализует интерфейс Closeable для грамотного освобождения ресурсов при использовании ObjectOutputStream
 * и ObjectInputStream, а также отвечает за отправку и получение сообщений.
 * @author Valentina Tevyants
 */
public class Connection implements Closeable {
    /** Поле клиентского сокета  */
    private final Socket socket;
    /** Поле потока отправки сообщений */
    private final ObjectOutputStream out;
    /** Поле потока считывания сообщений  */
    private final ObjectInputStream in;


    /**
     * Конструктор класса инициализирует сокет и потоки ввода-вывода для отправки и получения сообщений
     * @param socket Пользовательский сокет
     * @throws IOException
     */
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Отправка сообщений на сервер
     * @param message Объект класса Message, который хранит пользовательское сообщение
     * @throws IOException
     */
    public void send(Message message) throws IOException {
        synchronized (this.out){
            out.writeObject(message);
        }
    }

    /**
     * Получение сообщений от сервера и передача их клиентам
     * @return Объект класса Message, который хранит пользовательское сообщение
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (this.in) {
            try {
                Message message = (Message) in.readObject();
                return message;
            } catch (EOFException exception) {
                System.out.println(exception.getMessage());
                return null;
            }
        }
    }

    /**
     * Данный метод убивает поток пользователя и его потоки ввода-вывода
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
