import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyApp {
    public static final Logger logger = LogManager.getLogger(MyApp.class.getName());
    private static Connection connection;


    public static void main(String[] args) {
        setConnect();
        init("testproxy", "users");
        List<User> users = getUser();
        UserDao userDao = new UserDao(connection);
        userDao.userAdd(users, "testproxy.users");
        userDao = null;
        UserProxy userProxy = new UserProxy(connection);
        userProxy.userAdd(users, "testproxy.users");
        userProxy.disconnect();
        userProxy = null;
        return;
    }

    private static void setConnect() {
        try {
            connection = DriverManager.getConnection(ConnDate.getDatabaseUrl(), ConnDate.getLogin(), ConnDate.getPass());
            logger.debug("Создан коннект к БД");
        } catch (SQLException e) {
            logger.error("Не удалось подключится к БД: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void init(String schemaName, String tableName) {
        InitDao initDao = new InitDao(connection);
        initDao.createSchema(schemaName);
        logger.debug("Создана схема " + schemaName);
        initDao.createTable(schemaName, tableName);
        logger.debug("Создана таблица " + tableName);
        initDao.createNewUser("Anonim");
        logger.debug("Успешно создан пробный пользователь");
    }

    private static List<User> getUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("Masha", 31, 205.46, false));
        users.add(new User("Sasha", 71, 1705, true));
        users.add(new User("Dasha", 21, 100.5, false));
        users.add(new User("Rom", 38, 2000, true));
        users.add(new User("Alex", 42, 4205.46, true));
        return users;
    }
}