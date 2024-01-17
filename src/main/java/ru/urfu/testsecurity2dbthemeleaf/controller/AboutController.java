package ru.urfu.testsecurity2dbthemeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;
import ru.urfu.testsecurity2dbthemeleaf.service.UsersActionService;

@Controller
public class AboutController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersActionService usersActionService;

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal UserDetails userDetails){

        if(userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ССЫЛКУ /about");
        }

        return "about";
    }
}
