package ru.urfu.testsecurity2dbthemeleaf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.testsecurity2dbthemeleaf.dto.UserDto;
import ru.urfu.testsecurity2dbthemeleaf.entity.Role;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.repository.RoleRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void userPermissions(User user, String roleName) {

        Role roleFromRepository = roleRepository.findByName(roleName);
        user.getRoles().clear();
        user.getRoles().add(roleFromRepository);
        userRepository.save(user);

    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        //ENCRYPT THE PASSWORD
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (checkAdmin(user)) {//Если создаем админку, создаем обычного юзера
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            user.setRoles(Collections.singletonList(roleAdmin));
            userRepository.save(user);

            Role roleUser = roleRepository.findByName("ROLE_USER");//проверяем, чтобы не создать еще......
            if (roleUser == null) { //......одну роль
                Role role = new Role();
                role.setName("ROLE_USER");
                roleRepository.save(role);
            }
        } else {

            Role roleFromRepository = roleRepository.findByName("ROLE_READ");
            if (roleFromRepository == null) {
                Role role = new Role();
                role.setName("ROLE_READ");
                roleRepository.save(role);
            }
            user.setRoles(Collections.singletonList(roleFromRepository));//Arrays.asList()
            userRepository.save(user);

        }




//        Role role = roleRepository.findByName("ROLE_READ");
//        if(role == null){
//
//        }
//        //Role role = roleRepository.findByName("ROLE_ADMIN");
//        if(role == null){
//            role = checkRoleExist();
//        }
//        user.setRoles(Arrays.asList(role));
//        Role role = new Role();
//        role.setName("ROLE_USER");
//        user.setRoles(Arrays.asList(setPermission(user)));
//        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }




    @Override
    public List<UserDto> findAllUsers() {
        List<User> user = userRepository.findAll();
        return user.stream().map((users) -> mapToUserDto(users)).collect(Collectors.toList());

    }

    @Override
    public UserDto userToUserDto(User user) {
        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles().stream().map((role) -> (role.getName())).collect(Collectors.toList()));
//                (user.getRoles().stream().map(role -> r ).collect(Collectors.toList()));
        return userDto;

    }

    private User userDtoToUser(UserDto userDto) {
        User user = new User();
//        user.setId(userDto.getId());
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // user.setRoles(userDto.getRoles().stream());
        return user;
    }

    private boolean checkAdmin(User user) {

        if (Objects.equals(user.getEmail(), "admin@mail.ru")) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
            return true;
        }
        return false;

    }

    private boolean checkUser(User user) { //Костыль, потом по другому реализовать

        if (Objects.equals(user.getEmail(), "user@mail.ru")) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
            return true;
        }
        return false;

    }

}
