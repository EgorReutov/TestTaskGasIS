package ru.reutovEgor.dao;

import org.apache.log4j.Logger;
import ru.reutovEgor.item.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControlDBImpl implements ControlDB {
    private static final Logger LOGGER = Logger.getLogger(ControlDBImpl.class);
    private static final Settings SETTINGS = Settings.getInstance();
    private static final String FIND_USER_BY_NAME = SETTINGS.value("FIND_USER_BY_NAME");
    private static final String UPDATE_SURNAME = SETTINGS.value("UPDATE_SURNAME");
    private Connection connection;

    public ControlDBImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User findUserByName(String name) {
        String surname = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_NAME)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    surname = resultSet.getString("surname");
                }
            }
            return surname == null ? null : new User(name, surname);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public User updateSurname(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SURNAME)) {
            preparedStatement.setString(1, user.getSurname());
            preparedStatement.setString(2, user.getName());
            return preparedStatement.executeUpdate() == 1 ? user : null;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
