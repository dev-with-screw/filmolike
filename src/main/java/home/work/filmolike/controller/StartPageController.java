package home.work.filmolike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartPageController {
    @GetMapping("/")
    public String showNotes() {
            return "redirect:/notes";
    }
}
