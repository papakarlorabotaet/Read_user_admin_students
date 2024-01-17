package ru.urfu.testsecurity2dbthemeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.testsecurity2dbthemeleaf.dto.UserDto;
import ru.urfu.testsecurity2dbthemeleaf.entity.Role;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.repository.RoleRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;
import ru.urfu.testsecurity2dbthemeleaf.service.UserService;
import ru.urfu.testsecurity2dbthemeleaf.service.UsersActionService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;



@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UsersActionService usersActionService;


    @PostMapping("/userPermissions")
    public String userPermissions(@RequestParam("email") String email,
                            @RequestParam("role") String roleName,
                            @AuthenticationPrincipal UserDetails userDetails){
        User currentUser = userRepository.findByEmail(email);
        String userActionText = currentUser.getName().split(" ")[0] + " c "+currentUser.getAuthorities().toString() + " на " + roleName;
        userService.userPermissions(currentUser,roleName);



        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ИЗМЕНИЛ ПРАВА ПОЛЬЗОВАТЕЛЯ :", userActionText);
        }

        return "redirect:/users";
    }
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam String userEmail,
                             @AuthenticationPrincipal UserDetails userDetails){
        User userDelete = userRepository.findByEmail(userEmail); //полчаем инфу

        if(userDetails != null){
            String userRoleAndUserName = userDelete.getAuthorities().toString()  + " " + userRepository.findByEmail(userEmail).getName().split(" ")[0];
            User user = userRepository.findByEmail(userDetails.getUsername());//ищем и записываем в базу
            usersActionService.saveUsersAction(user, "УДАЛИЛ ПОЛЬЗОВАТЕЛЯ: ", userRoleAndUserName);

        }



        userDelete.getRoles().clear();//очищаем List<Role>
        userService.deleteUser(userDelete.getId()); //удаляем пользователя из базы
        return "redirect:/users";
    }
    @GetMapping("/users")
    public String users(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<UserDto> usersDto = userService.findAllUsers();
        List<Role> roles = roleRepository.findAll();
        List<String> listOfRole = roles.stream()
                .map(Role::getName)//УБИРАЕМ  лишнее [ROLE_ADMIN] -> ADMIN
                .collect(Collectors.toList());


        if(userDetails != null) {
            User users = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(users, "ОТКРЫЛ СПИСОК ПОЛЬЗОВАТЕЛЕЙ");
        }

        model.addAttribute("users", usersDto);
        model.addAttribute("userListOfRole", listOfRole);
        return "users";
    }

    @GetMapping("/users/addUser")
    public ModelAndView addUserForm(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView mav = new ModelAndView("add-user-form");
        UserDto userDto = new UserDto();
        mav.addObject("user", userDto);

        if(userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ФОРМУ ДОБАВЛЕНИЯ ПОЛЬЗОВАТЕЛЕЙ" );//
        }

        return mav;
    }

    @PostMapping("/users/addUser/save")
    public String addUser(@Valid @ModelAttribute("user") UserDto userDto,
                          @AuthenticationPrincipal UserDetails userDetails,
                          BindingResult result,
                          Model model) {
        User userInRepo = userService.findUserByEmail(userDto.getEmail());

        if (userInRepo != null && userInRepo.getEmail() != null && !userInRepo.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "на этот адресс электронной почты уже зарегистрирована учетная запись");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "add-user-form";
        }

        if(userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ДОБАВИЛ НОВОГО ПОЛЬЗОВАТЕЛЯ");
        }

        userService.saveUser(userDto);
        return "redirect:/users/addUser?success";
    }

}
