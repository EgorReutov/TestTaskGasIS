package ru.reutovEgor.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProducer {
    private static final BasicDataSource SOURCE = new BasicDataSource();
    private final Logger LOGGER = Logger.getLogger(ConnectionProducer.class);
    private static final Settings SETTINGS = Settings.getInstance();
    private static final String DRIVER_CLASS_NAME = SETTINGS.value("DRIVER_CLASS_NAME");
    private static final String URL = SETTINGS.value("URL");
    private static final String USERNAME = SETTINGS.value("USER");
    private static final String PASSWORD = SETTINGS.value("PASSWORD");
    private static final ConnectionProducer INSTANCE = new ConnectionProducer();

    private ConnectionProducer() {
        SOURCE.setDriverClassName(DRIVER_CLASS_NAME);
        SOURCE.setUrl(URL);
        SOURCE.setUsername(USERNAME);
        SOURCE.setPassword(PASSWORD);
    }

    public static ConnectionProducer getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = SOURCE.getConnection();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return connection;
    }

    public void shutDown(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
