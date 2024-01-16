package ru.urfu.testsecurity2dbthemeleaf.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.urfu.testsecurity2dbthemeleaf.entity.UsersAction;

import java.util.List;

@Controller
public class UserActionController {




    @GetMapping("/users/action")
    public String getUserAction(Model model,
                                @AuthenticationPrincipal UserDetails userDetails){
        List<UsersAction> usersAction
        return null;
    }
}
