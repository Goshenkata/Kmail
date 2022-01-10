package com.app.Kmail.controllers;

import com.app.Kmail.model.view.LoggedInUserIndexViewModel;
import com.app.Kmail.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("userData", userService.getData(principal.getName()));
        }
        return "index";
    }

    @ModelAttribute("userData")
    public LoggedInUserIndexViewModel loggedInUserIndexViewModel() {
        return new LoggedInUserIndexViewModel();
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
