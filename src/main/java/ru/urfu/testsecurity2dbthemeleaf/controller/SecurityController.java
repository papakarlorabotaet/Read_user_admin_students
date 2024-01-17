package ru.urfu.testsecurity2dbthemeleaf.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.urfu.testsecurity2dbthemeleaf.dto.UserDto;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;
import ru.urfu.testsecurity2dbthemeleaf.service.UserService;
import ru.urfu.testsecurity2dbthemeleaf.service.UsersActionService;

import javax.validation.Valid;


@Controller
public class SecurityController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersActionService usersActionService;
    @Autowired
    private UserService userService;


    @GetMapping("/index")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ГЛАВНУЮ СТРАНИЦУ");
        }


        return "index";
    }

    @GetMapping("/permissionDenied")
    public String permissionDenied(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКАЗАНО В ДОСТУПЕ");
        }

        return "permission-denied";
    }


    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ГЛАВНУЮ СТРАНИЦУ");
        }


        return "login";

    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);


        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               @AuthenticationPrincipal UserDetails userDetails,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "на этот адресс электронной почты уже зарегистрирована учетная запись");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ЗАРЕГИСТРИРОВАЛСЯ");
        }


        userService.saveUser(userDto);
        return "redirect:/register?success";
    }


}
