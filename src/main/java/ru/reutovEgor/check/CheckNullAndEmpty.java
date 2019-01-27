package ru.reutovEgor.check;

import ru.reutovEgor.item.User;

public interface CheckNullAndEmpty {
    User findUserByName(String name);
    User updateSurname(User user);
}
