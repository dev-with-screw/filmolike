package home.work.filmolike.controller;

import home.work.filmolike.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartPageController {
    @GetMapping("/")
    public String showNotes() {

        if () {
            return "redirect:/home";
        } else
            return "redirect:/notes";
    }
}
