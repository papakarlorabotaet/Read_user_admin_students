package ru.urfu.testsecurity2dbthemeleaf.service;

import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.entity.UsersAction;

import java.util.List;

public interface UsersActionService {
    void saveUsersAction(User user, String userAction);
    void saveUsersAction(User user, String userAction ,String userActionForWho);

    List<UsersAction> findAllUsersAction();
}
