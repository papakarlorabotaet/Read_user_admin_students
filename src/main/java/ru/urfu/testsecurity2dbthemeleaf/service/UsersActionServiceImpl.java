package ru.urfu.testsecurity2dbthemeleaf.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.entity.UsersAction;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserActionRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;

import java.util.Date;

@Service
public class UserActionServiceImpl implements UserActionService {


    @Autowired
    private UserRepository userRepository;
    @Autowired

    private UserActionRepository userActionRepository;

    @Override
    public void saveUsersAction(User user, String stringUserAction) throws Exception {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new Exception("User not found with id: " + id));

        UsersAction usersAction = new UsersAction();
        usersAction.setUserId(user.getId());
        usersAction.setUserEmail(user.getEmail());
        usersAction.setAction(stringUserAction);
        usersAction.setActionDate(new Date());

       userActionRepository.save(usersAction);


    }
}
