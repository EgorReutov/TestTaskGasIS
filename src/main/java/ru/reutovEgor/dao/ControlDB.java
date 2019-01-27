package ru.reutovEgor.dao;

import ru.reutovEgor.item.User;

public interface ControlDB {
    User findUserByName(String name);
    User updateSurname(User user);
}
