package ru.urfu.testsecurity2dbthemeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.testsecurity2dbthemeleaf.entity.Role;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.entity.UsersAction;
import ru.urfu.testsecurity2dbthemeleaf.repository.RoleRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UsersActionRepository;
import ru.urfu.testsecurity2dbthemeleaf.service.UserService;
import ru.urfu.testsecurity2dbthemeleaf.service.UsersActionService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class UsersActionController {

    @Autowired
    private UsersActionService usersActionService;
    @Autowired
    private UsersActionRepository usersActionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/users/action")
    public String getUserAction(Model model,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ИСТОРИЮ ДЕЙСТВИЙ ПОЛЬЗОВАТЕЛЕЙ");
        }

        List<UsersAction> usersAction = usersActionService.findAllUsersAction();
        List<Role> roles = roleRepository.findAll();
        List<String> listOfRoles = new ArrayList<>(roles.stream().map(Role::getName).toList());//.collect(Collectors.toList());

        Collections.reverse(usersAction);
        Collections.reverse(listOfRoles);

        model.addAttribute("usersAction", usersAction);
        model.addAttribute("listOfRoles", listOfRoles);

        return "usersAction";
    }

    @GetMapping("/users/action/clearForm")
    public ModelAndView clearHistoryAction(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView mav = new ModelAndView("delete-history-users-action");
        User user = userService.findUserByEmail(userDetails.getUsername());
        mav.addObject("user", user);

        User userFromRepo = userRepository.findByEmail(userDetails.getUsername());
        usersActionService.saveUsersAction(userFromRepo, "ОТКРЫЛ ФОРМУ УДАЛЕНИЯ ИСТОРИИ ПОЛЬЗОВАТЕЛЕЙ");


        return mav;
    }

    @PostMapping("/users/action/clearForm/delete")
    public String clear(@Valid @ModelAttribute("user") User user,
                        @AuthenticationPrincipal UserDetails userDetails,
                        BindingResult result,
                        Model model) {

        if (passwordEncoder.matches(user.getPassword(), userRepository.findByEmail((userDetails.getUsername())).getPassword())) {

            usersActionRepository.deleteAll();


            User userFromRepo = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(userFromRepo, "ОЧИСТИЛ ИСТОРИЮ ПОЛЬЗОВАТЕЛЕЙ");

        } else {
            result.rejectValue("password", null,
                    "пароль указан не верно");

            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "delete-history-users-action";
            }


        }


        return "redirect:/users/action/clearForm?success";
    }

}
