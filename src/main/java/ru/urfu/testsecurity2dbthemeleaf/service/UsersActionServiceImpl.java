package ru.urfu.testsecurity2dbthemeleaf.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.testsecurity2dbthemeleaf.entity.Role;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.entity.UsersAction;
import ru.urfu.testsecurity2dbthemeleaf.repository.UsersActionRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class UsersActionServiceImpl implements UsersActionService {


    @Autowired
    private UserRepository userRepository;
    @Autowired

    private UsersActionRepository usersActionRepository;

    @Override
    public void saveUsersAction(User user, String stringUserAction) {

        UsersAction usersAction = new UsersAction();
        usersAction.setUserEmail(user.getEmail());
        usersAction.setRole(user.getRoles()
                .stream().map(Role::getName)
                .toList()
                .toString()); //.collect(Collectors.toList()).toString()
        usersAction.setAction(stringUserAction);

        usersAction.setActionDate(
                usersAction.getDateFormat()
                        .format(new Date()));
        usersActionRepository.save(usersAction);

    }

    @Override
    public void saveUsersAction(User user, String stringUserAction, String stringUserActionForWho) {

        if (stringUserAction != null) {
            UsersAction usersAction = new UsersAction();
            usersAction.setUserEmail(user.getEmail());
            usersAction.setRole(user.getRoles()
                    .stream().map(Role::getName)
                    .toList()
                    .toString()); //.collect(Collectors.toList()).toString()
            usersAction.setAction(stringUserAction + " " + stringUserActionForWho);

            usersAction.setActionDate(
                    usersAction.getDateFormat()
                            .format(new Date()));
            usersActionRepository.save(usersAction);

        }
    }




    @Override
    public List<UsersAction> findAllUsersAction() {
        return usersActionRepository.findAll();
    }

}
