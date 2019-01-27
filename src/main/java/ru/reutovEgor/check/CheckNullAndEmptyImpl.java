package ru.reutovEgor.check;

import org.apache.log4j.Logger;

import ru.reutovEgor.dao.ConnectionProducer;
import ru.reutovEgor.dao.ControlDBImpl;
import ru.reutovEgor.item.User;


import java.sql.Connection;

public class CheckNullAndEmptyImpl implements CheckNullAndEmpty{
    private final Connection connection;
    private final ControlDBImpl store;
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(CheckNullAndEmptyImpl.class);

    public CheckNullAndEmptyImpl() {
        this.connection = ConnectionProducer.getInstance().getConnection();
        this.store = new ControlDBImpl(this.connection);
    }

    @Override
    public User findUserByName(String name) {
        User result = null;
        if (valueIsValid(name)) {
            result = store.findUserByName(name);
            if (result == null) {
                LOGGER.info("User(" + name + ") is not found");
            }
        } else {
            LOGGER.info("Enter a valid surname");
        }
        return result;
    }

    @Override
    public User updateSurname(User user) {
        User result = null;
        if (valueIsValid(user.getName()) && valueIsValid(user.getSurname())) {
            result = store.updateSurname(user);
            if (result == null) {
                LOGGER.info("User with this name(" + user.getName() + ") is not found");
            }
        } else {
            LOGGER.info("Enter a valid name");
        }
        return result;
    }

    private boolean valueIsValid(String value) {
        return ((value != null) && (!value.isEmpty()));
    }
}
