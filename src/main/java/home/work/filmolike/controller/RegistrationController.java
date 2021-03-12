package home.work.filmolike.controller;

import home.work.filmolike.domain.User;
import home.work.filmolike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        if(bindingResult.hasErrors()) {
            return "registration";
        }

        boolean isAdded = userService.addUser(user);

        if (isAdded) {
            return "registration-confirmed";
        } else {
            model.addAttribute("userExists", "userExists");
            return "registration";
        }
    }
}
