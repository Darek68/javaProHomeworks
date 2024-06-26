package ru.darek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserProxy {
    public static final Logger logger = LogManager.getLogger(InitDao.class.getName());

    private Connection connection;
    private Statement statement;
    private static String insertUserSql = "INSERT INTO %s (name,age,balance,sex,notice) VALUES (?,?,?,?,?);";

    public UserProxy(Connection connection) {
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void userAdd(List<User> users, String table){
        logger.info("Create users by proxy");
        if (users == null || table == null) return;
        insertUserSql = String.format(insertUserSql,table);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSql);) {
            connection.setAutoCommit(false);
            long start = System.currentTimeMillis();
            for (User user: users) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setInt(2, user.getAge());
                preparedStatement.setDouble(3, user.getBalance());
                preparedStatement.setBoolean(4, user.isSex());
                preparedStatement.setString(5, "Create by proxy");
                preparedStatement.executeUpdate();
                logger.debug("Пользователь " + user.getName() + " успешно создан");
            }
            logger.debug("Время выполнения? " + (System.currentTimeMillis() - start));
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Запрос на создание записи вызвало исключение: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
