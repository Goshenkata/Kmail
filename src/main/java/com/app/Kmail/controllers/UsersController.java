package com.app.Kmail.controllers;

import com.app.Kmail.model.binding.UserRegistrationBindingModel;
import com.app.Kmail.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@Valid UserRegistrationBindingModel userRegistrationBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        /*
        the conditions for redirecting the user are
        1)Username does not contain special characters
        2) Validation in the binding model failed
        3) The username is not unique
        4) The passwords do not match
        */
        /// TODO: 3.01.22 г. fix invalid email input not showing
        //  TODO is password valid check "Your password must be at between 8 and 32 characters long, must contain at least one capital letter and a number"
        boolean isUsernameValid = userService.isUsernameValid(userRegistrationBindingModel.getUsername());
        boolean isUserNameTaken = userService.isUsernameTaken(userRegistrationBindingModel.getUsername());
        boolean passwordsDoNotMatch = !userRegistrationBindingModel.getPassword().equals(userRegistrationBindingModel.getConfirmPassword());
        if (!isUsernameValid || bindingResult.hasErrors() || isUserNameTaken) {
            redirectAttributes.addFlashAttribute("userRegistrationBindingModel", userRegistrationBindingModel);
            redirectAttributes.addFlashAttribute("containsSpecialChars", !isUsernameValid);
            redirectAttributes.addFlashAttribute("usernameTaken", isUserNameTaken);
            redirectAttributes.addFlashAttribute("passwordsRepeated", passwordsDoNotMatch);
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationBindingModel", bindingResult);
            }
            return "redirect:/users/register";
        }
        return "redirect:/";
    }

    @ModelAttribute("userRegistrationBindingModel")
    public UserRegistrationBindingModel userRegistrationBindingModel() {
        return new UserRegistrationBindingModel();
    }

}
