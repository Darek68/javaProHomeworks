import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class InitDao {
    public static final Logger logger = LogManager.getLogger(InitDao.class.getName());

    private Connection connection;
    private Statement statement;

    private static final String CREATE_SCHEMA_SQL = "CREATE SCHEMA IF NOT EXISTS ";
    private static final String CREATE_TABLE_SQL = """   
                CREATE TABLE IF NOT EXISTS %s.%s (              
                ID int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
                NAME TEXT,                
                AGE            int4,
                BALANCE      float8,    
                SEX            bool,                                
                NOTICE VARCHAR(60));
            """;
    private static String insertUserSql = "INSERT INTO %s.%s (name) VALUES (?);";
    public InitDao(Connection connection) {
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSchema(String schemaName) {
        String createSchema = CREATE_SCHEMA_SQL + schemaName;
        try {
            statement.execute(createSchema);
        } catch (SQLException e) {
            logger.error("Запрос на создание схемы вызвал исключение: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void createTable(String schemaName,String tableName) {
        logger.debug(CREATE_TABLE_SQL);
        try {
            statement.executeUpdate(String.format(CREATE_TABLE_SQL,schemaName,tableName));
            insertUserSql = String.format(insertUserSql,schemaName,tableName);
        } catch (SQLException e) {
            logger.error("Запрос на создание таблицы вызвал исключение: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void createNewUser(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSql);) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Запрос на создание записи вызвало исключение: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.debug("Пользователь " + username + "(" + username + ") успешно создан");
    }

}
