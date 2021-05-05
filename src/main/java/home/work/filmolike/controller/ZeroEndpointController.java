package home.work.filmolike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class ZeroEndpointController {
    @GetMapping("/")
    public String redirect(Principal user) {
        if (user == null) {
            return "home";
        } else
            return "redirect:/notes";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
