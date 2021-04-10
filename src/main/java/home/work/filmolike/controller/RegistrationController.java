package home.work.filmolike.controller;

import home.work.filmolike.domain.User;
import home.work.filmolike.service.NoteService;
import home.work.filmolike.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

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

        LOGGER.debug("inside the registration controller");

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("errorMessage", "Пароли не совпадают");
            return "registration";
        }

        boolean isAdded = userService.addUser(user);

        if (isAdded) {
            return "registration-confirmed";
        } else {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует");
            return "registration";
        }
    }
}
