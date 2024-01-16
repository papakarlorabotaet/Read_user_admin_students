package ru.urfu.testsecurity2dbthemeleaf.service;

import ru.urfu.testsecurity2dbthemeleaf.entity.User;

public interface UserActionService {
    void saveUsersAction(User user, String userAction) throws Exception;
}
