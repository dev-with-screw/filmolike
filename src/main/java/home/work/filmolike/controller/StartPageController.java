package home.work.filmolike.controller;

import home.work.filmolike.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showHomePage(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        } else {
            return "redirect:/notes";
        }



    }

}
