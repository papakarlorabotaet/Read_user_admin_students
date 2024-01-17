package ru.urfu.testsecurity2dbthemeleaf.service;

import ru.urfu.testsecurity2dbthemeleaf.dto.UserDto;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;

import java.util.List;

public interface UserService {

    void userPermissions(User user,String roleName);
    void saveUser(UserDto userDto);
    void deleteUser(Long userId);

    User findUserByEmail(String email);


    List<UserDto> findAllUsers();
    UserDto userToUserDto(User user);
}
